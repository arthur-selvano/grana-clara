package br.com.granaclara.infrastructure.web;

import br.com.granaclara.application.dto.CreateTransactionRequest;
import br.com.granaclara.application.usecase.CreateTransactionUseCase;
import br.com.granaclara.application.usecase.GetAllTransactionsUseCase;
import jakarta.validation.Valid;
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
import br.com.granaclara.application.dto.FinancialSummaryResponse;
import br.com.granaclara.application.usecase.GetFinancialSummaryUseCase;
import br.com.granaclara.domain.model.Transaction;
import br.com.granaclara.application.usecase.UpdateTransactionUseCase;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class TransactionController {

    private final CreateTransactionUseCase createTransactionUseCase;
    private final GetAllTransactionsUseCase getAllTransactionsUseCase;
    private final GetTransactionByIdUseCase getTransactionByIdUseCase;
    private final DeleteTransactionUseCase deleteTransactionUseCase;
    private final GetFinancialSummaryUseCase getFinancialSummaryUseCase;
    private final UpdateTransactionUseCase updateTransactionUseCase;

    public TransactionController(
            CreateTransactionUseCase createTransactionUseCase,
            GetAllTransactionsUseCase getAllTransactionsUseCase,
            GetTransactionByIdUseCase getTransactionByIdUseCase,
            DeleteTransactionUseCase deleteTransactionUseCase,
            GetFinancialSummaryUseCase getFinancialSummaryUseCase,
            UpdateTransactionUseCase updateTransactionUseCase
    ) {
        this.createTransactionUseCase = createTransactionUseCase;
        this.getAllTransactionsUseCase = getAllTransactionsUseCase;
        this.getTransactionByIdUseCase = getTransactionByIdUseCase;
        this.deleteTransactionUseCase = deleteTransactionUseCase;
        this.getFinancialSummaryUseCase = getFinancialSummaryUseCase;
        this.updateTransactionUseCase = updateTransactionUseCase;
    }

    @PostMapping("/transactions")
    public ResponseEntity<Transaction> createTransaction(
            @Valid @RequestBody CreateTransactionRequest request) {

        Transaction transaction = createTransactionUseCase.create(request);

        return ResponseEntity.status(201).body(transaction);
    }

    @GetMapping("/transactions")
    public List<Transaction> getAllTransactions() {
        return getAllTransactionsUseCase.execute();
    }


    @GetMapping("/transactions/{id}")
    public ResponseEntity<Transaction> getTransactionById(
            @PathVariable Long id
    ) {
        return getTransactionByIdUseCase.execute(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new java.util.NoSuchElementException(
                        "Transação não encontrada: " + id
                ));
    }

    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable Long id
    ) {
        deleteTransactionUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/transactions/summary")
    public FinancialSummaryResponse getFinancialSummary() {
        return getFinancialSummaryUseCase.execute();
    }


    @PutMapping("/transactions/{id}")
    public ResponseEntity<Transaction> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody CreateTransactionRequest request
    ) {
        Transaction updatedTransaction =
                updateTransactionUseCase.execute(id, request);

        return ResponseEntity.ok(updatedTransaction);
    }
}
