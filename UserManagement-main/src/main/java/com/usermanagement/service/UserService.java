package com.usermanagement.service;

import com.usermanagement.entity.*;
import com.usermanagement.repository.*;
import com.usermanagement.utils.Util;
import com.usermanagement.exception.GeneralExceptionWithMessage;
import com.usermanagement.response.DefaultResponse;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class UserService {

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserOrganizationRepository userOrganizationRepository;

    @Autowired
    private OrganizationRoleRepository organizationRoleRepository;


    //  post mapping password encryption
    public ResponseEntity<DefaultResponse> create(@RequestBody User user, int roleID, int createdById) throws Exception {

        //Task 2 Email must be unique
        if (userRepository.findByEmail(user.getEmail())!=null)
            return new ResponseEntity<DefaultResponse>(new DefaultResponse("Email already exist", "F01", null), HttpStatus.NOT_ACCEPTABLE);
        User createdBy = userRepository.findById(createdById);
        if (createdBy == null)
            return new ResponseEntity<DefaultResponse>(new DefaultResponse("Created by user not found", "F01", null), HttpStatus.NOT_ACCEPTABLE);

        if(!validateUserRole(createdBy))
            return new ResponseEntity<DefaultResponse>(new DefaultResponse("You don't have rights to create a new user", "F01", null), HttpStatus.NOT_ACCEPTABLE);


        Role role = roleRepository.findById(roleID);
        if (role == null)
            return new ResponseEntity<DefaultResponse>(new DefaultResponse("Role not found", "F01", null), HttpStatus.NOT_ACCEPTABLE);

        //Task 3 Encrypt the password
        String base64Password = Util.encryptStringToBase64(user.getPassword());
        user.setPassword(base64Password);

        //task 4 set roles and created by user
        user.setRole(role);
        user.setCreatedBy(createdBy);
        user.setStatus(1);
        userRepository.save(user);
        return new ResponseEntity<DefaultResponse>(new DefaultResponse("User created successfully", "S01", null), HttpStatus.OK);

    }


    //post mapping for login
    public User Login(User request) throws Exception {
        User user = userRepository.findByEmailAndPassword(request.getEmail(), Util.encryptStringToBase64(request.getPassword()));

        //validate user
        if (user == null)
            throw new GeneralExceptionWithMessage("user is not exist");
        else {
            //Task 6 save and get their last login detail
            Timestamp lastLoginTimestamp = user.getLastLogin();
            user.setLastLogin(Timestamp.valueOf(LocalDateTime.now()));
            userRepository.save(user);
            user.setLastLogin(lastLoginTimestamp);
            user.setPassword(Util.decryptStringToBase64(user.getPassword()));

            return user;
        }
    }

    public List<User> getAllUsers(int userId) {

        List<User> userList = null;
        User user = userRepository.findById(userId);

        //Task 9
        //validate user has admin rights or moderator
        if(validateUserRole(user)){
            if (user.getRole().getId() == 2) {
                userList = userRepository.findAllByCreatedBy(user);
            }
            else
                userList = userRepository.findAll();
        }else {
           LOGGER.info("You don't have rights to see all users");

        }
        return userList;
    }




    public ResponseEntity<DefaultResponse> updateSingleUser(User request) throws Exception {
        User user = userRepository.findByEmailAndPassword(request.getEmail(), Util.encryptStringToBase64(request.getPassword()));

        //validate user
        if (user == null)
            return new ResponseEntity<DefaultResponse>(new DefaultResponse("User not found", "F01", null), HttpStatus.NOT_ACCEPTABLE);
        else {
         userRepository.save(request);

            return new ResponseEntity<DefaultResponse>(new DefaultResponse("User updated successfully", "S01", null), HttpStatus.OK);
        }
    }


    public ResponseEntity<DefaultResponse> updateMultipleUser(List<User> userList) {
        for (User user:userList) {
            User userEntity = userRepository.findById(user.getId());
            if (userEntity == null)
                LOGGER.info("user "+ user.getId()+" not found");
            else
                userRepository.save(user);

        }

        return new ResponseEntity<DefaultResponse>(new DefaultResponse("Users updated successfully", "S01", null), HttpStatus.OK);
    }


    //post mapping for Organization
    public ResponseEntity<DefaultResponse> addOrganization(Organization organization, int parentOrganization , int organizationAdmin, int createdBy, int updatedBy) {
        //task 1 organization insertion
        if (organizationRepository.findByName(organization.getName())!=null)
            return new ResponseEntity<>(new DefaultResponse("Name already Exist","F01",null),HttpStatus.NOT_ACCEPTABLE);
        Organization parent=organizationRepository.findById(parentOrganization);
        User orgAdmin=userRepository.findById(organizationAdmin);
        User createBy = userRepository.findById(createdBy);
        User updateBy = userRepository.findById(updatedBy);
        OrganizationRole organizationRole = organizationRoleRepository.findById(1);
        if (organizationRole==null)
            return new ResponseEntity<>(new DefaultResponse("organizationRole should not null","F01",null), HttpStatus.NOT_ACCEPTABLE);
        if (orgAdmin==null)
            return new ResponseEntity<>(new DefaultResponse("organizationAdmin should not null","F01",null), HttpStatus.NOT_ACCEPTABLE);
        if (parent==null)
            return new ResponseEntity<>(new DefaultResponse("organization should not null","F01",null), HttpStatus.NOT_ACCEPTABLE);
        if (createBy==null)
            return new ResponseEntity<>(new DefaultResponse("createdBy should not null","F01",null), HttpStatus.NOT_ACCEPTABLE);
        if (updateBy==null)
            return new ResponseEntity<>(new DefaultResponse("updatedBy should not null","F01",null), HttpStatus.NOT_ACCEPTABLE);
        if (!validateAdmin(createBy))
           return new ResponseEntity<>(new DefaultResponse("User is not an admin","F01",null),HttpStatus.NOT_ACCEPTABLE);
        if (!validateAdmin(updateBy))
            return new ResponseEntity<>(new DefaultResponse("User is not an admin","F01",null),HttpStatus.NOT_ACCEPTABLE);

        organization.setOrganizationAdmin(orgAdmin);
        organization.setParentOrganization(parent);
        organization.setCreatedBy(createBy);
        organization.setUpdatedBy(updateBy);
        organization.setStatus(0); //0=pending , 1=approve
        organizationRepository.save(organization);
       // return new ResponseEntity<DefaultResponse>(new DefaultResponse("Organization added successfully", "S01", null), HttpStatus.OK);
        // task 2 user insertion

      UserOrganization userOrganization = new UserOrganization();
      userOrganization.setUser(orgAdmin);
      userOrganization.setOrganization(organization);
      userOrganization.setOrganizationRole(organizationRole);
      userOrganization.setStatus(1);
      userOrganizationRepository.save(userOrganization);

         return new ResponseEntity<DefaultResponse>(new DefaultResponse("Organization added successfully", "S01", null), HttpStatus.OK);


    }

    // post mapping for User
    public ResponseEntity<DefaultResponse> addUser(int userId, int organizationId, int organizationRoleId) {

        //Organization user= organizationRepository.findUserById(userId);
        //Organization userOrg = organizationRepository.findByUser(userId);
        User user=userRepository.findById(userId);
        if (user==null)
            return new ResponseEntity<DefaultResponse>(new DefaultResponse("user does not exist", "S01", null), HttpStatus.NOT_ACCEPTABLE);

        OrganizationRole organizationRole = organizationRoleRepository.findById(organizationRoleId);
        if (organizationRole==null)
            return new ResponseEntity<DefaultResponse>(new DefaultResponse("organizationRole does not exist", "S01", null), HttpStatus.NOT_ACCEPTABLE);

        Organization organization = organizationRepository.findOrganizationById(organizationId);
        if (organization==null)
            return new ResponseEntity<DefaultResponse>(new DefaultResponse("organization does not exist", "S01", null), HttpStatus.NOT_ACCEPTABLE);

        UserOrganization userOrganization=new UserOrganization(1,user,organization,organizationRole);// By the help of constructor
 //        UserOrganization userOrganization1=new UserOrganization();// By manually getters and setters
//        userOrganization.setUser(user);
//        userOrganization.setStatus(1);
//        userOrganization.setOrganizationRole(organizationRole);
//        userOrganization.setOrganization(organization);
        userOrganizationRepository.save(new UserOrganization(1,user,organization,organizationRole));
        return new ResponseEntity<DefaultResponse>(new DefaultResponse("User added successfully", "S01", null), HttpStatus.OK);
    }

    // Role validation
    private boolean validateUserRole(User user) {
        if (user.getRole().getId() != 3) //simple user
            return true;
        else
            return false;

    }
    //Admin validation
    private boolean validateAdmin(User user){
        if (user.getRole().getId()==1)
            return true;
        else
            return false;
    }



    //fetch parent organization
    public List<Organization> getParentOrganization(int parentOrganization) {

   List <Organization> organizationList =   organizationRepository.findParentOrganizationById(parentOrganization);

     return organizationList;

    }

    // fetch unapproved status
    public List<Organization> getStatus(int status) {

        List<Organization> organizationList = organizationRepository.findOrganizationsByStatus(status);

        return organizationList;
    }

    //update single Role
    public UserOrganization updateSingleRole(int id, int organizationRoleId) throws Exception {

        UserOrganization userOrganization = userOrganizationRepository.findById(id).get();

        OrganizationRole organizationRole = organizationRoleRepository.findById(organizationRoleId);
        if (organizationRole==null)
            throw new Exception("Organization Role does not exist");
       if (userOrganization!=null) {
           userOrganization.setOrganizationRole(organizationRole);
           UserOrganization updatedOrganization = userOrganizationRepository.save(userOrganization);
           return updatedOrganization;
       }
       else{
           throw new Exception("userOrganization is null");
       }
    }

    // fetching Organizations contains more than 1 admin
    public List<UserOrganization> getByAdmins() {

        List<UserOrganization> adminList = userOrganizationRepository.findByAdmin();
        return adminList;


    }


}



