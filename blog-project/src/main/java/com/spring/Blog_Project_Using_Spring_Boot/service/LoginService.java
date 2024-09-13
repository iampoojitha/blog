package com.spring.Blog_Project_Using_Spring_Boot.service;

import com.spring.Blog_Project_Using_Spring_Boot.model.Users;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface LoginService {

    public void saveUser(Users user);

    public List<String> userNames();

    public List<String> userEmails();
//    Users findByUsername(String username);
//
//    Users findByEmail(String email);
}
