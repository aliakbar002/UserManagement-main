package com.usermanagement.controller;

import com.usermanagement.entity.Organization;
import com.usermanagement.entity.User;
import com.usermanagement.entity.UserOrganization;
import com.usermanagement.response.DefaultResponse;
import com.usermanagement.service.UserService;
import com.usermanagement.utils.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class UserController {

    private static final Logger LOGGER = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService userService;



    //Task 1
    @PostMapping({"createUser", "signUp"})
    public ResponseEntity create(@RequestBody User user, @RequestParam Integer roleId, @RequestParam Integer createdBy) throws Exception {
        LOGGER.info("Received crate user request");

        ResponseEntity<DefaultResponse> response;

        //bonus validation check
        if (Util.ValidateSigUpRequest(user, roleId, createdBy))
            response = userService.create(user, roleId, createdBy);
        else
            response = new ResponseEntity<DefaultResponse>(new DefaultResponse("Your request data isn't correct","F02",user),HttpStatus.NOT_ACCEPTABLE);
        return response;
    }


    //Task 5 Login call
    @PostMapping("/login")
    public ResponseEntity Login(@RequestBody User request) throws Exception {
        LOGGER.info("Received login request");

        User user = userService.Login(request);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    //Task 8
    @GetMapping("/getAllUsers")
    public ResponseEntity<List> Login(@RequestParam Integer userId) throws Exception {
        LOGGER.info("Received getAllUsers request");

        List<User> list = userService.getAllUsers(userId);
        return new ResponseEntity<List>(list, HttpStatus.OK);
    }


    //Task 9
    @PostMapping("/updateSingleUser")
    public ResponseEntity updateSingleUser(@RequestBody User request) throws Exception {
        LOGGER.info("Received updateSingleUser request");

        ResponseEntity<DefaultResponse> response = userService.updateSingleUser(request);
        return response;
    }


    //Task 10 , 11
    @PostMapping("/updateMultipleUser")
    public ResponseEntity updateMultipleUser(@RequestBody List<User> userList) throws Exception {
        LOGGER.info("Received updateMultipleUser request");

        ResponseEntity<DefaultResponse> response = userService.updateMultipleUser(userList);
        return response;
    }

      //Add Organization // Task 12
    @PostMapping("/organization")
    public ResponseEntity add(@RequestBody Organization organization,@RequestParam Integer parentOrganization,@RequestParam Integer organizationAdmin,@RequestParam Integer createdBy,@RequestParam Integer organizationRoleId) throws Exception{
        LOGGER.info("Recevied add organization request");

        ResponseEntity<DefaultResponse> response;
        if (Util.ValidateAddOrganizationRequest(organization, parentOrganization, organizationAdmin, createdBy, organizationRoleId))
            response = userService.addOrganization(organization,parentOrganization,organizationAdmin, createdBy,  organizationRoleId);
           else
           response = new ResponseEntity<DefaultResponse>(new DefaultResponse("Requested data is incorrect","F02",organization),HttpStatus.NOT_ACCEPTABLE);
      return response;

    }

    // Task 13
    //Add into user_organization
    @PostMapping("/user")
    public ResponseEntity add(@RequestParam Integer userId, @RequestParam Integer organizationId,@RequestParam Integer organizationRoleId){
        LOGGER.info("Received add user request");
        ResponseEntity<DefaultResponse> response;

            response = userService.addUser(userId, organizationId,organizationRoleId);
        return response;

    }
    //Task 14
    //Fetch organization against parent
    @GetMapping("/parentOrganization")
    public Organization getAll(Integer parentOrganization) throws Exception {
        LOGGER.info("Recevied fetch parent organization request");

   return userService.getParentOrganization(parentOrganization);

    }

    //Task 15
    //fetch unapproved organization
    @GetMapping("/getUnApproved")
    public ResponseEntity<Organization> status(@RequestParam Integer status){
        LOGGER.info("Received get un approved status request");
        Organization organization = userService.getStatus(status);
        return new ResponseEntity<>(organization, HttpStatus.OK);
    }



    //Task 16
    // update organizationRole
    @PutMapping("/updateOrganizationRole/{id}")
    public UserOrganization updateOrganizationRole(
            @PathVariable int id,@RequestParam Integer organizationRoleId) throws Exception{
        LOGGER.info("Recevied update organization status request");

        return userService.updateSingleRole(id,organizationRoleId);
    }

    //fetch unapproved organization
//    @GetMapping("/unapproved")
//    @ResponseBody
//    public List<Organization> getAllUnApproved(@RequestParam int status){
//
//        List<Organization> test = userService.getAllUnApproved(status);
//        return test;
//    }


}
