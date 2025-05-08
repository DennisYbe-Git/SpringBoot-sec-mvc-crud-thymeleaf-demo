package com.luv2code.springboot.thymeleafdemo.security;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class DemoSecurityConfig {
	
	// for Spring custom security db schema for our tables members and roles
	//	
	@Bean
	public UserDetailsManager userDetailsManager(DataSource dataSource) throws SQLException {
		
		// pwd is fun123
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
		
		// need to get the users and roles from custom table name in db
		jdbcUserDetailsManager.setUsersByUsernameQuery("select user_id, pw, active from members where user_id=?");
		jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select user_id, role from roles where user_id=?");
				
		return jdbcUserDetailsManager;
	}
	
	
	// for Spring default security db schema for tables users and authorities
	//
	/*
	@Bean
	public UserDetailsManager userDetailsManager(DataSource dataSource) throws SQLException {
		Connection conn = dataSource.getConnection();
		System.out.println(">>>>"+conn.getCatalog());
		
		return new JdbcUserDetailsManager(dataSource);
	}
	*/
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
		
		security.authorizeHttpRequests(authorize ->
		authorize
		.requestMatchers("/css/**").permitAll()
		.requestMatchers(HttpMethod.GET, "/employees/list/**").hasRole("EMPLOYEE")
//		.requestMatchers(HttpMethod.GET, "/leaders/**").hasRole("MANAGER")
		.requestMatchers(HttpMethod.GET, "/employees/showAddForm/**").hasRole("ADMIN")
		.requestMatchers(HttpMethod.GET, "/employees/showEditForm/**").hasAnyRole("ADMIN","MANAGER")
		.requestMatchers(HttpMethod.POST, "/employees/delete/**").hasRole("ADMIN")
		.anyRequest().authenticated()
		)
		
		.exceptionHandling(exception ->
		exception
		.accessDeniedPage("/errorAuth")
		)
		
		.formLogin(form -> 
		form
		.loginPage("/showMyLoginPage") // if there is an error the login page will redisplay with ?error param to handle in the form or controller
		.loginProcessingUrl("/authenticateTheUser") // This is a free mapping from Spring, no request mapping needed in controller.
		.permitAll()
		)
		
		// free endpoint /logout to invalidate user session. 
		// Spring will redirect to the login page with ?logout param
		.logout(logout -> logout.permitAll() 
		
		);
		
//		// disable Cross Site Request Forgery (CSRF) (not needed for stateless REST API for POST,PUT,DELETE)
//		security.csrf(csrf -> csrf.disable());
		
		return security.build();
	}
	
//	@Bean
//	public InMemoryUserDetailsManager userDetailsManager() {
//		
//		UserDetails john = User.builder()
//				.username("john")
//				.password("{noop}test123")
//				.roles("EMPLOYEE")
//				.build();
//		UserDetails mary = User.builder()
//				.username("mary")
//				.password("{noop}test123")
//				.roles("EMPLOYEE","MANAGER")
//				.build();
//		UserDetails susan = User.builder()
//				.username("susan")
//				.password("{noop}test123")
//				.roles("EMPLOYEE","MANAGER","ADMIN")
//				.build();
//		
//		return new InMemoryUserDetailsManager(john,mary,susan);
//	}

}
