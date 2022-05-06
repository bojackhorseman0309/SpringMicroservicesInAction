package com.optimagrowth.organization.service;

import java.util.Optional;
import java.util.UUID;

import brave.ScopedSpan;
import brave.Tracer;
import com.optimagrowth.organization.events.source.SimpleSourceBean;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.optimagrowth.organization.model.Organization;
import com.optimagrowth.organization.repository.OrganizationRepository;

@Service
public class OrganizationService {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(OrganizationService.class);

    @Autowired
    private OrganizationRepository repository;

    @Autowired
    SimpleSourceBean simpleSourceBean;

    @Autowired
    Tracer tracer;

    public Organization findById(String organizationId) {
        Optional<Organization> opt = null;
        ScopedSpan newSpan = tracer.startScopedSpan("getOrgDBCall");
        try {
            opt = repository.findById(organizationId);
            simpleSourceBean.publishOrganizationChange(SimpleSourceBean.ActionEnum.valueOf("GET"), organizationId);
            if (!opt.isPresent()) {
                String message = String.format("Unable to find an organization with theOrganization id %s",
                        organizationId);
                logger.error(message);
                throw new IllegalArgumentException(message);
            }
            logger.debug("Retrieving Organization Info: " +
                    opt.get().toString());
        } finally {
            newSpan.tag("peer.service", "postgres");
            newSpan.annotate("Client received");
            newSpan.finish();
        }
        return opt.get();
    }

    public Organization create(Organization organization) {
        organization.setId(UUID.randomUUID().toString());
        organization = repository.save(organization);
        simpleSourceBean.publishOrganizationChange(
                SimpleSourceBean.ActionEnum.CREATED,
                organization.getId());
        return organization;
    }

    public void update(Organization organization) {
        repository.save(organization);
    }

    public void delete(Organization organization) {
        repository.deleteById(organization.getId());
    }
}