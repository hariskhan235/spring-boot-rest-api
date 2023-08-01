package com.expensetracker.expensetracker.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expensetracker.expensetracker.domain.Transaction;
import com.expensetracker.services.TransactionService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/categories/{category_id}/transactions")
public class TransactionResource {

    @Autowired
    TransactionService transactionService;

    @GetMapping("")
    public ResponseEntity<List<Transaction>> fetchAllTransactions(HttpServletRequest request,
            @PathVariable("category_id") Integer categoryId) {
        int userId = (Integer) request.getAttribute("user_id");

        List<Transaction> transactions = transactionService.fetchAllTransactions(userId, categoryId);

        return new ResponseEntity<List<Transaction>>(transactions, HttpStatus.OK);
    }

    @GetMapping("{transation_id}")
    public ResponseEntity<Transaction> getTransactionById(
            HttpServletRequest request, @PathVariable("category_id") Integer categoryId,
            @PathVariable("transation_id") Integer transactionId) {
        System.out.println("Request " + request);
        int userId = (Integer) request.getAttribute("user_id");

        Transaction transaction = transactionService.fetchTransactionById(userId, categoryId, transactionId);

        return new ResponseEntity<>(transaction, HttpStatus.OK);

    }

    @PostMapping("")
    public ResponseEntity<Transaction> addTransaction(
            HttpServletRequest request,
            @PathVariable("category_id") Integer categoryId,
            @RequestBody Map<String, Object> transactionMap) {
        System.out.println("Category Id" + categoryId);
        int usedId = (Integer) request.getAttribute("user_id");
        System.out.println("User Id" + usedId);

        System.out.println("Transaction " + transactionMap);

        Double amount = Double.valueOf(transactionMap.get("amount").toString());

        String note = (String) transactionMap.get("note");

        Long transactionDate = (Long) transactionMap.get("transactionDate");

        Transaction transaction = transactionService.addTransaction(usedId, categoryId, amount, note,
                transactionDate);

        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @PutMapping("/{transation_id}")
    public ResponseEntity<Map<String, Boolean>> updateTransaction(HttpServletRequest request,
            @PathVariable("category_id") Integer categoryId, @PathVariable("transation_id") Integer transactionId,
            @RequestBody Transaction transactionMap) {

        int userId = (Integer) request.getAttribute("user_id");

        transactionService.updateTransaction(userId, categoryId, transactionId, transactionMap);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<Map<String, Boolean>>(map, HttpStatus.OK);
    }

    @DeleteMapping("/{transation_id}")

    public ResponseEntity<Map<String,Boolean>> deleteTransaction(
        HttpServletRequest request , @PathVariable("category_id") Integer categoryId,
        @PathVariable("transation_id") Integer trnsactionId
    ){
        int userId = (Integer) request.getAttribute("user_id");
        transactionService.removeTransaction(userId, categoryId, trnsactionId);

        Map<String,Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<Map<String,Boolean>>(map, HttpStatus.OK);
    }
}
