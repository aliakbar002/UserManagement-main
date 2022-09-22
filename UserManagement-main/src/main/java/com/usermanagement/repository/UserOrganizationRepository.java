package com.usermanagement.repository;


import com.usermanagement.entity.UserOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOrganizationRepository extends JpaRepository<UserOrganization, Integer> {
    UserOrganization findByUserAndOrganization(int user, int organization);
    // UserOrganization findByUserId(int id);
   // UserOrganization findByUserContaining(int id);


//    @Query("select count(id) from UserOrganization  where organizationRoleId= ?1")
//    long countOrganizationByIdWhereOrganizationRoleId(int organizationRoleId);

//    @Query(value = "select count(id) from  UserOrganization where organizationRole =:organizationRole")
//    public Integer countOrganization(int organizationRole);

    @Query(value = "select count(id) from UserOrganization  where organizationRole=:organizationRole")
    public Integer countOrganization(int organizationRole);



}
