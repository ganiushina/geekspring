package com.geekbrains.geekspring.controllers;

import com.geekbrains.geekspring.entities.Product;
import com.geekbrains.geekspring.entities.SystemUser;
import com.geekbrains.geekspring.entities.User;
import com.geekbrains.geekspring.services.ProductService;
import com.geekbrains.geekspring.services.UserService;
import com.geekbrains.geekspring.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.jws.soap.SOAPBinding;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class    AdminController {
  //  private UserService userService;

    private UserServiceImpl userServiceImpl;

    @Autowired
    public void setUserService(UserService userService, UserServiceImpl userServiceImpl) {
    //    this.userService = userService;
        this.userServiceImpl = userServiceImpl;
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
    public String showEditForm(@PathVariable String username, Model model) {
        User user = userServiceImpl.findByUserName(username);
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
