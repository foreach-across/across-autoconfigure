package ax.application.business;

import org.springframework.hateoas.ResourceSupport;

import java.util.List;

public class Company extends ResourceSupport
{

	private String name;
	private List<Employee> employees;

	public Company( String name, List<Employee> employees ) {
		this.name = name;
		this.employees = employees;
	}

	public String getName() {
		return name;
	}

	public List<Employee> getEmployees() {
		return employees;
	}
}
