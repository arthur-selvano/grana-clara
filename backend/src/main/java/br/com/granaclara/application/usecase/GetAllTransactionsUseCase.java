
package br.com.granaclara.application.usecase;

import br.com.granaclara.domain.model.Transaction;
import br.com.granaclara.domain.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllTransactionsUseCase {

    private final TransactionRepository transactionRepository;

    public GetAllTransactionsUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> execute() {
        return transactionRepository.findAll();
    }
}
