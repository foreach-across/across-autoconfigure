package ax.application.controllers;

import ax.application.business.Employee;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * @author Gunther Van Geetsom
 * @since 1.0.2
 */
@RestController
@RequestMapping("/employees")
@ExposesResourceFor(Employee.class)
public class EmployeeController
{

	@GetMapping("/{name}")
	public Employee employee( @PathVariable String name ) {
		Employee employee = new Employee( name, (int) ( Math.random() * 100 ) );
		employee.add( linkTo( methodOn( EmployeeController.class ).employee( name ) ).withSelfRel() );
		return employee;
	}

	@GetMapping
	public List<Employee> employeesOfCompany( @RequestParam String companyName ) {
		List<Employee> employees = new ArrayList<>();
		employees.add( new Employee( "Jean" ) );
		employees.add( new Employee( "Jozef" ) );
		return employees;
	}
}
