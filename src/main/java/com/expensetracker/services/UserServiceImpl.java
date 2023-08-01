package com.expensetracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expensetracker.expensetracker.domain.User;
import com.expensetracker.expensetracker.exceptions.EtAuthException;
import com.expensetracker.expensetracker.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User validateUser(String email, String password) throws EtAuthException {

        if(email != null) email = email.toLowerCase();

        return userRepository.findByEmailAndPassword(email, password);
    
    }

    @Override
    public User registerUser(String firstName, String lastName, String email, String password) throws EtAuthException {
        // Pattern pattern = Pattern.compile("^(.+)@(.+)$");

        // if (email != null)
        //     email = email.toLowerCase();
        // if (!pattern.matcher(email).matches())
        //     throw new EtAuthException("Invalid Email Format");
        Integer count = userRepository.getCountByEmail(email);
        if (count > 0)
            throw new EtAuthException("Email already in use");
        Integer userId = userRepository.create(firstName, lastName, email, password);
        return userRepository.findById(userId);
    }

}
