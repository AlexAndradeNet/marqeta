/* BlankFactor (C)2023 */
package com.marqeta.authorization.client;

import com.marqeta.authorization.dto.request.CoreAuthorizationRequest;
import com.marqeta.authorization.dto.response.CoreAuthorizationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        value = "coreAuthGatewayClient",
        url = "${core.api.base-url}",
        configuration = CoreFeignConfig.class)
public interface CoreAuthGatewayClient {

    @PostMapping("/port/authorization-gateway")
    ResponseEntity<CoreAuthorizationResponse> processAuthorization(
            CoreAuthorizationRequest coreAuthorizationRequest);
}
