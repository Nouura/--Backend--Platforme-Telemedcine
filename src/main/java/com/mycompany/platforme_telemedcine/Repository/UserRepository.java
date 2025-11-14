package com.mycompany.platforme_telemedcine.Repository;

import com.mycompany.platforme_telemedcine.Models.User;
import com.mycompany.platforme_telemedcine.Models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByname(String name);
    User findById(long id);
    User findByEmail(String email);
    User findByRole(UserRole role);
}
