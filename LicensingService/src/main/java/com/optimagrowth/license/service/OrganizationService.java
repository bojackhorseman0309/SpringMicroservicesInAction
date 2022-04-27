package com.optimagrowth.license.service;

import com.optimagrowth.license.model.Organization;
import com.optimagrowth.license.service.client.OrganizationRestTemplateClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService {

    @CircuitBreaker(name = "organizationService")
    private Organization getOrganization(String organizationId) {
        return OrganizationRestTemplateClient.getOrganization(organizationId);
    }

	
}
