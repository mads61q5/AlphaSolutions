package org.example.alphasolutions.repository;

import org.example.alphasolutions.Interfaces.UserRepository;
import org.example.alphasolutions.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }
    @Override
    public User findByUserName(String userName) {
        String sql = "SELECT * FROM users WHERE user_name = ?";
        return jdbcTemplate.queryForObject(sql, new UserRowMapper(), userName);
    }

    @Override
    public User findByID(int userID) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, new UserRowMapper(), userID);
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (user_name, user_password, user_role) " +
                "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                user.getUserName(),
                user.getUserPassword(),
                user.getUserRole());
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET user_name = ?, user_password = ?, " +
                "user_role = ?, is_active = ? WHERE user_id = ?";
        jdbcTemplate.update(sql,
                user.getUserName(),
                user.getUserPassword(),
                user.getUserRole(),
                user.getUserID());
    }

    @Override
    public void delete(int userID) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(sql, userID);
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(
                    rs.getInt("user_id"),
                    rs.getString("user_name"),
                    rs.getString("user_password"),
                    rs.getString("user_role")
            );
        }
    }
}
