/* BlankFactor (C)2023 */
package com.accelerator;

import static org.junit.jupiter.api.Assertions.*;

import com.accelerator.common.dto.Account;
import com.accelerator.common.dto.Transaction;
import com.accelerator.common.port.AccountPort;
import com.accelerator.common.port.TransactionPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.SocketPolicy;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
// Allow database rollback after each test
@Transactional
@TestMethodOrder(MethodOrderer.Random.class)
public class BaseTest {

    @Autowired protected MockMvc mockMvc;
    @Autowired protected static ObjectMapper objectMapper;

    // Your JPA repository interface
    @Autowired protected EntityManager entityManager;
    @Autowired protected AccountPort accountPort;
    @Autowired protected TransactionPort transactionPort;

    protected static Account account;
    protected static Account expectedAccount;
    protected static Account transitAccount;
    protected static Transaction expectedTransaction;
    protected static final String CONTENT_TYPE = "application/json";
    protected static final int TRANSIT_ACCOUNT_ID = 33333;
    protected static final int SERVER_PORT = 8089;
    protected static final String LEDGER_SUCCESS_RESPONSE =
            "{\"versionResponse\": \"alpha\", \"reservationId\":"
                    + " \"8afa90f9-5fec-428f-bd17-aec9cc657284\"}";
    private MockWebServer mockWebServerNonStatic;

    @BeforeAll
    protected static void setUp() {
        objectMapper = new ObjectMapper();
    }

    @AfterEach
    protected void tearDown() throws IOException {
        if (mockWebServerNonStatic != null) {
            mockWebServerNonStatic.shutdown();
        }
        resetDatabaseSequences();
        cleanupDatabase();
    }

    @Test
    void emptyTest() {
        // This test is here to prevent JUnit from complaining about no tests
        // being present in this class
        assertTrue(true);
    }

    protected void resetDatabaseSequences() {
        entityManager
                .createNativeQuery("ALTER TABLE account ALTER COLUMN account_id RESTART WITH 1")
                .executeUpdate();
        entityManager
                .createNativeQuery(
                        "ALTER TABLE transaction ALTER COLUMN transaction_id RESTART WITH 1")
                .executeUpdate();
    }

    protected void cleanupDatabase() {
        transactionPort.deleteAll();
        accountPort.deleteAll();
    }

    protected void checkAccountBalance(long accountId, int accountBalance, int reservationBalance) {
        Optional<Account> dbAccount = accountPort.getById(accountId);
        assertTrue(dbAccount.isPresent(), String.format("Account '%s' not found", accountId));

        BigDecimal formattedAccountBalance = formatBigDecimal(dbAccount.get().getAccountBalance());
        BigDecimal formattedReservationBalance =
                formatBigDecimal(dbAccount.get().getReservationBalance());

        assertEquals(formatBigDecimal(accountBalance), formattedAccountBalance);
        assertEquals(formatBigDecimal(reservationBalance), formattedReservationBalance);
    }

    protected void checkAccountBalance(int accountBalance, int reservationBalance) {
        long accountId =
                (account == null || account.getAccountId() == null)
                        ? 1
                        : account.getAccountId(); // First table item
        checkAccountBalance(accountId, accountBalance, reservationBalance);

        if (expectedTransaction != null) {
            expectedTransaction.getAccount().setAccountBalance(formatBigDecimal(accountBalance));
            expectedTransaction
                    .getAccount()
                    .setReservationBalance(formatBigDecimal(reservationBalance));
        }
    }

    protected void checkTransitAccountBalance(int accountBalance, int reservationBalance) {
        checkAccountBalance(TRANSIT_ACCOUNT_ID, accountBalance, reservationBalance);
    }

    protected BigDecimal formatBigDecimal(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    protected BigDecimal formatBigDecimal(int integer) {
        return formatBigDecimal(new BigDecimal(integer));
    }

    protected void checkForNoTransactionRecorded() {
        Optional<Transaction> savedTransaction =
                transactionPort.getById(expectedTransaction.getTransactionId());
        assertFalse(savedTransaction.isPresent(), "Transaction was unexpectedly recorded");
    }

    protected void checkForTransactionRecorded() {
        Optional<Transaction> savedTransaction =
                transactionPort.getByTransactionToken(expectedTransaction.getTransactionToken());
        assertTrue(
                savedTransaction.isPresent(),
                String.format(
                        "Transaction %s not recorded", expectedTransaction.getTransactionId()));

        formatAndSetTransactionDetails(savedTransaction.get());

        assertEquals(
                expectedTransaction,
                savedTransaction.get(),
                "Saved transaction does not match expected transaction");
    }

    private void formatAndSetTransactionDetails(Transaction savedTransaction) {
        savedTransaction
                .getAccount()
                .setAccountBalance(
                        formatBigDecimal(savedTransaction.getAccount().getAccountBalance()));
        savedTransaction
                .getAccount()
                .setReservationBalance(
                        formatBigDecimal(savedTransaction.getAccount().getReservationBalance()));
        savedTransaction.setAmount(formatBigDecimal(savedTransaction.getAmount()));

        expectedTransaction.setTransactionId(savedTransaction.getTransactionId());
        expectedTransaction.getAccount().setAccountId(savedTransaction.getAccount().getAccountId());
        expectedTransaction.setTransactionDate(savedTransaction.getTransactionDate());
    }

    protected void saveAccountChangesIntoDB() {
        account = accountPort.save(account);
        expectedAccount.setAccountId(account.getAccountId());
        expectedTransaction.setAccount(expectedAccount);
    }

    protected void createTransitAccountForClearingTransaction() {
        transitAccount = new Account();
        transitAccount.setAccountId((long) TRANSIT_ACCOUNT_ID);
        transitAccount.setUserId("4444L");
        transitAccount.setCardId("4444L");
        transitAccount.setAccountBalance(formatBigDecimal(0));
        transitAccount.setReservationBalance(formatBigDecimal(0));

        entityManager
                .createNativeQuery(
                        "INSERT INTO account( account_id, user_id, card_id, account_balance, reservation_balance ) VALUES (%s, %s, '%s', %s, %s)"
                                .formatted(
                                        transitAccount.getAccountId(),
                                        transitAccount.getUserId(),
                                        transitAccount.getCardId(),
                                        transitAccount.getAccountBalance(),
                                        transitAccount.getReservationBalance()))
                .executeUpdate();
    }

    protected void startLedgerMockServer() throws IOException {
        mockWebServerNonStatic = new MockWebServer();
        mockWebServerNonStatic.start(SERVER_PORT);
    }

    protected void setLedgerMockResponse(String response, HttpStatus httpStatus) {
        mockWebServerNonStatic.enqueue(
                new MockResponse()
                        .setBody(response)
                        .setResponseCode(httpStatus.value())
                        .addHeader("Content-Type", CONTENT_TYPE));
    }

    protected void setLedgerMockResponseToSuccess() {
        setLedgerMockResponse(LEDGER_SUCCESS_RESPONSE, HttpStatus.OK);
    }

    protected void setLedgerMockResponseToNetworkFailure() {
        mockWebServerNonStatic.enqueue(
                new MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START));
    }
}
