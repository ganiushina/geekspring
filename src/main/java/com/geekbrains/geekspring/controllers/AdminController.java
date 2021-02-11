package com.geekbrains.geekspring.controllers;

import com.baeldung.grpc.UserRoleRequest;
import com.baeldung.grpc.UserRoleResponse;
import com.baeldung.grpc.UserRoleServiceGrpc;
import com.geekbrains.geekspring.entities.Product;
import com.geekbrains.geekspring.entities.Role;
import com.geekbrains.geekspring.entities.SystemUser;
import com.geekbrains.geekspring.entities.User;
import com.geekbrains.geekspring.services.ProductService;
import com.geekbrains.geekspring.services.UserRoleServiceImpl;
import com.geekbrains.geekspring.services.UserService;
import com.geekbrains.geekspring.services.UserServiceImpl;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.jws.soap.SOAPBinding;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class   AdminController {
  //  private UserService userService;

    private UserServiceImpl userServiceImpl;
    private UserRoleServiceImpl userRoleService;


    @Autowired
    public void setUserService(UserService userService, UserServiceImpl userServiceImpl, UserRoleServiceImpl userRoleService) {
    //    this.userService = userService;
        this.userServiceImpl = userServiceImpl;
        this.userRoleService = userRoleService;
    }

    @GetMapping("")
    public String adminHome(Principal principal, Model model) {
//        User user = userService.findByUserName(principal.getName());
        User user = userServiceImpl.findByUserName(principal.getName());

        String email = "unknown";
        if (user != null) {
            email = user.getEmail();
        }
        List<User> usersList = userServiceImpl.getAllUsers();
        model.addAttribute("usersList", usersList);
        model.addAttribute("email", email);
        return "admin-panel";
    }

    @GetMapping("/edit/{username}")
    public String showEditForm(@PathVariable String username, Model model) throws IOException {
        User user = userServiceImpl.findByUserName(username);
        Integer i = 0;

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8189)
        .usePlaintext()
        .build();

        UserRoleServiceGrpc.UserRoleServiceBlockingStub stub = UserRoleServiceGrpc.newBlockingStub(channel);

        Collection<com.geekbrains.geekspring.entities.Role> roles = user.getRoles();

        UserRoleRequest.Builder builder = UserRoleRequest.newBuilder();

        for (Role role : roles) {
            builder.addRoles(com.baeldung.grpc.Role.newBuilder().setId(Math.toIntExact(role.getId())).setName(role.getName()).build());
        }


        UserRoleRequest userRoleRequest = builder.build();
        UserRoleResponse userRoleResponse = stub.userRole(userRoleRequest);

        //userRoleService.userRole(userRoleRequest, userRoleResponse.);
        System.out.println("Response received from server:\n" + userRoleResponse.getGreeting());

        model.addAttribute("role", userRoleResponse.getGreeting());
        model.addAttribute("user", user);
        return "edit_user_form";
    }


    @PostMapping("/edit")
    public String modifyUser(@ModelAttribute User user) {
        User userOld = userServiceImpl.findById(user.getId());
        userOld.setEmail(user.getEmail());
        userOld.setUserName(user.getUserName());
        userServiceImpl.saveOrUpdate(userOld);
        return "redirect:/admin/";
    }


}
