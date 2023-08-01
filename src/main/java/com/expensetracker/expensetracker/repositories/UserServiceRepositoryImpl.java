package com.expensetracker.expensetracker.repositories;

import java.sql.PreparedStatement;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.expensetracker.expensetracker.domain.User;
import com.expensetracker.expensetracker.exceptions.EtAuthException;

@Repository
public class UserServiceRepositoryImpl implements UserRepository {
    private static final String SQL_CREATE = "INSERT INTO TEST_USERS(USER_ID , FIRST_NAME , LAST_NAME , EMAIL , PASSWORD) VALUES(NEXTVAL('TEST_USERS_SEQ') , ?, ?, ?, ?)";
    private static final String SQL_COUNT_BY_EMAIL = "SELECT COUNT(*) FROM TEST_USERS WHERE EMAIL = ?";
    private static final String SQL_FIND_BY_ID = "SELECT USER_ID , FIRST_NAME , LAST_NAME , EMAIL , PASSWORD FROM TEST_USERS WHERE USER_ID = ?";
    private static final String SQL_FIND_BY_EMAIL = "SELECT USER_ID , FIRST_NAME , LAST_NAME , EMAIL , PASSWORD " +
            "FROM TEST_USERS WHERE EMAIL = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Integer create(String firstName, String lastName, String email, String password) throws EtAuthException {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE,
                        java.sql.Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, email);
                ps.setString(4, hashedPassword);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("USER_ID");
        } catch (Exception e) {
            throw new EtAuthException("Invalid details, Failed to create account");
        }
    }

    @Override
    public com.expensetracker.expensetracker.domain.User findByEmailAndPassword(String email, String password)
            throws EtAuthException {
        try {
            User user = jdbcTemplate.queryForObject(SQL_FIND_BY_EMAIL, userRowMapper, new Object[] { email });
            if (!BCrypt.checkpw(password, user.getPassword()))
                throw new EtAuthException("Invalid email/password");
            return user;
        } catch (EmptyResultDataAccessException e) {
            throw new EtAuthException("Invalid email/password");
        }
    }

    @Override
    public Integer getCountByEmail(String email) {
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL,Integer.class, new Object[] { email });
    }

    @Override
    public com.expensetracker.expensetracker.domain.User findById(Integer userId) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID,userRowMapper, new Object[] { userId });
    }

    RowMapper<User> userRowMapper = ((rs, rowNum) -> {
        return new User(rs.getInt("USER_ID"),
                rs.getString("FIRST_NAME"),
                rs.getString("LAST_NAME"),
                rs.getString("EMAIL"),
                rs.getString("PASSWORD"));
    });

}
