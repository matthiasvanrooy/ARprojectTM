package fact.it.productservice.dto;

import fact.it.productservice.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    //private String id;
    //private String skuCode;
    private String name;
    private Category category;
    private BigDecimal price;
}