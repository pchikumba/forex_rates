package com.wiremit.forex_rates.auth.service;


import com.wiremit.forex_rates.auth.config.JWTUtil;
import com.wiremit.forex_rates.auth.config.UserInfoConfigManager;
import com.wiremit.forex_rates.auth.config.UserMapper;
import com.wiremit.forex_rates.auth.dto.*;
import com.wiremit.forex_rates.auth.model.User;
import com.wiremit.forex_rates.auth.repository.AuthRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private ModelMapper modelMapper;
    /*@Autowired
    private EmailNotificationService emailNotificationService;*/

    @Autowired
    JWTUtil jwtUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserInfoConfigManager userInfoConfigManager;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public RegisterResponse register(RegisterDTO registerDTO) {
        if (authRepository.findByUsername(registerDTO.getEmail()) != null) {
            throw new IllegalArgumentException("Username is already in use");
        }
        var user =  User.builder()
                .name(registerDTO.getName())
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .username(registerDTO.getEmail())
                .role(registerDTO.getRole())
                .createdAt(LocalDateTime.now())
                .build();

        var save = authRepository.save(user);
      /*  var emailNotificationRequest = new EmailNotificationRequest();
        emailNotificationRequest.setTo(registerDTO.getEmail());
        emailNotificationRequest.setSubject("Account creation");
        emailNotificationRequest.setMessage
                ("Your have successfully registered your account.\n \n " +
                        "Thank you " + registerDTO.getName() +
                        " for choosing Smatech as you best E-commerce platform.");
        emailNotificationService.sendEmail(emailNotificationRequest);*/
        return modelMapper.map(save, RegisterResponse.class);
    }

    public Response registerUser(RegisterDTO registerDTO) {
        var response = new Response();
        try {
            if (authRepository.findByUsername(registerDTO.getEmail()) != null) {
                response.setMessage("Username " + registerDTO.getEmail() + " is already in exist");
                response.setStatusCode(409);
                return response;
            }
            var user = new User();
            user.setUsername(registerDTO.getEmail());
            user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
            user.setCreatedAt(LocalDateTime.now());
            user.setName(registerDTO.getName());
            user.setEmail(registerDTO.getEmail());
            user.setRole(registerDTO.getRole());
            User save = authRepository.save(user);
            var userDto = UserMapper.toResponse(save);
            response.setStatusCode(200);
            response.setMessage("Account created successfully");
            response.setData(userDto);
            return response;
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occured while registering your account.");
            return response;
        }
    }

    @Override
    public Response login(LoginCredentials loginCredentials) {
        Response response = new Response();
        if (loginCredentials.getEmail() == null || loginCredentials.getPassword() == null) {
            response.setStatusCode(400);
            response.setMessage("Username or password must be provided");
            return response;
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginCredentials.getEmail(), loginCredentials.getPassword())
            );

            User user = authRepository.findByUsername(loginCredentials.getEmail());
            if (user == null) {
                response.setStatusCode(401);
                response.setMessage("Invalid username or password");
            }
            UserDetails userDetails = userInfoConfigManager.loadUserByUsername(loginCredentials.getEmail());
            assert user != null;
            String jwt = jwtUtil.generateToken(userDetails.getUsername(),user.getRole().name());
            if (jwt == null) {
                response.setStatusCode(401);
                response.setMessage("Invalid username or password");
                return response;
            }

            var userResponse = UserMapper.toResponse(user);
            LoginResponse loginResponse = LoginResponse.builder()
                    .token(jwt)
                    .user(userResponse)
                    .build();
            response.setStatusCode(200);
            response.setData(loginResponse);
            response.setMessage("User logged in successfully");

            return response;
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occured while logging in");
            return response;
        }
    }
}
