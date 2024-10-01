package fact.it.userservice.controller;

import fact.it.userservice.dto.UserRequest;
import fact.it.userservice.dto.UserResponse;
import fact.it.userservice.exception.ProductAlreadyScannedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import fact.it.userservice.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userservice;
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getUserByEmail(@RequestParam String email) {
        return userservice.getUserByEmail(email);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getAllUsers() { return userservice.getAllUsers();}

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void createUser
            (@RequestBody UserRequest userRequest) {
        userservice.createUser(userRequest);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateUser
            (@PathVariable Long id, @RequestBody UserRequest userRequest) {
        userservice.updateUser(id, userRequest);
    }

    @PostMapping("/{userId}/products/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public void addProductToUser(@PathVariable Long userId, @PathVariable String productId) {
        userservice.addProductToUser(userId, productId);
    }

    @ExceptionHandler(ProductAlreadyScannedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleProductAlreadyScannedException(ProductAlreadyScannedException ex) {
        return ex.getMessage();
    }
}
