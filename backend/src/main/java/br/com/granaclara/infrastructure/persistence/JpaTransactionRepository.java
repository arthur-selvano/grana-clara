
package br.com.granaclara.infrastructure.persistence;

import br.com.granaclara.domain.model.Transaction;
import br.com.granaclara.domain.repository.TransactionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaTransactionRepository implements TransactionRepository {

    private final SpringDataTransactionRepository springDataRepository;

    public JpaTransactionRepository(
            SpringDataTransactionRepository springDataRepository
    ) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        var entity = new TransactionEntity(
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getDate(),
                transaction.getType()
        );

        var savedEntity = springDataRepository.save(entity);

        return toDomain(savedEntity);
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return springDataRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<Transaction> findAll() {
        return springDataRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void deleteBy(Long id) {
        springDataRepository.deleteById(id);
    }

    private Transaction toDomain(TransactionEntity entity) {
        var transaction = new Transaction(
                entity.getDescription(),
                entity.getAmount(),
                entity.getDate(),
                entity.getType()
        );

        transaction.assignId(entity.getId());

        return transaction;
    }
}
