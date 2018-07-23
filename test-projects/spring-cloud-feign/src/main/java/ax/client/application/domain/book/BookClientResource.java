package ax.client.application.domain.book;

import lombok.*;

import java.time.LocalDate;

/**
 * @author Gunther Van Geetsom
 * @since 1.0.1
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@Builder
public class BookClientResource
{
	private long id;
	private String name;
	private String author;
	private double price;
	private LocalDate publishedOn;
}
