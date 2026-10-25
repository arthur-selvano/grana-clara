
package br.com.granaclara.infrastructure.web;

import br.com.granaclara.application.dto.CreateTransactionRequest;
import br.com.granaclara.domain.model.Transaction;
import br.com.granaclara.domain.model.TransactionType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;
import static org.hamcrest.Matchers.is;
import com.fasterxml.jackson.databind.JsonNode;

import static org.junit.jupiter.api.Assertions.assertEquals;


import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TransactionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateAndRetrieveTransaction() throws Exception {
        String request = """
                {
                    "description": "Salário teste integração",
                    "amount": 3500.00,
                    "date": "2026-10-24",
                    "type": "INCOME"
                }
                """;

        String response = mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long id = new ObjectMapper()
                .readTree(response)
                .get("id")
                .asLong();

        mockMvc.perform(get("/transactions/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description")
                        .value("Salário teste integração"))
                .andExpect(jsonPath("$.amount")
                        .value(3500.00))
                .andExpect(jsonPath("$.type")
                        .value("INCOME"));
    }


    @Test
    void shouldReturnNotFoundWhenTransactionDoesNotExist() throws Exception {
        mockMvc.perform(get("/transactions/999999999"))
                .andExpect(status().isNotFound());
    }


    @Test
    void shouldDeleteTransactionSuccessfully() throws Exception {
        String request = """
            {
                "description": "Transação para excluir",
                "amount": 100.00,
                "date": "2026-10-24",
                "type": "EXPENSE"
            }
            """;

        String response = mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long id = new ObjectMapper()
                .readTree(response)
                .get("id")
                .asLong();

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/transactions/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/transactions/" + id))
                .andExpect(status().isNotFound());
    }



    @Test
    void shouldListTransactions() throws Exception {
        String request = """
            {
                "description": "Transação para listar",
                "amount": 250.00,
                "date": "2026-10-24",
                "type": "INCOME"
            }
            """;

        String response = mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long id = new ObjectMapper()
                .readTree(response)
                .get("id")
                .asLong();

        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[?(@.id == " + id + ")]").exists())
                .andExpect(jsonPath("$[?(@.id == " + id + ")].description")
                        .value(org.hamcrest.Matchers.hasItem(
                                "Transação para listar")))
                .andExpect(jsonPath("$[?(@.id == " + id + ")].amount")
                        .value(org.hamcrest.Matchers.hasItem(250.00)))
                .andExpect(jsonPath("$[?(@.id == " + id + ")].date")
                        .value(org.hamcrest.Matchers.hasItem("2026-10-24")))
                .andExpect(jsonPath("$[?(@.id == " + id + ")].type")
                        .value(org.hamcrest.Matchers.hasItem("INCOME")));
    }


    @Test
    void shouldReturnNotFoundWhenDeletingNonexistentTransaction()
            throws Exception {

        mockMvc.perform(
                        org.springframework.test.web.servlet.request
                                .MockMvcRequestBuilders
                                .delete("/transactions/999999999"))
                .andExpect(status().isNotFound());
    }


    @Test
    void shouldRejectTransactionWithBlankDescription() throws Exception {
        String request = """
            {
                "description": "   ",
                "amount": 100.00,
                "date": "2026-10-24",
                "type": "EXPENSE"
            }
            """;

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());
    }


    @Test
    void shouldRejectTransactionWithZeroAmount() throws Exception {
        String request = """
            {
                "description": "Compra inválida",
                "amount": 0,
                "date": "2026-10-24",
                "type": "EXPENSE"
            }
            """;

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());
    }


    @Test
    void shouldRejectTransactionWithoutDate() throws Exception {
        String request = """
            {
                "description": "Compra sem data",
                "amount": 50.00,
                "type": "EXPENSE"
            }
            """;

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());
    }


    @Test
    void shouldRejectTransactionWithoutType() throws Exception {
        String request = """
            {
                "description": "Compra sem tipo",
                "amount": 50.00,
                "date": "2026-10-24"
            }
            """;

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());
    }


    @Test
    void shouldCalculateFinancialSummaryFromPersistedTransactions() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        String initialResponse = mockMvc.perform(get("/transactions/summary"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode initialSummary = objectMapper.readTree(initialResponse);

        BigDecimal initialIncome =
                initialSummary.get("totalIncome").decimalValue();

        BigDecimal initialExpenses =
                initialSummary.get("totalExpenses").decimalValue();

        BigDecimal initialBalance =
                initialSummary.get("balance").decimalValue();

        String incomeRequest = """
            {
                "description": "Salário teste resumo",
                "amount": 3500.00,
                "date": "2026-10-24",
                "type": "INCOME"
            }
            """;

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(incomeRequest))
                .andExpect(status().isCreated());

        String expenseRequest = """
            {
                "description": "Aluguel teste resumo",
                "amount": 800.00,
                "date": "2026-10-24",
                "type": "EXPENSE"
            }
            """;

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expenseRequest))
                .andExpect(status().isCreated());

        String finalResponse = mockMvc.perform(get("/transactions/summary"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode finalSummary = objectMapper.readTree(finalResponse);

        BigDecimal expectedIncome =
                initialIncome.add(new BigDecimal("3500.00"));

        BigDecimal expectedExpenses =
                initialExpenses.add(new BigDecimal("800.00"));

        BigDecimal expectedBalance =
                initialBalance.add(new BigDecimal("2700.00"));

        assertEquals(
                0,
                expectedIncome.compareTo(
                        finalSummary.get("totalIncome").decimalValue()),
                "O total de receitas deve aumentar em R$ 3.500,00"
        );

        assertEquals(
                0,
                expectedExpenses.compareTo(
                        finalSummary.get("totalExpenses").decimalValue()),
                "O total de despesas deve aumentar em R$ 800,00"
        );

        assertEquals(
                0,
                expectedBalance.compareTo(
                        finalSummary.get("balance").decimalValue()),
                "O saldo deve aumentar em R$ 2.700,00"
        );
    }
}
