
package com.example.cardealer.users.control;

import com.example.cardealer.customers.boundary.CustomerRepository;
import com.example.cardealer.users.boundary.*;
import com.example.cardealer.users.entity.User;
import com.example.cardealer.utils.enums.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<User> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User findUser(Long id) {
        return userRepository.findById(id).get();
    }


    public String addNewUser(CreateUserRequest request) {
        if (emailExists(request.getEmail())) {
            return "Podany adres email " + request.getEmail() + " istnieje w systemie";
        } else {
            User user = new User();
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setActive(true);
            user.addRole(roleRepository.findByName("CLIENT"));
            user.setUserType(UserType.CLIENT);
            userRepository.save(user);
            return "Dodano użytkonika " + request.getFirstName() + " " + request.getLastName();
        }
    }

    public void updateUserPassword(UpdateUserPasswordRequest request) {
        userRepository.findById(request.getId()).ifPresent(
                u -> {
                    u.setPassword(passwordEncoder.encode(request.getPassword()));
                    userRepository.save(u);
                }
        );
        //TODO
        /*send message about change password (requset.getPassword()) on request.getEmail() */
    }
    public void deleteUser(Long id) {
        userRepository.findById(id).ifPresent(u -> userRepository.deleteById(u.getId()));
    }

    public void updateUserInformation(UpdateUserRequest request) {
        userRepository.findById(request.getId()).ifPresent(
                u -> {
                    u.setFirstName(request.getFirstName());
                    u.setLastName(request.getLastName());
                    u.setEmail(request.getEmail());
                    u.setPhoneNumber(request.getPhoneNumber());
                }
        );
    }

    /*Registration*/

    public void registrationNewAppUser(CreateUserRequest request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setActive(true);
        user.setRoles(Arrays.asList(roleRepository.findByName("CLIENT")));
        userRepository.save(user);
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
