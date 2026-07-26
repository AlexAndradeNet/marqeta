/* BlankFactor (C)2023 */
package com.accelerator.ledger.client;

import com.accelerator.ledger.dto.request.LedgerRequest;
import com.accelerator.ledger.dto.response.LedgerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "ledgerClient", url = "${ledger.api.base-url}")
public interface LedgerClient {

    @PostMapping("/v1/parties/digitalbank/accounting-transaction/reservation/webhook")
    ResponseEntity<LedgerResponse> processReservation(LedgerRequest ledgerRequest);
}
