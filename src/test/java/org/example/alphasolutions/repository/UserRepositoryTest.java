package org.example.alphasolutions.repository;

import java.util.List;

import org.example.alphasolutions.Interfaces.UserRepository;
import org.example.alphasolutions.model.User;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:h2init.sql")
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findAll_ShouldReturn3Users() {
        List<User> users = userRepository.findAll();
        assertEquals(3, users.size());
    }

    @Test
    void findByUserName_ShouldReturnCorrectUser() {
        User admin = userRepository.findByUserName("admin_user");
        assertNotNull(admin);
        assertEquals("admin_user", admin.getUserName());
        assertEquals("Admin", admin.getUserRole());

        User manager = userRepository.findByUserName("manager_user");
        assertNotNull(manager);
        assertEquals("manager_user", manager.getUserName());
        assertEquals("ProjectManager", manager.getUserRole());

        User dev = userRepository.findByUserName("dev_user");
        assertNotNull(dev);
        assertEquals("dev_user", dev.getUserName());
        assertEquals("Developer", dev.getUserRole());
    }

    @Test
    void findByID_ShouldReturnCorrectUser() {
        User user = userRepository.findByID(1);
        assertNotNull(user);
        assertEquals("admin_user", user.getUserName());
        assertEquals("Admin", user.getUserRole());
    }

    @Test
    void save_ShouldCreateNewUser() {
        User newUser = new User();
        newUser.setUserName("test_user");
        newUser.setUserPassword("testpass");
        newUser.setUserRole("Tester");

        userRepository.save(newUser);

        User savedUser = userRepository.findByUserName("test_user");
        assertNotNull(savedUser);
        assertEquals("test_user", savedUser.getUserName());
        assertEquals("Tester", savedUser.getUserRole());

        List<User> allUsers = userRepository.findAll();
        assertEquals(4, allUsers.size());
    }

    @Test
    void update_ShouldModifyUser() {
        User user = userRepository.findByID(1);
        user.setUserRole("SuperAdmin");
        
        userRepository.update(user);
        
        User updatedUser = userRepository.findByID(1);
        assertEquals("SuperAdmin", updatedUser.getUserRole());
    }

    @Test
    void delete_ShouldRemoveUser() {
        userRepository.delete(3);
        
        try {
            User deletedUser = userRepository.findByID(3);
            assertNull(deletedUser);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Incorrect result size"));
        }
        
        List<User> remainingUsers = userRepository.findAll();
        assertEquals(2, remainingUsers.size());
    }
} 