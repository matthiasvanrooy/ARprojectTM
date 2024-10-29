package fact.it.orderservice;

import fact.it.orderservice.dto.*;
import fact.it.orderservice.exception.NoMoreStockException;
import fact.it.orderservice.model.OrderLineItem;
import fact.it.orderservice.repository.OrderRepository;
import fact.it.orderservice.service.OrderService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import fact.it.orderservice.model.Order;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceApplicationTests {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    private MockWebServer mockProductService;
    private MockWebServer mockInventoryService;
    private WebClient webClient;

    @BeforeEach
    void setUp() throws IOException {
        mockProductService = new MockWebServer();
        mockInventoryService = new MockWebServer();
        mockProductService.start(8081);
        mockInventoryService.start(8082);

        webClient = WebClient.builder().build();
        orderService = new OrderService(orderRepository, webClient);

        // Gebruik .replace("http://", "") om de URL te verkrijgen zonder "http://" er nog eens voor
        ReflectionTestUtils.setField(orderService, "productServiceBaseUrl", mockProductService.url("/").toString().replace("http://", ""));
        ReflectionTestUtils.setField(orderService, "inventoryServiceBaseUrl", mockInventoryService.url("/").toString().replace("http://", ""));
    }

    @AfterEach
    void tearDown() throws IOException {
        mockProductService.shutdown();
        mockInventoryService.shutdown();
    }

    @Test
    public void testPlaceOrder_Success() throws Exception {
        // Arrange
        String skuCode = "sku1";
        Integer quantity = 2;
        BigDecimal price = BigDecimal.valueOf(100);

        OrderRequest orderRequest = new OrderRequest();
        OrderLineItemDto orderLineItemDto = new OrderLineItemDto();
        orderLineItemDto.setSkuCode(skuCode);
        orderLineItemDto.setQuantity(quantity);
        orderRequest.setOrderLineItemsDtoList(Arrays.asList(orderLineItemDto));

        Order order = new Order();
        order.setOrderNumber("1");
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSkuCode(skuCode);
        orderLineItem.setQuantity(quantity);
        orderLineItem.setPrice(price);
        order.setOrderLineItemsList(Arrays.asList(orderLineItem));

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Mocking external service responses
        mockInventoryService.enqueue(new MockResponse()
                .setBody("[{\"skuCode\":\"sku1\",\"inStock\":true,\"quantity\":10}]")
                .addHeader("Content-Type", "application/json"));

        mockProductService.enqueue(new MockResponse()
                .setBody("[{\"id\":\"1\",\"skuCode\":\"sku1\",\"name\":\"Test Name\",\"description\":\"Test Description\",\"price\":100}]")
                .addHeader("Content-Type", "application/json"));

        // Mocking the update stock request
        mockInventoryService.enqueue(new MockResponse()
                .setResponseCode(200));

        // Act
        boolean result = orderService.placeOrder(orderRequest);

        // Assert
        assertTrue(result);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void testPlaceOrder_FailureIfOutOfStock() throws Exception {
        // Arrange
        String skuCode = "sku1";
        Integer quantity = 2;

        OrderRequest orderRequest = new OrderRequest();
        OrderLineItemDto orderLineItemDto = new OrderLineItemDto();
        orderLineItemDto.setSkuCode(skuCode);
        orderLineItemDto.setQuantity(quantity);
        orderRequest.setOrderLineItemsDtoList(Arrays.asList(orderLineItemDto));

        // Mocking external service responses (Out of stock case)
        mockInventoryService.enqueue(new MockResponse()
                .setBody("[{\"skuCode\":\"sku1\",\"inStock\":false,\"quantity\":1}]")
                .addHeader("Content-Type", "application/json"));

        mockProductService.enqueue(new MockResponse()
                .setBody("[{\"id\":\"1\",\"skuCode\":\"sku1\",\"name\":\"Test Name\",\"description\":\"Test Description\",\"price\":100}]")
                .addHeader("Content-Type", "application/json"));

        // Act & Assert
        assertThrows(NoMoreStockException.class, () -> {
            orderService.placeOrder(orderRequest);
        });

        verify(orderRepository, times(0)).save(any(Order.class));
    }

    @Test
    public void testGetAllOrders() {
        // Arrange
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, "sku1", new BigDecimal("10.00"), 2);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, "sku2", new BigDecimal("20.00"), 3);

        Order order1 = new Order(1L, "order1", Arrays.asList(orderLineItem1, orderLineItem2));

        OrderLineItem orderLineItem3 = new OrderLineItem(3L, "sku3", new BigDecimal("30.00"), 4);
        OrderLineItem orderLineItem4 = new OrderLineItem(4L, "sku4", new BigDecimal("40.00"), 5);

        Order order2 = new Order(2L, "order2", Arrays.asList(orderLineItem3, orderLineItem4));

        when(orderRepository.findAll()).thenReturn(Arrays.asList(order1, order2));

        // Act
        List<OrderResponse> result = orderService.getAllOrders();

        // Assert
        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findAll();
    }
}