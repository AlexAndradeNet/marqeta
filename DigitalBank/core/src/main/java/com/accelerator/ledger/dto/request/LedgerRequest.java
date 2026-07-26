/* BlankFactor (C)2023 */
package com.accelerator.ledger.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LedgerRequest {

    private String version;
    private String timestamp;
    private String description;
    private String expiration;
    private List<AccountingEntryRequest> entries;
}
