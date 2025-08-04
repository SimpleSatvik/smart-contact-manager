package com.smart.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import com.smart.dao.userRepository;
import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.service.emailService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
public class HomeController 
{
	@Autowired
	private userRepository UserRepository;
	
	@Autowired
	private emailService EmailService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	
	@RequestMapping("/")
	public String home( Model model)
	{
		model.addAttribute("Title", "Home - Smart Contact Manager");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about( Model model)
	{
		model.addAttribute("Title", "About - Smart Contact Manager");
		return "about";
	}
	
	//handler for registration 
	@RequestMapping("/signup")
	public String signup( Model model)
	{
		model.addAttribute("Title", "Register - Smart Contact Manager");
		model.addAttribute("user", new User());
		
		return "signup";
	}
	
	//Handler for login
	@GetMapping("/signin")
	public String customLogin(Model model)
	{
		model.addAttribute("Title", "Login Page");
		return "login";
	}
	
	//This is the handler for Registering User
	@RequestMapping(value = "/do_register", method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User User, 
            BindingResult result1, 
            @RequestParam(value = "agreement", defaultValue = "false") boolean agreement,  
            Model model, HttpSession session) 
	{
	    try {
	        if (!agreement) {
	        	System.out.println("You have not agreed to the terms and conditions!");
	            throw new Exception("You have not agreed to the terms and conditions!");
	        }
	        
	        if(result1.hasErrors())
	        {
	        	System.out.println("ERROR " + result1.toString());
	        	model.addAttribute("user", User);
	        	return "signup";
	        }
	        
	        User.setRole("ROLE_USER");
	        User.setEnabled(true);
	        User.setImageUrl("default.jpg");
	        User.setPassword(passwordEncoder.encode(User.getPassword()));

	        System.out.println("Agreement " + agreement);
	        System.out.println("User " + User);
	        
	        

	        session.setAttribute("user", User);
	        model.addAttribute("message", new Message("Registered Successfully!", "alert-success"));
	        session.setAttribute("message", new Message("Registered Successfully!", "alert-success"));
	    } 

	    catch (Exception e) {
	        //e.printStackTrace();
	        model.addAttribute("user", User);
	        model.addAttribute("message", new Message("Something went wrong! " + "<br>" + e.getMessage(), "alert-danger"));
	        return "signup";
	    }

	    return "redirect:/send-otp";
	}
	
	//verify if an email exists or not
	@GetMapping("/send-otp")
	public String sendOTP(HttpSession session)
	{
		User user = (User) session.getAttribute("user");
		String email = user.getEmail();
		
		//generating otp of 4 digits
		Random random = new Random();
		int otp = 100000 + random.nextInt(900000); 
		
		System.out.println("OTP : " + otp);

		//Write code for send OTP to email...
		String subject = "OTP From Smart Contact Manager";
		String msg = ""
				+ "<div style = 'border : 1px solid #e2e2e2; padding : 20px;'>"
				+ "<h1>"
				+ "OTP is "
				+ "<b>"+otp+"</b>"
				+ "</h1>"
				+ "</div>";
		String to = email;
		
		boolean flag = this.EmailService.sendEmail(subject, msg, to);
		
		if(flag)
		{
			session.setAttribute("myOTP", otp);
			session.setAttribute("email", email);
			return "verify_user";
		}
			
		
		session.setAttribute("message", "Check your email id!!");
		return "signup";
	}
	
	//used for registration
	@PostMapping("/verify-user")
	public String verifyOtp(@RequestParam("OTP") Integer otp, HttpSession session, Model model)
	{
		Integer myOTP = (int) session.getAttribute("myOTP");
		String email = (String) session.getAttribute("email");
		
		if(myOTP.equals(otp))
		{
			User user = (User) session.getAttribute("user");
				
			try
	        {
				 
	        	 User result = this.UserRepository.save(user);
	        	 model.addAttribute("message", new Message("Registered Successfully!", "alert-success"));
	        	 return "login";
	        	 
	        }
			catch(DataIntegrityViolationException e)
		    {
		    	model.addAttribute("user", user);
		        model.addAttribute("message", new Message("Something went wrong!<br>User Already Exists!", "alert-danger"));
		        return "verify_user";
		    }
		    catch (Exception e) {
		        //e.printStackTrace();
		        model.addAttribute("user", user);
		        model.addAttribute("message", new Message("Something went wrong! " + "<br>" + e.getMessage(), "alert-danger"));
		        return "verify_user";
		    }


		}
				
		
		
		else
		{
			model.addAttribute("message", new Message("Wrong Otp!", "alert-danger"));
			return "verify_user";
		}
	
	}

}
