package ax.application.business;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class Company extends RepresentationModel<Company>
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
