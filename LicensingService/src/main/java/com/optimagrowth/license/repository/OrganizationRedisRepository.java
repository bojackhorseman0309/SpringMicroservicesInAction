package com.optimagrowth.license.repository;

import com.optimagrowth.license.model.Organization;
import org.springframework.data.repository.CrudRepository;

public interface OrganizationRedisRepository extends CrudRepository<Organization, String> {
}
