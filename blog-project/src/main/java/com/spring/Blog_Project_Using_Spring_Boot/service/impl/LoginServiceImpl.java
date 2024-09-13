package com.spring.Blog_Project_Using_Spring_Boot.service.impl;


import com.spring.Blog_Project_Using_Spring_Boot.model.Users;
import com.spring.Blog_Project_Using_Spring_Boot.repository.LoginRepository;
import com.spring.Blog_Project_Using_Spring_Boot.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {
    private final LoginRepository loginRepository;

    public LoginServiceImpl(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public void saveUser(Users user) {
            System.out.println("Saving user: " + user.getName()); // Debug statement
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole("author");
            loginRepository.save(user);
        }

   public List<String > userNames(){
        return loginRepository.userNames();
   }
    public List<String > userEmails(){
        return loginRepository.userEmails();
    }


}

//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Users user = loginRepository.findByUsername(username);
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found");
//        }
//        return User.withUsername(user.getUsername())
//                .password(user.getPassword())
//                .roles(user.getRole().name())
//                .build();
//    }


