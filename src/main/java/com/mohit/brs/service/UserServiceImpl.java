package com.mohit.brs.service;


import com.mohit.brs.model.user.Role;
import com.mohit.brs.model.user.User;
import com.mohit.brs.model.request.UserRegistrationDto;
import com.mohit.brs.repository.RoleRepository;
import com.mohit.brs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;


    @Autowired
    private RoleRepository roleRepository;


    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;

    }


    @Override
    public void registerUser(UserRegistrationDto userRegistrationDto) {


        Optional<Role> userRole = roleRepository.findById(userRegistrationDto.getRole().getId());

        User user = User.builder()
                .email(userRegistrationDto.getEmail())
                .firstName(userRegistrationDto.getFirstName())
                .lastName(userRegistrationDto.getLastName())
                .phoneNumber(userRegistrationDto.getPhoneNumber())
                .build();

        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));

        if (userRole.isEmpty()) {
            throw new RuntimeException();
        } else {
//            user.setRole(Set.of(userRole.get()));
            user.setRole(userRole.get());
        }

        userRepository.saveAndFlush(user);

    }

    @Override
    public void loginUser(String email, String password) {


    }

    @Override
    public boolean isUserExists(String email) {

        return userRepository.existsByEmail(email);

    }

    @Override
    public void saveUser(User user) {
        userRepository.saveAndFlush(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

//        @may-to-may
//        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRole()) );

//        many-to-one
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(mapRoleToAuthority(user.getRole()))
        );


    }

    @Override
    public User findByEmail(String username) {
        return userRepository.findByEmail(username);
    }

//  @many-to-many
//    public Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles){
//        return roles.stream().map(role -> new SimpleGrantedAuthority(role.toString())).collect(Collectors.toList());
//
//        return roles.getRole().name();
//    }

    /*
    Method Signature: The method returns a GrantedAuthority object and takes a Role object as an argument.
    Purpose: This method is responsible for mapping a Role object to a GrantedAuthority object. In Spring Security, GrantedAuthority represents a granted authority or role that a user possesses.
    Role to Authority Mapping:
    It extracts the role name from the Role object using role.getRole().name().
    This assumes that the Role object has an Enum field representing the role name.
    It creates a SimpleGrantedAuthority object using the role name extracted from the Role object.
    This SimpleGrantedAuthority constructor expects a string representing the authority name, which in this case is the role name.
    Return Value: The method returns a GrantedAuthority object, which represents the authority/role of a user.
    This object will be used in Spring Security's user authentication and authorization process.
    In summary, this method converts a Role object into a GrantedAuthority object, which is a fundamental
    component of Spring Security's authentication and authorization mechanisms. It enables seamless integration
    of application-specific roles with Spring Security's role-based access control.
     */
//    many-to-one
    private GrantedAuthority mapRoleToAuthority(Role role) {
        return new SimpleGrantedAuthority(role.getRole().name());
    }
}
