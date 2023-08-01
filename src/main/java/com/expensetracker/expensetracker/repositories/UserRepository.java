package com.expensetracker.expensetracker.repositories;

import com.expensetracker.expensetracker.domain.User;
import com.expensetracker.expensetracker.exceptions.EtAuthException;

public interface UserRepository {
    Integer create(String firstName, String lastName , String email, String password) throws EtAuthException;

    User findByEmailAndPassword(String email , String password) throws EtAuthException;

    Integer getCountByEmail(String email);

    User findById(Integer userId);
}
