package com.ghpark.hotalk.security;

import com.ghpark.hotalk.security.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author peter
 * 유저 리포지토리 인터페이스
 */
public interface UserRepository extends CrudRepository<User, String> {
    public Optional<User> findByEmail(String email);
}
