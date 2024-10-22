package com.vitalysukhinin.financial_system;

import com.vitalysukhinin.financial_system.entities.Transaction;
import com.vitalysukhinin.financial_system.entities.TransactionType;
import com.vitalysukhinin.financial_system.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TransactionControllerTests {
    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldReturnTransactions() throws Exception {
        mvc.perform(get("/transactions")
                        .with(AuthUtil.auth()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn12TransactionsFromDateToDate() throws Exception {
        mvc.perform(get("/transactions")
                .param("from", "2024-08-01T00:00:00")
                .param("to", "2024-08-31T00:00:00")
                .with(AuthUtil.auth()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(39));
    }

    @Test
    @Transactional
    public void shouldCreateTransaction() throws Exception {

        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        user.setEmail(AuthUtil.email);
        Transaction transaction = new Transaction();
        transaction.setTransactionDate(now);
        transaction.setAmount(1000.0);
        transaction.setDescription("Description");
        transaction.setType(new TransactionType(1, "Income"));
        transaction.setUser(user);

        mvc.perform(post("/transactions")
                        .contentType("application/json")
                        .content(TestUtil.convertObjectToJsonBytes(transaction))
                .with(AuthUtil.auth()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.amount").value(1000.0))
                .andExpect(jsonPath("$.description").value("Description"));
    }
}
