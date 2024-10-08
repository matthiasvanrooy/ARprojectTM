package fact.it.productservice.controller;

import fact.it.productservice.dto.ProductResponse;
import fact.it.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

//    @PostMapping
//    @ResponseStatus(HttpStatus.OK)
//    public void createProduct
//            (@RequestBody ProductRequest productRequest) {
//        productService.createProduct(productRequest);
//    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getProductBySkuCode
            (@RequestParam List<String> skuCode) {
        return productService.getProductBySkuCode(skuCode);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

//    @GetMapping("/{productId}")
//    @ResponseStatus(HttpStatus.OK)
//    public ProductResponse getProductById(@PathVariable String productId) {
//        return productService.getProductById(productId);
//    }

}

