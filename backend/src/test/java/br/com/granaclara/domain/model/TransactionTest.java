
package br.com.granaclara.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionTest {

    @Test
    void shouldCreateTransactionWithValidData() {
        assertDoesNotThrow(() -> new Transaction(
                "Salário",
                new BigDecimal("3500.00"),
                LocalDate.of(2026, 10, 24),
                TransactionType.INCOME
        ));
    }


    @Test
    void shouldRejectBlankDescription() {
        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(
                        "   ",
                        new BigDecimal("100.00"),
                        LocalDate.of(2026, 10, 24),
                        TransactionType.EXPENSE
                )
        );
    }
}
