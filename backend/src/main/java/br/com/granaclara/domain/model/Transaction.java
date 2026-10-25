package br.com.granaclara.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {

    private Long id;
    private String description;
    private BigDecimal amount;
    private LocalDate date;
    private TransactionType type;

    public Transaction(String description,
    BigDecimal amount,
    LocalDate date,
    TransactionType type)
    {
        if(description == null || description.isBlank()) {
            throw new IllegalArgumentException("A descrição é obrigatória!");
        }

        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero!");
        }

        if(date == null || type == null){
            throw new IllegalArgumentException("Data e tipo são obrigatórios!");
        }

        this.description = description;
        this.amount = amount;
        this.date = date;
        this.type = type;
    }

    public void assignId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID é obrigatório!");
        }

        if (this.id != null) {
            throw new IllegalStateException("O ID já foi atribuído!");
        }

        this.id = id;
    }


    public void update(
            String description,
            BigDecimal amount,
            LocalDate date,
            TransactionType type
    ) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("A descrição é obrigatória!");
        }

        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero!");
        }

        if (date == null || type == null) {
            throw new IllegalArgumentException("Data e tipo são obrigatórios!");
        }

        this.description = description;
        this.amount = amount;
        this.date = date;
        this.type = type;
    }

    public Long getId() {
        return id;
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
