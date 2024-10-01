package fact.it.userservice.service;

import fact.it.userservice.dto.UserRequest;
import fact.it.userservice.dto.UserResponse;
import fact.it.userservice.model.User;
import fact.it.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

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

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .name(user.getName())
                //.email(user.getEmail())
                .build();
    }
}