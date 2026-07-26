/* BlankFactor (C)2023 */
package com.marqeta.authorization.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.marqeta.authorization.validator.AuthorizationStateValidator;
import com.marqeta.authorization.validator.PrecedingTransactionTokenValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Map;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NotNull(message = "json body is required")
@PrecedingTransactionTokenValidator
public class AuthorizationRequest {

    @NotBlank(message = "type is required")
    private String type;

    @NotBlank(message = "state is required")
    @AuthorizationStateValidator
    private String state;

    private String token;

    @JsonProperty("preceding_related_transaction_token")
    private String precedingTransactionToken;

    @JsonProperty("user_token")
    @NotBlank(message = "user_token is required")
    private String userToken;

    @JsonProperty("acting_user_token")
    @NotBlank(message = "acting_user_token is required")
    private String actingUserToken;

    @JsonProperty("card_token")
    @NotBlank(message = "card_token is required")
    private String cardToken;

    @JsonProperty("card_product_token")
    private String cardProductToken;

    @JsonProperty("original_credit")
    private OriginalCredit originalCredit;

    private Gpa gpa;

    @JsonProperty("gpa_order")
    @NotNull(message = "gpa_order is required")
    @Valid
    private GpaOrder gpaOrder;

    @JsonProperty("created_time")
    private String createdTime;

    @JsonProperty("user_transaction_time")
    private String userTransactionTime;

    @JsonProperty("settlement_date")
    private String settlementDate;

    @JsonProperty("request_amount")
    private BigDecimal requestAmount;

    @NotNull(message = "amount is required")
    @Min(value = 0, message = "amount should be greater or equal than zero")
    private BigDecimal amount;

    @JsonProperty("currency_code")
    private String currencyCode;

    private String network;

    @JsonProperty("acquirer_fee_amount")
    private BigDecimal acquirerFeeAmount;

    private Acquirer acquirer;
    private User user;
    private Card card;

    @JsonProperty("issuer_received_time")
    private String issuerReceivedTime;

    @JsonProperty("issuer_payment_node")
    private String issuerPaymentNode;

    @JsonProperty("network_reference_id")
    private String networkReferenceId;

    @JsonProperty("card_acceptor")
    @Valid
    private CardAcceptor cardAcceptor;

    private Pos pos;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OriginalCredit {
        @JsonProperty("fast_funds_enabled")
        private Boolean fastFundsEnabled;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Gpa {
        @JsonProperty("currency_code")
        private String currencyCode;

        @JsonProperty("ledger_balance")
        private BigDecimal ledgerBalance;

        @JsonProperty("available_balance")
        private BigDecimal availableBalance;

        @JsonProperty("credit_balance")
        private BigDecimal creditBalance;

        @JsonProperty("pending_credits")
        private double pendingCredits;

        private Map<String, Balance> balances;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Balance {
            @JsonProperty("currency_code")
            private String currencyCode;

            @JsonProperty("ledger_balance")
            private BigDecimal ledgerBalance;

            @JsonProperty("available_balance")
            private BigDecimal availableBalance;

            @JsonProperty("credit_balance")
            private BigDecimal creditBalance;

            @JsonProperty("pending_credits")
            private BigDecimal pendingCredits;
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GpaOrder {
        private String token;
        private BigDecimal amount;

        @JsonProperty("transaction_token")
        @NotBlank(message = "transaction_token is required")
        private String transactionToken;

        private String state;
        private Funding funding;

        @JsonProperty("funding_source_token")
        private String fundingSourceToken;

        @NotNull(message = "jit_funding is required")
        @JsonProperty("jit_funding")
        private JitFunding jitFunding;

        @JsonProperty("user_token")
        private String userToken;

        @JsonProperty("currency_code")
        private String currencyCode;

        private String memo;
        private String tags;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Funding {
            private BigDecimal amount;
            private Source source;

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Source {
                private String type;
                private String token;
                private String name;
                private Boolean active;

                @JsonProperty("is_default_account")
                private Boolean isDefaultAccount;

                @JsonProperty("created_time")
                private String createdTime;

                @JsonProperty("last_modified_time")
                private String lastModifiedTime;
            }
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class JitFunding {
            @NotBlank(message = "jit_funding token is required")
            private String token;

            @NotBlank(message = "jit_funding method is required")
            private String method;

            @JsonProperty("user_token")
            private String userToken;

            @JsonProperty("acting_user_token")
            private String actingUserToken;

            private BigDecimal amount;
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Acquirer {
        @JsonProperty("system_trace_audit_number")
        private String systemTraceAuditNumber;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User {
        private Map<String, String> metadata;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Card {
        @JsonProperty("last_four")
        private String lastFour;

        private Map<String, String> metadata;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CardAcceptor {
        private String mid;
        private String mcc;

        @NotBlank(message = "name is required")
        private String name;

        @JsonProperty("street_address")
        private String streetAddress;

        @NotBlank(message = "city is required")
        private String city;

        private String state;

        @JsonProperty("postal_code")
        private String postalCode;

        @JsonProperty("country_code")
        @NotBlank(message = "country_code is required")
        private String countryCode;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Pos {
        @JsonProperty("pin_present")
        private Boolean pinPresent;

        @JsonProperty("partial_approval_capable")
        private Boolean partialApprovalCapable;

        @JsonProperty("purchase_amount_only")
        private Boolean purchaseAmountOnly;

        @JsonProperty("is_recurring")
        private Boolean recurring;

        @JsonProperty("is_installment")
        private Boolean installment;
    }
}
