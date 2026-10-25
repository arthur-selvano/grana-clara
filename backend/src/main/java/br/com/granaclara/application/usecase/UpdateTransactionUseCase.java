
package br.com.granaclara.application.usecase;

import br.com.granaclara.application.dto.CreateTransactionRequest;
import br.com.granaclara.domain.model.Transaction;
import br.com.granaclara.domain.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UpdateTransactionUseCase {

    private final TransactionRepository transactionRepository;

    public UpdateTransactionUseCase(
            TransactionRepository transactionRepository
    ) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction execute(Long id, CreateTransactionRequest request) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "Transação não encontrada: " + id
                ));

        transaction.update(
                request.getDescription(),
                request.getAmount(),
                request.getDate(),
                request.getType()
        );

        return transactionRepository.save(transaction);
    }
}