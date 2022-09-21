package com.usermanagement.repository;

import com.usermanagement.entity.Organization;
import com.usermanagement.entity.OrganizationRole;
import com.usermanagement.entity.User;
import com.usermanagement.entity.UserOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRoleRepository extends JpaRepository<OrganizationRole,Integer> {

    OrganizationRole findById(int id);
   // UserOrganization findByUserAndOrganization(int user, int organization);
}
