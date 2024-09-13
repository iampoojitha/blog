package com.spring.Blog_Project_Using_Spring_Boot.controller;

import com.spring.Blog_Project_Using_Spring_Boot.model.Users;
import com.spring.Blog_Project_Using_Spring_Boot.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class LoginController {

    private final LoginService loginService;


    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/showLoginPage")
    public String showLoginPage(Model model){
        model.addAttribute("successMessage","Registered successfully please login to continue");
        return "security/login";
    }

    @GetMapping("/createUser")
    public String showRegisterForm(Model model){

        model.addAttribute("user",new Users());
        return "security/register";
    }

    @PostMapping("/saveUserDetails")
    public String  saveUserDetails(@ModelAttribute Users user, Model model, RedirectAttributes redirectAttributes) {

        List<String > userNames = loginService.userNames();
        List<String> userEmails = loginService.userEmails();
        if(userNames.contains(user.getName())){
            redirectAttributes.addFlashAttribute("errorMessage", "Existing username");
            return "redirect:/createUser";
        }
       if(userEmails.contains(user.getEmail())){
           redirectAttributes.addFlashAttribute("errorMessage", "Existing username");
           return "redirect:/createUser";
       }
        else {
            loginService.saveUser(user);
            model.addAttribute("successMessage", "Registered successfully, please login to continue.");
            redirectAttributes.addFlashAttribute("successMessage", "Registered successfully, please login to continue.");
            return "redirect:/showLoginPage";
        }
    }


}
