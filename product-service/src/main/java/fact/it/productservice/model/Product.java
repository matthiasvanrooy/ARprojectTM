package fact.it.productservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(value = "product")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Product {
    private String id;
    private String skuCode;
    private String name;
    private BigDecimal price;
    private Category category;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
}
