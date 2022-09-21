package com.usermanagement.repository;


import com.usermanagement.entity.Organization;
import com.usermanagement.entity.User;
import com.usermanagement.entity.UserOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOrganizationRepository extends JpaRepository<UserOrganization, Integer> {
    UserOrganization findByUserAndOrganization(int user, int organization);
    // UserOrganization findByUserId(int id);
   // UserOrganization findByUserContaining(int id);


}
