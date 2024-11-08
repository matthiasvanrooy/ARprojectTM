package fact.it.inventoryservice.service;

import fact.it.inventoryservice.dto.InventoryResponse;
import fact.it.inventoryservice.dto.UpdateStockRequest;
import fact.it.inventoryservice.model.StockItem;
import fact.it.inventoryservice.repository.InventoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @PostConstruct
    public void loadData() {
        if(inventoryRepository.count() <= 0){
            StockItem stockItem1 = new StockItem();
            stockItem1.setSkuCode("APP001");
            stockItem1.setQuantity(100);

            StockItem stockItem2 = new StockItem();
            stockItem2.setSkuCode("PEA001");
            stockItem2.setQuantity(50);

            StockItem stockItem3 = new StockItem();
            stockItem3.setSkuCode("CUC001");
            stockItem3.setQuantity(1);

            StockItem stockItem4 = new StockItem();
            stockItem4.setSkuCode("PEP001");
            stockItem4.setQuantity(20);

            StockItem stockItem5 = new StockItem();
            stockItem5.setSkuCode("CAR001");
            stockItem5.setQuantity(5);

            inventoryRepository.save(stockItem1);
            inventoryRepository.save(stockItem2);
            inventoryRepository.save(stockItem3);
            inventoryRepository.save(stockItem4);
            inventoryRepository.save(stockItem5);
        }
    }

    public void updateStock(UpdateStockRequest updateStockRequest) {
        StockItem stockItem = inventoryRepository.findBySkuCode(updateStockRequest.getSkuCode());
        int newQuantity = stockItem.getQuantity() - updateStockRequest.getQuantity();
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be less than zero");
        }
        stockItem.setQuantity(newQuantity);
        inventoryRepository.save(stockItem);
    }

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode) {

        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(stockItem ->
                        InventoryResponse.builder()
                                .skuCode(stockItem.getSkuCode())
                                .isInStock(stockItem.getQuantity() > 0)
                                .quantity(stockItem.getQuantity())
                                .build()
                ).toList();
    }
}
