package fact.it.inventoryservice.controller;

import fact.it.inventoryservice.dto.InventoryResponse;
import fact.it.inventoryservice.dto.UpdateStockRequest;
import fact.it.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock
    (@RequestParam List<String> skuCode) {
        return inventoryService.isInStock(skuCode);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateStock(@RequestBody UpdateStockRequest updateStockRequest) {
        inventoryService.updateStock(updateStockRequest);
    }
}