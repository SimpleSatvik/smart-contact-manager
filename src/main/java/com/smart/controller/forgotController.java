package com.smart.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.userRepository;
import com.smart.entities.User;
import com.smart.service.emailService;

import jakarta.servlet.http.HttpSession;



@Controller
public class forgotController 
{
	@Autowired
	private emailService EmailService;
	//email id form opener
	
	@Autowired
	private userRepository Ur;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping("/forgot")
	public String openEmailForm()
	{
		return "forgot_email_form";
	}
	
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email, HttpSession session)
	{
		
		User user = this.Ur.getUserByUserName(email);
		
		if(user == null)
		{
			//send error msg
			session.setAttribute("message", "User does not exists with this email!!!");
			return "forgot_email_form";
		}
		
		System.out.println("EMAIL : " + email);
		
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
			return "verify_otp";
		}
			
		
		session.setAttribute("message", "Check your email id!!");
		return "forgot_email_form";
	}
	
	//Verify otp
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("OTP") Integer otp, HttpSession session)
	{
		Integer myOTP = (int) session.getAttribute("myOTP");
		String email = (String) session.getAttribute("email");
		
		if(myOTP.equals(otp))
		{
			
				//send change password form
				return "password_change_form";
			}
				
		
		
		else
		{
			session.setAttribute("message", "You have entered wrong OTP!");
			return "verify_otp";
		}
	
	}
	
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("newPassword") String newPassword, HttpSession session)
	{
		String email = (String) session.getAttribute("email");
		User user = this.Ur.getUserByUserName(email);
		user.setPassword(this.passwordEncoder.encode(newPassword));
		this.Ur.save(user);
		
		return "redirect:/signin?change=Password Changed Successfully";
	}
}
