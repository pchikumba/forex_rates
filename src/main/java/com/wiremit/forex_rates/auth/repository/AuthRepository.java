package com.wiremit.forex_rates.auth.repository;


import com.wiremit.forex_rates.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    User findByEmail(String email);
}
