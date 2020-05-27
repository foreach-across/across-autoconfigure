package ax.application.controllers;

import ax.application.business.Company;
import ax.application.business.Employee;
import ax.modules.custom.book.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@ExposesResourceFor(Company.class)
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController
{
	private final EntityLinks entityLinks;
	private final LinkRelationProvider relProvider;

	@GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
	public Company company( @PathVariable(value = "id", required = false) String id ) {
		List<Employee> employees = new ArrayList<>();
		employees.add( new Employee( "Jean" ) );
		employees.add( new Employee( "Jozef" ) );

		Company company = new Company( "Google", employees );
		company.add( linkTo( methodOn( CompanyController.class ).company( id ) ).withSelfRel() );
		company.add( entityLinks.linkToCollectionResource( Employee.class ).withRel( relProvider.getCollectionResourceRelFor( Employee.class ) ) );
		for ( Employee e : employees ) {
			company.add( entityLinks.linkToItemResource( Employee.class, e.getName() ).withRel( relProvider.getItemResourceRelFor( Employee.class ) ) );
		}
		company.add( entityLinks.linkToItemResource( Book.class, "someBook" ).withRel( relProvider.getItemResourceRelFor( Book.class ) ) );

		return company;
	}
}
