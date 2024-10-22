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
    private final WebClient webClient;

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

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return mapToUserResponse(user);
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);

        return mapToUserResponse(user);
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

    public List<ProductResponse> getUserProducts(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        List<String> productIds = user.getProductSkucodes();

        return getProductBySkuCode(productIds);
    }

    public void addProductToUser(Long id, String skuCode) {
        User user = userRepository.findById(id).orElseThrow();
        if (user.getProductSkucodes().contains(skuCode)) {
            throw new ProductAlreadyScannedException("You have already scanned this product!");
        }
        user.getProductSkucodes().add(skuCode);
        userRepository.save(user);
    }

//    private List<ProductResponse> getProductBySkuCode(List<String> skuCodes) {
//        ProductResponse[] productResponseArray = webClientBuilder.build()
//                .get()
//                .uri("http://" + productServiceBaseUrl + "/api/product",
//                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes.toArray()).build())
//                .retrieve()
//                .bodyToMono(ProductResponse[].class)
//                .block();
//
//        return List.of(productResponseArray);

    // Change your UserService call to use a list if your product service method is expecting it
    public List<ProductResponse> getProductBySkuCode(List<String> skuCodes) {
        ProductResponse[] productResponseArray = webClient.get()
                .uri("http://" + productServiceBaseUrl + "/api/product",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(ProductResponse[].class)
                .block();

        return List.of(productResponseArray);
    }


//    private ProductResponse getProductById(String productId) {
//        return webClientBuilder.build()
//                .get()
//                .uri("http://"+ productServiceBaseUrl + "/api/product/" + productId)
//                .retrieve()
//                .bodyToMono(ProductResponse.class)
//                .block();
//    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .productSkucodes(user.getProductSkucodes())
                .build();
    }

}