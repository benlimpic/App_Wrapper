
package com.authentication.demo.Repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authentication.demo.Model.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByUsername(String username);
    Optional<UserModel> findByEmail(String email);
}