
package br.com.granaclara.application.usecase;

import br.com.granaclara.domain.model.Transaction;
import br.com.granaclara.domain.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetTransactionByIdUseCase {

    private final TransactionRepository transactionRepository;

    public GetTransactionByIdUseCase(
            TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Optional<Transaction> execute(Long id) {
        return transactionRepository.findById(id);
    }
}
