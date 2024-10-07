package fact.it.userservice.service;

import fact.it.userservice.dto.ProductResponse;
import fact.it.userservice.dto.UserRequest;
import fact.it.userservice.dto.UserResponse;
import fact.it.userservice.exception.ProductAlreadyScannedException;
import fact.it.userservice.model.User;
import fact.it.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final WebClient.Builder webClientBuilder;

    @Value("${productservice.baseurl}")
    private String productServiceBaseUrl;

        public void createUser(UserRequest userRequest){
        User user = User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();

        userRepository.save(user);
    }

    public List<UserResponse> getUserByEmail(String email) {
        List<User> users = userRepository.findByEmail(email);

        return users.stream().map(this::mapToUserResponse).toList();
    }


    public List<UserResponse> getAllUsers() {
            List<User> users = userRepository.findAll();
            return users.stream()
                    .map(this::mapToUserResponse)
                    .toList();
    }

    //Update vervangt nu enkel de ingevulde velden. Indien een veld wordt leeggelaten, zal dit niets overschrijven? Is dit wel nuttig? Denk het eigenlijk niet.
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id).orElseThrow();
        if (userRequest.getName() != null) {
            user.setName(userRequest.getName());
        }
        if (userRequest.getEmail() != null) {
            user.setEmail(userRequest.getEmail());
        }
        if (userRequest.getPassword() != null) {
            user.setPassword(userRequest.getPassword());
        }

        userRepository.save(user);

        return mapToUserResponse(user);
    }

    public List<ProductResponse> getUserProducts(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        List<String> productIds = user.getProductIds();

        return productIds.stream()
                .map(this::getProductById)
                .collect(Collectors.toList());
    }

    public void addProductToUser(Long userId, String productId) {
        User user = userRepository.findById(userId).orElseThrow();
        if (user.getProductIds().contains(productId)) {
            throw new ProductAlreadyScannedException("You have already scanned this product!");
        }
        user.getProductIds().add(productId);
        userRepository.save(user);
    }

    private ProductResponse getProductById(String productId) {
        return webClientBuilder.build()
                .get()
                .uri("http://"+ productServiceBaseUrl + "/api/product/" + productId)
                .retrieve()
                .bodyToMono(ProductResponse.class)
                .block();
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                //.password(user.getPassword())
                .build();
    }

}