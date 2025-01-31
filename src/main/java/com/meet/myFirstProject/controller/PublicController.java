package com.meet.myFirstProject.controller;

import com.meet.myFirstProject.entity.User;
import com.meet.myFirstProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicController
{
    @Autowired
    private UserService userService;

    @GetMapping("check")
    public String healthCheck()
    {
        return "OK";
    }

    @PostMapping("create-user")
    public void createuser(@RequestBody User user)
    {
        userService.saveUser(user);
    }
}
