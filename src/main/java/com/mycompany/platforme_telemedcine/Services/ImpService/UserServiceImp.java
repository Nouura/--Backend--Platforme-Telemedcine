package com.mycompany.platforme_telemedcine.Services.ImpService;

import com.mycompany.platforme_telemedcine.Models.User;
import com.mycompany.platforme_telemedcine.Models.UserRole;
import com.mycompany.platforme_telemedcine.Repository.UserRepository;
import com.mycompany.platforme_telemedcine.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public void deleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
