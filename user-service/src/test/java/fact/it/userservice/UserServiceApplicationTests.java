package fact.it.userservice;

import fact.it.userservice.dto.ProductResponse;
import fact.it.userservice.dto.UserRequest;
import fact.it.userservice.dto.UserResponse;
import fact.it.userservice.exception.ProductAlreadyScannedException;
import fact.it.userservice.model.Category;
import fact.it.userservice.model.User;
import fact.it.userservice.repository.UserRepository;
import fact.it.userservice.service.UserService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.*;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceApplicationTests {

	private User user;
	private UserRequest userRequest;

	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepository userRepository;

	private MockWebServer mockProductService;
	private WebClient webClient;

	@BeforeEach
	void setUp() throws IOException {
		user = new User(1L, "John Doe", "john@example.com", "password123", Arrays.asList("sku1","sku2"));
		userRequest = new UserRequest("Jane Doe", "jane@example.com", "newpassword123");
		mockProductService = new MockWebServer();
		mockProductService.start(8081);

		webClient = WebClient.builder().build();

		userService = new UserService(userRepository, webClient);

		ReflectionTestUtils.setField(userService, "productServiceBaseUrl", mockProductService.url("/").toString().replace("http://", ""));
		// Set the product service base URL to the mock server URL
	}

	@AfterEach
	public void tearDown() throws Exception {
		reset(userRepository);
		mockProductService.shutdown();
	}

	@Test
	public void testCreateUser() {
		// Act
		userService.createUser(userRequest);

		// Assert
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	public void testGetUserById() {
		// Arrange
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		// Act
		UserResponse userResponse = userService.getUserById(1L);

		// Assert
		assertNotNull(userResponse);
		assertEquals("John Doe", userResponse.getName());
		assertEquals("john@example.com", userResponse.getEmail());
		verify(userRepository, times(1)).findById(1L);
	}

	@Test
	public void testGetUserByEmail() {
		// Arrange
		when(userRepository.findByEmail("john@example.com")).thenReturn(user);

		// Act
		UserResponse userResponse = userService.getUserByEmail(user.getEmail());

		// Assert
		assertNotNull(userResponse);
		assertEquals("John Doe", userResponse.getName());
		assertEquals("password123", userResponse.getPassword());
		verify(userRepository, times(1)).findByEmail("john@example.com");
	}

	@Test
	public void testGetAllUsers() {
		// Arrange
		User user2 = new User(2L, "Jane Doe", "jane@example.com", "password456", Arrays.asList("sku3", "sku4"));
		when(userRepository.findAll()).thenReturn(Arrays.asList(user, user2));

		// Act
		List<UserResponse> users = userService.getAllUsers();

		// Assert
		assertNotNull(users);
		assertEquals(2, users.size());
		assertEquals("John Doe", users.get(0).getName());
		assertEquals("Jane Doe", users.get(1).getName());
		verify(userRepository, times(1)).findAll();
	}

	@Test
public void testUpdateUser() {
	// Arrange
	when(userRepository.findById(1L)).thenReturn(Optional.of(user));

	// Act
	UserResponse updatedUser = userService.updateUser(1L, userRequest);

	// Assert
	assertNotNull(updatedUser);
	assertEquals("Jane Doe", updatedUser.getName());
	assertEquals("jane@example.com", updatedUser.getEmail());
	assertEquals("newpassword123", updatedUser.getPassword());
	verify(userRepository, times(1)).findById(1L);
	verify(userRepository, times(1)).save(any(User.class));
}

	@Test
	public void testGetUserProducts() throws Exception {
		// Arrange
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		// Mock the product service response
		ProductResponse productResponse1 = new ProductResponse("sku1", "Product 1", Category.VEGETABLE, BigDecimal.valueOf(100));
		ProductResponse productResponse2 = new ProductResponse("sku2", "Product 2", Category.FRUIT, BigDecimal.valueOf(200));
		mockProductService.enqueue(new MockResponse()
				.setBody("[{\"skuCode\":\"sku1\",\"name\":\"Product 1\",\"category\":\"VEGETABLE\",\"price\":100},{\"skuCode\":\"sku2\",\"name\":\"Product 2\",\"category\":\"FRUIT\",\"price\":200}]")
				.addHeader("Content-Type", "application/json"));

		// Act
		List<ProductResponse> products = userService.getUserProducts(1L);

		// Assert
		assertNotNull(products);
		assertEquals(2, products.size());
		assertEquals("sku1", products.get(0).getSkuCode());
		assertEquals("Product 1", products.get(0).getName());
		verify(userRepository, times(1)).findById(1L);
	}

@Test
public void testAddProductToUser() {
    // Arrange
    User user1 = new User(1L, "John Doe", "john@example.com", "password123", new ArrayList<>());
    when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

    // Mock the product service response
    ProductResponse productResponse = new ProductResponse("sku1", "Product 1", Category.VEGETABLE, BigDecimal.valueOf(100));
    mockProductService.enqueue(new MockResponse()
            .setBody("[{\"skuCode\":\"sku1\",\"name\":\"Product 1\",\"category\":\"VEGETABLE\",\"price\":100}]")
            .addHeader("Content-Type", "application/json"));

    // Act
    userService.addProductToUser(1L, "sku1");
    List<ProductResponse> products = userService.getProductBySkuCode(Collections.singletonList("sku1"));

    // Assert
    assertTrue(user1.getProductSkucodes().contains("sku1"));
    assertNotNull(products);
    assertEquals(1, products.size());
    assertEquals("sku1", products.get(0).getSkuCode());
    assertEquals("Product 1", products.get(0).getName());
    verify(userRepository, times(1)).save(user1);
}

	@Test
	public void testAddProductToUser_AlreadyScanned() {
		// Arrange
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		// Act & Assert
		Exception exception = assertThrows(ProductAlreadyScannedException.class, () -> {
			userService.addProductToUser(1L, "sku1");
		});

		assertEquals("You have already scanned this product!", exception.getMessage());
		verify(userRepository, times(0)).save(any(User.class));
	}

	@Test
	public void testGetProductBySkuCode() {
		// Mock the product service response
		ProductResponse productResponse = new ProductResponse("sku1", "Product 1", Category.VEGETABLE, BigDecimal.valueOf(100));
		mockProductService.enqueue(new MockResponse()
				.setBody("[{\"skuCode\":\"sku1\",\"name\":\"Product 1\",\"category\":\"VEGETABLE\",\"price\":100}]")
				.addHeader("Content-Type", "application/json"));

		// Act
		List<ProductResponse> products = userService.getProductBySkuCode(Arrays.asList("sku1"));

		// Assert
		assertNotNull(products);
		assertEquals(1, products.size());
		assertEquals("sku1", products.get(0).getSkuCode());
		assertEquals("Product 1", products.get(0).getName());
		verify(userRepository, times(0)).findById(1L);
	}
}
