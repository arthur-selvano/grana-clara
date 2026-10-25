
package br.com.granaclara.application.usecase;

import br.com.granaclara.application.dto.CreateTransactionRequest;
import br.com.granaclara.domain.model.Transaction;
import br.com.granaclara.domain.model.TransactionType;
import br.com.granaclara.domain.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateTransactionUseCaseTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private UpdateTransactionUseCase updateTransactionUseCase;

    @Test
    void shouldUpdateExistingTransaction() {
        Transaction transaction = new Transaction(
                "Salário",
                new BigDecimal("3500.00"),
                LocalDate.of(2026, 10, 24),
                TransactionType.INCOME
        );
        transaction.assignId(1L);

        CreateTransactionRequest request = new CreateTransactionRequest(
                "Salário atualizado",
                new BigDecimal("4000.00"),
                LocalDate.of(2026, 10, 25),
                TransactionType.INCOME
        );

        when(transactionRepository.findById(1L))
                .thenReturn(Optional.of(transaction));
        when(transactionRepository.save(transaction))
                .thenReturn(transaction);

        Transaction result = updateTransactionUseCase.execute(1L, request);

        assertEquals(1L, result.getId());
        assertEquals("Salário atualizado", result.getDescription());
        assertEquals(0, new BigDecimal("4000.00")
                .compareTo(result.getAmount()));
        assertEquals(LocalDate.of(2026, 10, 25), result.getDate());
        assertEquals(TransactionType.INCOME, result.getType());

        verify(transactionRepository).findById(1L);
        verify(transactionRepository).save(transaction);
    }

    @Test
    void shouldThrowExceptionWhenTransactionDoesNotExist() {
        CreateTransactionRequest request = new CreateTransactionRequest(
                "Salário atualizado",
                new BigDecimal("4000.00"),
                LocalDate.of(2026, 10, 25),
                TransactionType.INCOME
        );

        when(transactionRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> updateTransactionUseCase.execute(99L, request)
        );

        verify(transactionRepository, never()).save(any());
    }
}
