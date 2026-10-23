package br.com.granaclara.domain.repository;

import br.com.granaclara.domain.model.Transaction;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository {

    Transaction save(Transaction transaction);

    Optional<Transaction> findById(Long id);

    List<Transaction> findAll();

    void deleteBy(Long id);

}
