
package br.com.granaclara.application.usecase;

import br.com.granaclara.application.dto.FinancialSummaryResponse;
import br.com.granaclara.domain.model.Transaction;
import br.com.granaclara.domain.model.TransactionType;
import br.com.granaclara.domain.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetFinancialSummaryUseCaseTest {

    @Mock
    private TransactionRepository transactionRepository;

    private GetFinancialSummaryUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetFinancialSummaryUseCase(transactionRepository);
    }

    @Test
    void shouldCalculateIncomeExpensesAndBalance() {
        List<Transaction> transactions = List.of(
                new Transaction(
                        "Salário",
                        new BigDecimal("3500.00"),
                        LocalDate.of(2026, 10, 1),
                        TransactionType.INCOME
                ),
                new Transaction(
                        "Aluguel",
                        new BigDecimal("800.00"),
                        LocalDate.of(2026, 10, 5),
                        TransactionType.EXPENSE
                ),
                new Transaction(
                        "Internet",
                        new BigDecimal("200.00"),
                        LocalDate.of(2026, 10, 8),
                        TransactionType.EXPENSE
                )
        );

        when(transactionRepository.findAll()).thenReturn(transactions);

        FinancialSummaryResponse summary = useCase.execute();

        assertEquals(0, new BigDecimal("3500.00")
                .compareTo(summary.getTotalIncome()));
        assertEquals(0, new BigDecimal("1000.00")
                .compareTo(summary.getTotalExpenses()));
        assertEquals(0, new BigDecimal("2500.00")
                .compareTo(summary.getBalance()));
    }

    @Test
    void shouldReturnZeroTotalsWhenThereAreNoTransactions() {
        when(transactionRepository.findAll()).thenReturn(List.of());

        FinancialSummaryResponse summary = useCase.execute();

        assertEquals(0, BigDecimal.ZERO.compareTo(summary.getTotalIncome()));
        assertEquals(0, BigDecimal.ZERO.compareTo(summary.getTotalExpenses()));
        assertEquals(0, BigDecimal.ZERO.compareTo(summary.getBalance()));
    }

    @Test
    void shouldReturnNegativeBalanceWhenThereAreOnlyExpenses() {
        List<Transaction> transactions = List.of(
                new Transaction(
                        "Mercado",
                        new BigDecimal("150.00"),
                        LocalDate.of(2026, 10, 10),
                        TransactionType.EXPENSE
                )
        );

        when(transactionRepository.findAll()).thenReturn(transactions);

        FinancialSummaryResponse summary = useCase.execute();

        assertEquals(0, BigDecimal.ZERO.compareTo(summary.getTotalIncome()));
        assertEquals(0, new BigDecimal("150.00")
                .compareTo(summary.getTotalExpenses()));
        assertEquals(0, new BigDecimal("-150.00")
                .compareTo(summary.getBalance()));
    }
}
