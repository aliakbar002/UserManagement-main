package com.usermanagement.repository;

import com.usermanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmailAndPassword( String email, String password);

     User findById(int id);
     User findByEmail(String email);

      List<User> findAllByCreatedBy(User createdBy);
}
