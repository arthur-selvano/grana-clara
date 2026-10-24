
package br.com.granaclara.application.usecase;

import br.com.granaclara.domain.repository.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteTransactionUseCase {

    private final TransactionRepository transactionRepository;

    public DeleteTransactionUseCase(
            TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }


    public void execute(Long id) {
        if (transactionRepository.findById(id).isEmpty()) {
            throw new java.util.NoSuchElementException(
                    "Transação não encontrada: " + id);
        }

        transactionRepository.deleteBy(id);
    }
}
