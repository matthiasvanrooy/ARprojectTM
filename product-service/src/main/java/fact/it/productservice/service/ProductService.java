package fact.it.productservice.service;

//import fact.it.productservice.dto.ProductRequest;
import fact.it.productservice.dto.ProductResponse;
import fact.it.productservice.model.Product;
import fact.it.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

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

    public List<ProductResponse> getProductBySkuCode(List<String> skuCode) {
        List<Product> products = productRepository.findBySkuCodeIn(skuCode);

        return products.stream().map(this::mapToProductResponse).toList();
    }

    public List<ProductResponse> getProductsByUserId(Long userId) {
        List<Product> products = productRepository.findByUserId(userId);
        return products.stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                //.id(product.getId())
                //.skuCode(product.getSkuCode())
                .name(product.getName())
                .category(product.getCategory())
                .price(product.getPrice())
                .build();
    }

}
