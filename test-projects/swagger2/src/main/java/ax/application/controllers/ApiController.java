package ax.application.controllers;

import ax.application.dtos.ItemDto;
import io.swagger.annotations.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author Steven Gentens
 */
@RestController
public class ApiController
{
	private final ItemDto ITEM_ONE = ItemDto.builder()
	                                        .id( -1L )
	                                        .name( "Item one" )
	                                        .amount( 1578945612 )
	                                        .created( new Date() )
	                                        .deleted( false )
	                                        .relatedItems( new ArrayList<>() )
	                                        .build();
	private final ItemDto ITEM_TWO = ItemDto.builder()
	                                        .id( -2L )
	                                        .name( "Item two" )
	                                        .amount( 123456789 )
	                                        .created( new Date() )
	                                        .deleted( true )
	                                        .relatedItems( Collections.singletonList( ItemDto.builder()
	                                                                                         .id( -4L )
	                                                                                         .name( "Item four" )
	                                                                                         .amount( 74158642 )
	                                                                                         .created( new Date() )
	                                                                                         .deleted( false )
	                                                                                         .relatedItems( new ArrayList<>() )
	                                                                                         .build() ) )
	                                        .build();
	private final ItemDto ITEM_THREE = ItemDto.builder()
	                                          .id( -3L )
	                                          .name( "Item three" )
	                                          .amount( 987456321 )
	                                          .created( new Date() )
	                                          .deleted( false )
	                                          .relatedItems( Arrays.asList(
			                                          ITEM_ONE, ITEM_TWO
	                                          ) )
	                                          .build();

	@GetMapping("/status")
	public String status() {
		return "ok";
	}

	@ApiOperation(value = "Retrieve a list of items.", responseContainer = "List")
	@GetMapping(path = "/api/items", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "name", value = "User's name", required = false, dataType = "string", paramType = "query", defaultValue = "John")
	})
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = ItemDto.class),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Failure") })
	public ResponseEntity<List<ItemDto>> getItems( @RequestParam(value = "showDeleted", required = false, defaultValue = "false") boolean showDeleted,
	                                               @RequestParam(value = "items", required = false, defaultValue = "5") int items,
	                                               @RequestParam(value = "offset", required = false, defaultValue = "0") int offset ) {
		return ResponseEntity.ok( Arrays.asList( ITEM_ONE, ITEM_TWO, ITEM_THREE ) );
	}

	@ApiOperation(value = "Retrieve a single item.",
			response = ItemDto.class)
	@GetMapping(path = "/api/item/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemDto> getItem( @ApiParam(name = "id", required = true, example = "15") @PathVariable long id ) {
		return ResponseEntity.ok( ITEM_THREE );
	}
}
