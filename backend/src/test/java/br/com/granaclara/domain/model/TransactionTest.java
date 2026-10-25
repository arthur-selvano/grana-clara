
package br.com.granaclara.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void shouldUpdateTransactionAndKeepSameId() {
        Transaction transaction = new Transaction(
                "Salário",
                new BigDecimal("3500.00"),
                LocalDate.of(2026, 10, 24),
                TransactionType.INCOME
        );

        transaction.assignId(1L);

        transaction.update(
                "Salário atualizado",
                new BigDecimal("4000.00"),
                LocalDate.of(2026, 10, 25),
                TransactionType.INCOME
        );

        assertEquals(1L, transaction.getId());
        assertEquals("Salário atualizado", transaction.getDescription());
        assertEquals(0, new BigDecimal("4000.00")
                .compareTo(transaction.getAmount()));
        assertEquals(LocalDate.of(2026, 10, 25), transaction.getDate());
        assertEquals(TransactionType.INCOME, transaction.getType());
    }

    @Test
    void shouldRejectBlankDescriptionWhenUpdating() {
        Transaction transaction = new Transaction(
                "Salário",
                new BigDecimal("3500.00"),
                LocalDate.of(2026, 10, 24),
                TransactionType.INCOME
        );

        assertThrows(IllegalArgumentException.class, () ->
                transaction.update(
                        " ",
                        new BigDecimal("4000.00"),
                        LocalDate.of(2026, 10, 25),
                        TransactionType.INCOME
                )
        );
    }

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


    @Test
    void shouldRejectZeroAmountWhenUpdating() {
        Transaction transaction = new Transaction(
                "Salário",
                new BigDecimal("3500.00"),
                LocalDate.of(2026, 10, 24),
                TransactionType.INCOME
        );

        assertThrows(IllegalArgumentException.class, () ->
                transaction.update(
                        "Salário atualizado",
                        BigDecimal.ZERO,
                        LocalDate.of(2026, 10, 25),
                        TransactionType.INCOME
                )
        );
    }
}