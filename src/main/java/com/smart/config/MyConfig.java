package com.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class MyConfig {

    // 1️⃣ Defining UserDetailsService (Fetches user details from DB)
    @Bean
    public UserDetailsService getUserDetailService() {
        return new userDetailServiceImpul();
    }

    // 2️⃣ Defining Password Encoder (Encrypts passwords)
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 3️⃣ Authentication Provider (Tells Spring how to authenticate users)
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(getUserDetailService()); // Fetch user
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder()); // Encrypt password
        return daoAuthenticationProvider;
    }

    // 4️⃣ Authentication Manager (Manages authentication)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // 5️⃣ Security Configuration (Authorization rules)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
    	http.
    	csrf(csrf -> csrf.disable())
    	
    	.authorizeHttpRequests(auth -> auth
    	.requestMatchers("/admin/**").hasRole("ADMIN")
    	.requestMatchers("/user/**").hasRole("USER")
    	.requestMatchers("/**").permitAll())
    	
    	.formLogin(login -> login.loginPage("/signin")
    	.loginProcessingUrl("/dologin")
    	.defaultSuccessUrl("/user/index", true).permitAll());
    	
    	return http.build();
    	
    }
}

/*
 THIS IS OLD STYLE. THIS ONE IS NOW DEPRICATED.
package com.smart.config;

@Configuration
public class MyConfig extends WebSecurityConfigurerAdapter 
{
	@Bean
	public UserDetailsService getUserDetailService()
	{
		return new userDetailServiceImpul();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider()
	{
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.getUserDetailService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		
		return daoAuthenticationProvider;
	}
	
	//Configure Method
	@Override 
	protected void configure (AuthenticationManagerBuilder auth) throws Exception 
	{ 
		auth.authenticationProvider(authenticationProvider());
	}
	
	@Override 
	protected void configure(HttpSecurity http) throws Exception 
	{ 
		http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN")
		.antMatchers("/user/**").hasRole("USER")
		.antMatchers("/**").permitAll().and().formLogin().and().csrf.disable();
	}
}
*/