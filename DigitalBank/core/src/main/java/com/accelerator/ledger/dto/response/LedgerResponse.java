/* BlankFactor (C)2023 */
package com.accelerator.ledger.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LedgerResponse {

    private String versionResponse;
    private String reservationId;
}
