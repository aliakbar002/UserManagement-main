package com.usermanagement.repository;


import com.usermanagement.entity.UserOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserOrganizationRepository extends JpaRepository<UserOrganization, Integer> {
   // UserOrganization findByUserAndOrganization(int user, int organization);
    // UserOrganization findByUserId(int id);
   // UserOrganization findByUserContaining(int id);


//    @Query("select count(id) from UserOrganization  where organizationRoleId= ?1")
//    long countOrganizationByIdWhereOrganizationRoleId(int organizationRoleId);

//    @Query(value = "select count(id) from  UserOrganization where organizationRole =:organizationRole")
//    public Integer countOrganization(int organizationRole);

   // @Query(value = "select count(id) from UserOrganization  where organizationRole=:organizationRole")
   /// public Integer countOrganization(int organizationRole);

    List<UserOrganizationRepository> findByOrganizationRoleId(int organizationRoleId);

    //Native Query
    @Query(value = "select * from user_organization uo inner join organization org on uo.organization_id=org.id where uo.organization_role_id=1 and org.organization_admin=1", nativeQuery = true)
    List<UserOrganization> findByAdmin();

    //Native Query Desired
//    @Query(value = "select uo.organization_id, uo.organization_role_id, org.name, org.organization_admin from user_organization uo inner join organization org on uo.organization_id=org.id", nativeQuery = true)
//    List<UserOrganization> findByAdmin();

}
