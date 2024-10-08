package fact.it.productservice.service;

//import fact.it.productservice.dto.ProductRequest;
import fact.it.productservice.dto.ProductResponse;
import fact.it.productservice.model.Product;
import fact.it.productservice.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static fact.it.productservice.model.Category.FRUIT;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @PostConstruct
    public void loadData() {
        if(productRepository.count() <= 0){
            Product product1 = new Product();
            product1.setSkuCode("APPLE001");
            product1.setName("Granny Smith Apple");
            product1.setCategory(FRUIT);
            product1.setPrice(BigDecimal.valueOf(55));

            Product product2 = new Product();
            product2.setSkuCode("APPLE002");
            product2.setName("Pink Lady Apple");
            product2.setCategory(FRUIT);
            product2.setPrice(BigDecimal.valueOf(100));

            Product product3 = new Product();
            product3.setSkuCode("PEAR001");
            product3.setName("Conference Pear");
            product3.setCategory(FRUIT);
            product3.setPrice(BigDecimal.valueOf(75));

            productRepository.save(product1);
            productRepository.save(product2);
            productRepository.save(product3);
        }
    }

//    public void createProduct(ProductRequest productRequest){
//        Product product = Product.builder()
//                .skuCode(productRequest.getSkuCode())
//                .name(productRequest.getName())
//                .category(productRequest.getCategory())
//                .price(productRequest.getPrice())
//                .build();
//
//        productRepository.save(product);
//    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(this::mapToProductResponse).toList();
    }

//    public ProductResponse getProductById(String productId) {
//        return productRepository.findById(productId)
//                .map(this::mapToProductResponse)
//                .orElse(null);
//    }

    public List<ProductResponse> getProductBySkuCode(List<String> skuCode) {
        List<Product> products = productRepository.findBySkuCodeIn(skuCode);

        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                //.id(product.getId())
                .skuCode(product.getSkuCode())
                .name(product.getName())
                .category(product.getCategory())
                .price(product.getPrice())
                .build();
    }

}
