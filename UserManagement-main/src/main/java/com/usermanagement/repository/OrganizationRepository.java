package com.usermanagement.repository;

import com.usermanagement.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization,Integer> {

    Organization findByName(String name);
    Organization findById(int id);
    Organization findOrganizationById(int id);

 //  List <Organization> findOrganizationsByParentOrganizationIsNull();
    List<Organization> findOrganizationsByParentOrganization(int parentOrganization);

    List<Organization> findOrganizationsByStatus(int status);
   // Organization findStatusById(int status);
  //  Organization findOrganizationsById(int id);

  //  List<Organization> findByRolesIn(Collection<String> names);





    //List<Organization> findByStatusEquals(int status);
// List<Organization> findOrganizationByStatus(int status);


}

