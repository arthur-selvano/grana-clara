
package br.com.granaclara.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import br.com.granaclara.domain.model.Transaction;
import br.com.granaclara.domain.repository.TransactionRepository;

public class InMemoryTransactionRepository implements TransactionRepository {

    private final List<Transaction> transactions = new ArrayList<>();
    private Long nextId = 1L;

    @Override
    public Transaction save(Transaction transaction) {
        if (transaction.getId() == null) {
            transaction.assignId(nextId++);
        }

        transactions.add(transaction);
        return transaction;
    }

    @Override
    public List<Transaction> findAll() {
        return List.copyOf(transactions);
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        for (Transaction transaction : transactions) {
            if (Objects.equals(transaction.getId(), id)) {
                return Optional.of(transaction);
            }
        }

        return Optional.empty();
    }

    public void deleteBy(Long id) {
        transactions.removeIf(
                transaction -> Objects.equals(transaction.getId(), id)
        );
    }
}
