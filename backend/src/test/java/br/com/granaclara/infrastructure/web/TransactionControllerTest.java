
package br.com.granaclara.infrastructure.web;

import br.com.granaclara.application.dto.CreateTransactionRequest;
import br.com.granaclara.domain.model.Transaction;
import br.com.granaclara.domain.model.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.LocalDate;


import br.com.granaclara.application.usecase.CreateTransactionUseCase;
import br.com.granaclara.application.usecase.GetAllTransactionsUseCase;
import br.com.granaclara.application.usecase.GetTransactionByIdUseCase;
import br.com.granaclara.application.usecase.DeleteTransactionUseCase;
import br.com.granaclara.application.usecase.GetFinancialSummaryUseCase;
import br.com.granaclara.application.dto.FinancialSummaryResponse;
import br.com.granaclara.application.usecase.UpdateTransactionUseCase;

import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateTransactionUseCase createTransactionUseCase;

    @MockitoBean
    private GetAllTransactionsUseCase getAllTransactionsUseCase;

    @MockitoBean
    private GetTransactionByIdUseCase getTransactionByIdUseCase;

    @MockitoBean
    private DeleteTransactionUseCase deleteTransactionUseCase;

    @MockitoBean
    private GetFinancialSummaryUseCase getFinancialSummaryUseCase;

    @MockitoBean
    private UpdateTransactionUseCase updateTransactionUseCase;

    @Test
    void shouldReturnBadRequestWhenDescriptionIsBlank() throws Exception {
        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "description": "   ",
                                    "amount": 100.00,
                                    "date": "2026-10-24",
                                    "type": "EXPENSE"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]")
                        .value("A descrição é obrigatória!"));
    }

    @Test
    void shouldCreateTransactionWhenRequestIsValid() throws Exception {
        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "description": "Salário",
                                "amount": 3500.00,
                                "date": "2026-10-24",
                                "type": "INCOME"
                            }
                            """))
                .andExpect(status().isCreated());
    }


    @Test
    void shouldReturnFinancialSummary() throws Exception {
        when(getFinancialSummaryUseCase.execute()).thenReturn(
                new FinancialSummaryResponse(
                        new BigDecimal("3500.00"),
                        new BigDecimal("1000.00"),
                        new BigDecimal("2500.00")
                )
        );

        mockMvc.perform(get("/transactions/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalIncome").value(3500.00))
                .andExpect(jsonPath("$.totalExpenses").value(1000.00))
                .andExpect(jsonPath("$.balance").value(2500.00));
    }


    @Test
    void shouldUpdateTransactionSuccessfully() throws Exception {
        Transaction updatedTransaction = new Transaction(
                "Salário atualizado",
                new BigDecimal("4000.00"),
                LocalDate.of(2026, 10, 25),
                TransactionType.INCOME
        );
        updatedTransaction.assignId(1L);

        when(updateTransactionUseCase.execute(
                eq(1L),
                any(CreateTransactionRequest.class)
        )).thenReturn(updatedTransaction);

        mockMvc.perform(put("/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "description": "Salário atualizado",
                                "amount": 4000.00,
                                "date": "2026-10-25",
                                "type": "INCOME"
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Salário atualizado"))
                .andExpect(jsonPath("$.amount").value(4000.00))
                .andExpect(jsonPath("$.type").value("INCOME"));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonexistentTransaction()
            throws Exception {
        when(updateTransactionUseCase.execute(
                eq(999L),
                any(CreateTransactionRequest.class)
        )).thenThrow(new java.util.NoSuchElementException(
                "Transação não encontrada: 999"
        ));

        mockMvc.perform(put("/transactions/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "description": "Salário atualizado",
                                "amount": 4000.00,
                                "date": "2026-10-25",
                                "type": "INCOME"
                            }
                            """))
                .andExpect(status().isNotFound());
    }
}
