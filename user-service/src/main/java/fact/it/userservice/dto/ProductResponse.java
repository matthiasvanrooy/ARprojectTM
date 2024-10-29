package fact.it.userservice.dto;

import fact.it.userservice.model.Category;
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
    private String skuCode;
    private String name;
    private Category category;
    private BigDecimal price;
}