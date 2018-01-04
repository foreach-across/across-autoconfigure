package ax.application.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * @author Steven Gentens
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto
{
	private long id;
	@ApiModelProperty(notes = "The name of the item", required = true)
	private String name;
	private Integer amount;
	private List<ItemDto> relatedItems;
	private boolean deleted;
	@ApiModelProperty(notes = "The date the item was created", readOnly = true, example = "2017-12-19T13:39:14.007Z")
	private Date created;
}
