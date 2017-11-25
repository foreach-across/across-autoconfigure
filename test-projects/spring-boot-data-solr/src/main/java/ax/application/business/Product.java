package ax.application.business;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Date;

@Data
@SolrDocument(solrCoreName = "techproducts")
public class Product {
    @Id
    @Indexed
    private String id;

    @Field
    private String name;

    @Field
    private boolean inStock;

    @Field
    private String manu;

    @Field("manu_id_s")
    private String manufacturerId;

    @Field
    private Date manufacturedate_dt;

    @Field
    private float price;

    @Field
    private int popularity;
}
