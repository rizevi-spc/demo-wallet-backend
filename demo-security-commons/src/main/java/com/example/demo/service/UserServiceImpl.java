package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @Override
    public Long saveCustomerUser(UserDto userDto) {
        final User user = userMapper.toEntity(userDto);
        user.setPassword(encoder.encode(user.getPassword()));
        roleRepository.findRoleByName("CUSTOMER")
                .map(Collections::singletonList)
                .ifPresent(user::setRoles);
        userRepository.save(user);
        return user.getId();
    }
}
