package com.expensetracker.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expensetracker.expensetracker.domain.Transaction;
import com.expensetracker.expensetracker.exceptions.EtBadRequestException;
import com.expensetracker.expensetracker.exceptions.EtResourceNotFoundException;
import com.expensetracker.expensetracker.repositories.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public List<Transaction> fetchAllTransactions(Integer userId, Integer categoryId) {
        return transactionRepository.findAll(userId, categoryId);
    }

    @Override
    public Transaction fetchTransactionById(Integer userId, Integer categoryId, Integer transactionId) {
        return transactionRepository.findById(userId, categoryId, transactionId);
    }

    @Override
    public Transaction addTransaction(Integer userId, Integer categoryId, Double amount, String note,
            Long transactionDate) throws EtBadRequestException {
        Integer transationId = transactionRepository.create(userId, categoryId, amount, note, transactionDate);
        return transactionRepository.findById(categoryId, userId, transationId);
    }

    @Override
    public void updateTransaction(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction)
            throws EtBadRequestException {
        transactionRepository.update(userId, categoryId, transactionId, transaction);
    }

    @Override
    public void removeTransaction(Integer userId, Integer categoryId, Integer transactionId)
            throws EtResourceNotFoundException {
        transactionRepository.removeById(userId, categoryId, transactionId);
    }

}
