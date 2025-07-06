package com.example.demo.config;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitComponent {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    /**
     * to add a user and roles after init
     */
    @EventListener
    public void afterInit(ApplicationReadyEvent event) {
        Role admin = new Role();
        admin.setName("EMPLOYEE");
        Role customer = new Role();
        customer.setName("CUSTOMER");
        roleRepository.save(admin);
        roleRepository.save(customer);
        User user = new User();
        user.setUsername("user");
        user.setPassword(encoder.encode("1234"));
        user.setRoles(List.of(admin, customer));

        userRepository.save(user);
    }
}
