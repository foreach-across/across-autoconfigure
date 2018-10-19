package ax.application.controllers;

import ax.application.business.Company;
import ax.application.business.Employee;
import ax.modules.custom.book.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RelProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@ExposesResourceFor(Company.class)
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController
{
	private final EntityLinks entityLinks;
	private final RelProvider relProvider;

	@GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
	public Company company( @PathVariable(value = "id", required = false) String id ) {
		List<Employee> employees = new ArrayList<>();
		employees.add( new Employee( "Jean" ) );
		employees.add( new Employee( "Jozef" ) );

		Company company = new Company( "Google", employees );
		company.add( linkTo( methodOn( CompanyController.class ).company( id ) ).withSelfRel() );
		company.add( entityLinks.linkToCollectionResource( Employee.class ).withRel( relProvider.getCollectionResourceRelFor( Employee.class ) ) );
		for ( Employee e : employees ) {
			company.add( entityLinks.linkToSingleResource( Employee.class, e.getName() ).withRel( relProvider.getItemResourceRelFor( Employee.class ) ) );
		}
		company.add( entityLinks.linkToSingleResource( Book.class, "someBook" ).withRel( relProvider.getItemResourceRelFor( Book.class ) ) );

		return company;
	}
}
