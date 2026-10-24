package br.com.granaclara.infrastructure.web;

import br.com.granaclara.application.dto.CreateTransactionRequest;
import br.com.granaclara.application.usecase.CreateTransactionUseCase;
import br.com.granaclara.application.usecase.GetAllTransactionsUseCase;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import br.com.granaclara.domain.model.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import br.com.granaclara.application.usecase.GetTransactionByIdUseCase;
import org.springframework.web.bind.annotation.PathVariable;
import br.com.granaclara.application.usecase.DeleteTransactionUseCase;
import org.springframework.web.bind.annotation.DeleteMapping;
import br.com.granaclara.domain.model.Transaction;

@RestController
public class TransactionController {

    private final CreateTransactionUseCase createTransactionUseCase;
    private final GetAllTransactionsUseCase getAllTransactionsUseCase;
    private final GetTransactionByIdUseCase getTransactionByIdUseCase;
    private final DeleteTransactionUseCase deleteTransactionUseCase;

    public TransactionController(CreateTransactionUseCase createTransactionUseCase,
                                 GetAllTransactionsUseCase getAllTransactionsUseCase,
                                 GetTransactionByIdUseCase getTransactionByIdUseCase,
                                 DeleteTransactionUseCase deleteTransactionUseCase) {
        this.createTransactionUseCase = createTransactionUseCase;
        this.getAllTransactionsUseCase = getAllTransactionsUseCase;
        this.getTransactionByIdUseCase = getTransactionByIdUseCase;
        this.deleteTransactionUseCase = deleteTransactionUseCase;
    }

    @PostMapping("/transactions")
    public ResponseEntity<Transaction> createTransaction(
            @RequestBody CreateTransactionRequest request) {

        Transaction transaction = createTransactionUseCase.create(request);

        return ResponseEntity.status(201).body(transaction);
    }

    @GetMapping("/transactions")
    public List<Transaction> getAllTransactions() {
        return getAllTransactionsUseCase.execute();
    }


    @GetMapping("/transactions/{id}")
    public ResponseEntity<Transaction> getTransactionById(
            @PathVariable Long id) {

        return getTransactionByIdUseCase.execute(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable Long id) {

        try {
            deleteTransactionUseCase.execute(id);
            return ResponseEntity.noContent().build();
        } catch (java.util.NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
