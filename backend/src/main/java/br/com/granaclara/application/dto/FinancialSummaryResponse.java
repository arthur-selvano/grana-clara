
package br.com.granaclara.application.dto;

import java.math.BigDecimal;

public class FinancialSummaryResponse {

    private final BigDecimal totalIncome;
    private final BigDecimal totalExpenses;
    private final BigDecimal balance;

    public FinancialSummaryResponse(
            BigDecimal totalIncome,
            BigDecimal totalExpenses,
            BigDecimal balance
    ) {
        this.totalIncome = totalIncome;
        this.totalExpenses = totalExpenses;
        this.balance = balance;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
