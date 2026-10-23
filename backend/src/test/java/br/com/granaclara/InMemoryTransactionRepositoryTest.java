
package br.com.granaclara;

import br.com.granaclara.domain.model.Transaction;
import br.com.granaclara.domain.model.TransactionType;
import br.com.granaclara.infrastructure.persistence.InMemoryTransactionRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTransactionRepositoryTest {

    @Test
    void shouldSaveAndFindTransactionById() {
        var repository = new InMemoryTransactionRepository();

        var transaction1 = new Transaction(
                "Salário",
                new BigDecimal("3000.00"),
                LocalDate.now(),
                TransactionType.INCOME
        );

        var transaction2 = new Transaction(
                "Aluguel",
                new BigDecimal("1000.00"),
                LocalDate.now(),
                TransactionType.EXPENSE
        );

        repository.save(transaction1);
        repository.save(transaction2);

        var result = repository.findAll();

        assertEquals(2, result.size());
    }
}
