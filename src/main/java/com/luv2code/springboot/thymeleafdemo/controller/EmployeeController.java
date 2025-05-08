package com.luv2code.springboot.thymeleafdemo.controller;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luv2code.springboot.thymeleafdemo.entity.Employee;
import com.luv2code.springboot.thymeleafdemo.service.EmployeeService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/employees")
public class EmployeeController {
	
	static Logger logger = Logger.getLogger(EmployeeController.class.getSimpleName());
	private EmployeeService employeeService;
	private String direction = "asc";
	private String fieldName = "lastName";

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		// trims leading and trailing whitespace from request input
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	// list the employees
	@GetMapping("list")
	public String listEmployees(@RequestParam(name="sort", required=false) String field,
								@RequestParam(name="direction", required=false) String direct,
								Model theModel) {
	
		if (field == null) 
			field = fieldName;
		
		// allows to toggle
		if (direct == null) {
			if (direction.equals("asc")) 
				direction = "desc";			
			else 
				direction = "asc";
		// explicitly passed in so use the sorting direction
		} else
			direction = direct;
		
		fieldName = field;
		
		System.out.println("field "+ field);
		System.out.println("direction "+ direction);
		
		List<Employee> employees = employeeService.findAll(field, direction);	
		theModel.addAttribute("employees", employees);
		
		return "employees/list-employees";
	}
	
	@GetMapping("showAddForm")
	public String showAddForm(Model theModel) {
		
		theModel.addAttribute("employee", new Employee());	
		theModel.addAttribute("fieldName", fieldName);
		theModel.addAttribute("direction", direction);
		return "employees/addEmployee-form";
	}
	
	@PostMapping("processAddForm")
	public String processFormEmployee(@Valid @ModelAttribute("employee") Employee employee,
										BindingResult theBindingResult) {
		
		// result of the validation on firstName
		if (theBindingResult.hasErrors()) {
			List<FieldError> ferror = theBindingResult.getFieldErrors();
			ferror.forEach(e -> logger.info(e.toString()));			
			return "employees/addEmployee-form";
		}
		
		employeeService.save(employee);
		return "redirect:/employees/list?sort="+fieldName+"&direction="+direction;
	}
	
	@GetMapping("showEditForm")
	public String showEditForm(Model theModel, @RequestParam int id) {
		
		// get the employee by id
		Employee employee = employeeService.findById(id);
		theModel.addAttribute("employee", employee);
		theModel.addAttribute("fieldName", fieldName);
		theModel.addAttribute("direction", direction);
		
		return "employees/editEmployee-form";
	}
	
	@PostMapping("processEditForm")
	public String processUpdateForm(@Valid @ModelAttribute("employee") Employee employee,
									BindingResult theBindingResult) {
		
		if (theBindingResult.hasErrors())
			return "employees/editEmployee-form";
		
		employeeService.save(employee);	
		return "redirect:/employees/list?sort="+fieldName+"&direction="+direction;
	}
	
	@GetMapping("delete")
	public String processDelete(@RequestParam int id) {	
		employeeService.deleteById(id);
		
		return String.format("redirect:/employees/list?sort=%s&direction=%s",  fieldName, direction);
		
	}

}
