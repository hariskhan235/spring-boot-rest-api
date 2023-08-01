package com.expensetracker.services;

import com.expensetracker.expensetracker.domain.User;
import com.expensetracker.expensetracker.exceptions.EtAuthException;

public interface UserService {
    
   User validateUser(String email , String password) throws EtAuthException;

   User registerUser(String firstName , String lastName , String email , String password) throws EtAuthException;
}

