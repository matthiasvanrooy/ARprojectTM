package fact.it.inventoryservice;

import fact.it.inventoryservice.dto.InventoryResponse;
import fact.it.inventoryservice.model.StockItem;
import fact.it.inventoryservice.repository.InventoryRepository;
import fact.it.inventoryservice.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceApplicationTests {

    @InjectMocks
    private InventoryService inventoryService;

    @Mock
    private InventoryRepository inventoryRepository;

    @Test
    public void testIsInStock() {
        // Arrange
        List<String> skuCodes = Arrays.asList("sku1", "sku2");
        StockItem stockItem1 = new StockItem();
        stockItem1.setSkuCode("sku1");
        stockItem1.setQuantity(10);

        StockItem stockItem2 = new StockItem();
        stockItem2.setSkuCode("sku2");
        stockItem2.setQuantity(0);

        when(inventoryRepository.findBySkuCodeIn(skuCodes)).thenReturn(Arrays.asList(stockItem1, stockItem2));

        // Act
        List<InventoryResponse> inventoryResponses = inventoryService.isInStock(skuCodes);

        // Assert
        assertEquals(2, inventoryResponses.size());
        assertEquals("sku1", inventoryResponses.get(0).getSkuCode());
        assertEquals(true, inventoryResponses.get(0).isInStock());
        assertEquals("sku2", inventoryResponses.get(1).getSkuCode());
        assertEquals(false, inventoryResponses.get(1).isInStock());
    }
}
