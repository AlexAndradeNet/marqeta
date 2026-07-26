/* BlankFactor (C)2023 */
package com.accelerator.ledger.adapter;

import com.accelerator.ledger.client.LedgerClient;
import com.accelerator.ledger.dto.AccountingEntry;
import com.accelerator.ledger.dto.request.LedgerRequest;
import com.accelerator.ledger.dto.response.LedgerResponse;
import com.accelerator.ledger.exception.LedgerAPIException;
import com.accelerator.ledger.mapper.AccountingEntryMapper;
import com.accelerator.ledger.port.LedgerPort;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LedgerAPIAdapter implements LedgerPort {

    private static final String LEDGER_VERSION = "1";
    private static final String LEDGER_RESERVATION_DESCRIPTION =
            "Performing reservation to transit account";
    private static final Long REQUEST_DAYS_AVAILABILITY = 1L;

    private final LedgerClient ledgerClient;
    private final AccountingEntryMapper accountingEntryMapper;

    @Override
    public String processReservation(AccountingEntry source, AccountingEntry target)
            throws LedgerAPIException {

        LedgerRequest ledgerRequest = new LedgerRequest();
        ledgerRequest.setVersion(LEDGER_VERSION);
        ledgerRequest.setTimestamp(LocalDate.now().toString());
        ledgerRequest.setDescription(LEDGER_RESERVATION_DESCRIPTION);
        ledgerRequest.setExpiration(LocalDate.now().plusDays(REQUEST_DAYS_AVAILABILITY).toString());
        ledgerRequest.setEntries(
                List.of(
                        accountingEntryMapper.toRequest(source),
                        accountingEntryMapper.toRequest(target)));

        try {
            LedgerResponse response = ledgerClient.processReservation(ledgerRequest).getBody();

            if (Objects.isNull(response) || Objects.isNull(response.getReservationId())) {
                throw new LedgerAPIException("Invalid Ledger response: null object or reservationId");
            }

            return response.getReservationId();
        } catch (Exception e) {
            throw new LedgerAPIException("Invalid Ledger response: " + e.getMessage());
        }
    }
}
