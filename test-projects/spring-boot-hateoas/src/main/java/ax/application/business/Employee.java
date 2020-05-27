package ax.application.business;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

/**
 * @author Gunther Van Geetsom
 * @since 1.0.2
 */
@Relation(value = "employee", collectionRelation = "employees")
public class Employee extends RepresentationModel
{
	private String name;
	private int age;

	public Employee( String name ) {
		this.name = name;
	}

	public Employee( String name, int age ) {
		this.name = name;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}
}
