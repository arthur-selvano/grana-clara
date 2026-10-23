package br.com.granaclara.application.dto;

import br.com.granaclara.domain.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateTransactionRequest {

    private final String description;
    private final BigDecimal amount;
    private final LocalDate date;
    private final TransactionType type;

    public CreateTransactionRequest(String description, BigDecimal amount, LocalDate date, TransactionType type){
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public TransactionType getType() {
        return type;
    }
}
