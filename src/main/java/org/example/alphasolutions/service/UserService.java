package org.example.alphasolutions.service;

import java.util.List;

import org.example.alphasolutions.Interfaces.UserRepository;
import org.example.alphasolutions.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Verificerer om login er rigtigt
    public boolean login(String username, String password) {
        User user = userRepository.findByUserName(username);
        if(user != null) {
            return user.getUserPassword().equals(password);
        }
        return false;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByName(String username) {
        return userRepository.findByUserName(username);
    }

    public User getUserByID(int userID) {
        return userRepository.findByID(userID);
    }

    public void updateUser (User user) {
        userRepository.update(user);
    }

    public void deleteUser (int id) {
        userRepository.delete(id);
    }

    public void saveUser (User user) {
        userRepository.save(user);
    }

}
