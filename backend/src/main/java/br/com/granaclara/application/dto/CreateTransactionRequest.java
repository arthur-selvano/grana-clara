package br.com.granaclara.application.dto;

import br.com.granaclara.domain.model.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateTransactionRequest {

    @NotBlank(message = "A descrição é obrigatória!")
    private final String description;

    @NotNull(message = "O valor é obrigatório!")
    @DecimalMin(value = "0.0", inclusive = false, message = "O valor tem que ser maior que zero!")
    private final BigDecimal amount;

    @NotNull(message = "A data é obrigatória!")
    private final LocalDate date;

    @NotNull(message = "O tipo de transação é obrigatório!")
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
