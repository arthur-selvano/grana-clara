package br.com.granaclara.application.usecase;

import br.com.granaclara.domain.model.Transaction;
import br.com.granaclara.domain.model.TransactionType;
import br.com.granaclara.domain.repository.TransactionRepository;
import br.com.granaclara.application.dto.CreateTransactionRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateTransactionUseCase {

    private final TransactionRepository repository;

    public CreateTransactionUseCase(TransactionRepository repository){
        this.repository = repository;
    }

    public Transaction create(CreateTransactionRequest request){

        String description = request.getDescription();
        BigDecimal amount = request.getAmount();
        LocalDate date = request.getDate();
        TransactionType type = request.getType();

        var transaction = new Transaction(description, amount, date, type);

        return repository.save(transaction);
    }



}
