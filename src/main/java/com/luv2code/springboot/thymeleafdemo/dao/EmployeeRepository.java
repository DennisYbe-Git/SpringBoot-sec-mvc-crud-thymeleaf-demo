package com.luv2code.springboot.thymeleafdemo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luv2code.springboot.thymeleafdemo.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    // that's it ... no need to write any code LOL!
	
	// add a method to sort all by field name
	// spring jpa will introspect using the method name for search criteria.
//	public List<Employee> findAllByOrderByFirstNameDesc();
//	public List<Employee> findAllByOrderByLastNameAsc();
	
}
