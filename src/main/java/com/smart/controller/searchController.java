package com.smart.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smart.dao.contactRepository;
import com.smart.dao.userRepository;
import com.smart.entities.contact;
import com.smart.entities.User;

@RestController
public class searchController 
{
	@Autowired
	private userRepository Ur;
	
	@Autowired
	private contactRepository Cr;
	
	//Search Handler
	@GetMapping("/search/{query}")
	public ResponseEntity<List<contact>> search(@PathVariable("query") String query,Principal p)
	{
		System.out.println(query);
		User User = Ur.getUserByUserName(p.getName());
		List<contact> contact = Cr.findByNameContainingAndUser(query, User);
		
		return ResponseEntity.ok(contact);
	}
}
