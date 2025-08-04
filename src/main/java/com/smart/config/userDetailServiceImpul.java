package com.smart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart.dao.userRepository;
import com.smart.entities.User;

public class userDetailServiceImpul implements UserDetailsService
{
	@Autowired
	private userRepository UserRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		// Fetching user from database
		User User = UserRepository.getUserByUserName(username);
		
		if(User == null)
		{
			throw new UsernameNotFoundException("Could not find user");
		}
		
		CustomUserDetails customUserDetail = new CustomUserDetails(User);
		
		return customUserDetail;
	}

}
