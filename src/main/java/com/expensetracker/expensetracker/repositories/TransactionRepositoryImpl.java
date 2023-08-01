package com.expensetracker.expensetracker.repositories;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.expensetracker.expensetracker.domain.Transaction;
import com.expensetracker.expensetracker.exceptions.EtBadRequestException;
import com.expensetracker.expensetracker.exceptions.EtResourceNotFoundException;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {


    private static final String SQL_FIND_ALL = "SELECT TRANSATION_ID, CATEGORY_ID, USER_ID, AMOUNT, NOTE, TRANSACTION_DATE FROM TEST_TRANSACTIONS WHERE USER_ID = ? AND CATEGORY_ID = ?";
    private static final String SQL_FIND_BY_ID = "SELECT TRANSATION_ID, CATEGORY_ID, USER_ID, AMOUNT, NOTE, TRANSACTION_DATE FROM TEST_TRANSACTIONS WHERE USER_ID = ? AND CATEGORY_ID = ? AND TRANSATION_ID = ?";
    private static final String SQL_CREATE = "INSERT INTO TEST_TRANSACTIONS (TRANSATION_ID, CATEGORY_ID, USER_ID, AMOUNT, NOTE, TRANSACTION_DATE) VALUES(NEXTVAL('TEST_TRANSACTIONS_SEQ'), ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE TEST_TRANSACTIONS SET AMOUNT = ?, NOTE = ?, TRANSACTION_DATE = ? WHERE USER_ID = ? AND CATEGORY_ID = ? AND TRANSATION_ID = ?";
    private static final String SQL_DELETE = "DELETE FROM TEST_TRANSACTIONS WHERE USER_ID = ? AND CATEGORY_ID = ? AND TRANSATION_ID = ?";


    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Transaction> findAll(Integer userId, Integer categoryId) {
        return jdbcTemplate.query(SQL_FIND_ALL, transactionRowMapper, new Object[] { userId, categoryId });
    }

    @Override
    public Transaction findById(Integer userId, Integer categoryId, Integer transactionId)
            throws EtResourceNotFoundException {
        try {
            System.out.println("User ID"+userId + "Category ID" +categoryId + "Transaction ID"+transactionId);
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, transactionRowMapper,
                    new Object[] { categoryId, userId, transactionId });
        } catch (Exception e) {
            System.out.println("EXCEPTION"+e);
            throw new EtResourceNotFoundException("Transaction not found");
        }
    }

    @Override
    public Integer create(Integer userId, Integer categoryId, Double amount, String note, Long transactionDate)
            throws EtBadRequestException {
        try {
            System.out.println("Category Id"+categoryId);
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, categoryId);
                ps.setInt(2, userId);
                ps.setDouble(3, amount);
                ps.setString(4, note);
                ps.setLong(5, transactionDate);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("TRANSATION_ID");
        } catch (Exception e) {
            System.out.println("Exception "+e);
            throw new EtBadRequestException("Invalid request");
        }
    }

    @Override
    public void update(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction)
            throws EtBadRequestException {
        try {
            jdbcTemplate.update(SQL_UPDATE, new Object[] { transaction.getAmount(), transaction.getNote(),
                    transaction.getTransactionDate(), userId, categoryId, transactionId });
        } catch (Exception e) {
            throw new EtBadRequestException("Invalid request");
        }
    }

    @Override
    public void removeById(Integer userId, Integer categoryId, Integer transactionId)
            throws EtResourceNotFoundException {
        int count = jdbcTemplate.update(SQL_DELETE, new Object[] { userId, categoryId, transactionId });
        if (count == 0)
            throw new EtResourceNotFoundException("Transaction not found");
    }

    private RowMapper<Transaction> transactionRowMapper = ((rs, rowNum) -> {
        return new Transaction(rs.getInt("TRANSATION_ID"),
                rs.getInt("CATEGORY_ID"),
                rs.getInt("USER_ID"),
                rs.getDouble("AMOUNT"),
                rs.getString("NOTE"),
                rs.getLong("TRANSACTION_DATE"));
    });

}