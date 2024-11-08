package fact.it.productservice;

import fact.it.productservice.dto.ProductResponse;
import fact.it.productservice.model.Category;
import fact.it.productservice.model.Product;
import fact.it.productservice.repository.ProductRepository;
import fact.it.productservice.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceApplicationTests {

    private Product product1;

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        product1 = new Product();
        product1.setSkuCode("SKU123");
        product1.setName("Test Product 1");
        product1.setDescription("Test Description 1");
        product1.setPrice(BigDecimal.valueOf(100));
        product1.setCategory(Category.valueOf("VEGETABLE"));
    }

    @AfterEach
    public void tearDown() {
        reset(productRepository);
    }

    @Test
    public void testGetAllProducts() {
        // Arrange
        Product product2 = new Product();
        product2.setSkuCode("SKU124");
        product2.setName("Test Product 2");
        product2.setDescription("Test Description 2");
        product2.setPrice(BigDecimal.valueOf(200));
        product2.setCategory(Category.valueOf("FRUIT"));

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        // Act
        List<ProductResponse> products = productService.getAllProducts();

        // Assert
        assertEquals(2, products.size());
        assertEquals("SKU123", products.get(0).getSkuCode());
        assertEquals("SKU124", products.get(1).getSkuCode());
        assertEquals("Test Product 1", products.get(0).getName());
        assertEquals("Test Product 2", products.get(1).getName());
        assertEquals("Test Description 1", products.get(0).getDescription());
        assertEquals("Test Description 2", products.get(1).getDescription());
        assertEquals(BigDecimal.valueOf(100), products.get(0).getPrice());
        assertEquals(BigDecimal.valueOf(200), products.get(1).getPrice());
        assertEquals(Category.valueOf("VEGETABLE"), products.get(0).getCategory());
        assertEquals(Category.valueOf("FRUIT"), products.get(1).getCategory());

//        Ga na dat .findAll() maar 1 keer aangeroepen is
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testGetProductBySkuCode() {
        // Arrange
        when(productRepository.findBySkuCodeIn(Arrays.asList("SKU123"))).thenReturn(Arrays.asList(product1));

        // Act
        List<ProductResponse> products = productService.getProductBySkuCode(Arrays.asList("SKU123"));

        // Assert
        assertEquals(1, products.size());
        assertEquals("SKU123", products.get(0).getSkuCode());
        assertEquals("Test Product 1", products.get(0).getName());
        assertEquals("Test Description 1", products.get(0).getDescription());
        assertEquals(BigDecimal.valueOf(100), products.get(0).getPrice());
        assertEquals(Category.valueOf("VEGETABLE"), products.get(0).getCategory());

        verify(productRepository, times(1)).findBySkuCodeIn(Arrays.asList(product1.getSkuCode()));
    }
}