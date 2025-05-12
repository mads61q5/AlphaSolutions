package org.example.alphasolutions.Interfaces;
import org.example.alphasolutions.model.User;
import java.util.List;

public interface UserRepository {
    List<User> findAll();
    User findByID(int userID);
    User findByUserName(String userName);
    void save(User user);
    void update(User user);
    void delete(int userID);
}
