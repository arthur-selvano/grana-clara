package br.com.granaclara.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataTransactionRepository
        extends JpaRepository<TransactionEntity, Long> {
}
