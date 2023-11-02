package ru.ylab.walletservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ylab.walletservice.dto.TransactionDTO;
import ru.ylab.walletservice.model.Transaction;
import ru.ylab.walletservice.services.TransactionService;
import ru.ylab.walletservice.utils.annotation.TrackEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This controller contains logic for endpoints /transaction/debit, /transaction/credit
 * /history and consumes POST(create credit or debit transaction) and GET(get all users transactions) methods
 */
@RestController
@RequestMapping("/transaction")
@Tag(name = "Transaction", description = "Endpoints for managing transactions")
@RequiredArgsConstructor
public class TransactionsController {
    /**
     * This field contains link to TransactionService
     */
    private final TransactionService service;

    /**
     * Method contains logic for creates a debit transaction
     * @param userId Long
     * @param dto TransactionDTO
     * @return Status code and JSON message
     */
    @TrackEvent(action = "User debit transaction")
    @PostMapping(value = "/debit", consumes = {"application/json"})
    @Operation(summary = "Creates a debit transaction", description = "Creates a new debit transaction " +
            "to the logged in user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "{message: Transaction created}"),
            @ApiResponse(responseCode = "400", description = "{message: Not enough funds in the account}"),
            @ApiResponse(responseCode = "400", description = "{message: }"),
            @ApiResponse(responseCode = "409", description = "{message: No valid transaction id}") })
    public ResponseEntity<Object> debitTransaction(@RequestAttribute long userId, @RequestBody TransactionDTO dto) {
        System.out.println(userId);
        System.out.println(dto);
        Map<Object, Object> model = new HashMap<>();
        String response = service.processDebitTransaction(dto, userId);
       switch (response){
           case "Transaction created" -> {
               model.put("message", "Transaction created");
               return ResponseEntity.status(201).body(model);
           }
           case "Not enough funds in the account" -> {
               model.put("message", "Not enough funds in the account");
               return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(model);
           }
           case "No valid transaction id" -> {
               model.put("message", "No valid transaction id");
               return ResponseEntity.status(409).body(model);
           }
           default -> {
               model.put("message", response);
               return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(model);
           }
       }
    }

    /**
     * Method contains logic for creates a credit transaction
     * @param userId Long
     * @param dto TransactionDTO
     * @return Status code and JSON message
     */
    @TrackEvent(action = "User credit transaction")
    @PostMapping(value = "/credit", consumes = {"application/json"})
    @Operation(summary = "Creates a credit transaction", description = "Creates a new credit transaction " +
            "to the logged in user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "{message: Transaction created}"),
            @ApiResponse(responseCode = "409", description = "{message: No valid transaction id}") ,
            @ApiResponse(responseCode = "400", description = "{message: }")})
    public ResponseEntity<Object> creditTransaction(@RequestAttribute long userId, @RequestBody TransactionDTO dto) {
        Map<Object, Object> model = new HashMap<>();
        String response = service.processCreditTransaction(dto, userId);
        switch (response){
            case "Transaction created" -> {
                model.put("message", "Transaction created");
                return ResponseEntity.status(201).body(model);
            }
            case "No valid transaction id" -> {
                model.put("message", "No valid transaction id");
                return ResponseEntity.status(409).body(model);
            }
            default -> {
                model.put("message", response);
                return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(model);
            }
        }
    }

    /**
     * Method contains logic for return user transaction history
     * @param userId Long
     * @return list if user transaction history
     */
    @GetMapping(value = "/history")
    @Operation(summary = "Returns list of logged in user transactions")
    @ApiResponse(responseCode = "200", description = "{transactions list}")
    public ResponseEntity<List<Transaction>> transactionHistory(@RequestAttribute long userId) {
      return ResponseEntity.ok(service.getAllTransactions(userId));
    }
}
