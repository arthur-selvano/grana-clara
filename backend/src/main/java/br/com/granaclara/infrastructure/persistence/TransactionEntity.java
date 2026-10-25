package br.com.granaclara.infrastructure.persistence;

import br.com.granaclara.domain.model.TransactionType;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private BigDecimal amount;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    protected TransactionEntity() {}

    public TransactionEntity(
            String description,
            BigDecimal amount,
            LocalDate date,
            TransactionType type
    ) {
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

    public LocalDate getDate() {
        return date;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }


    public void update(
            String description,
            BigDecimal amount,
            LocalDate date,
            TransactionType type
    ) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.type = type;
    }
}