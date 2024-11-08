package fact.it.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import fact.it.orderservice.model.Category;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private String skuCode;
    private String name;
    private String description;
    private Category category;
    private BigDecimal price;
}