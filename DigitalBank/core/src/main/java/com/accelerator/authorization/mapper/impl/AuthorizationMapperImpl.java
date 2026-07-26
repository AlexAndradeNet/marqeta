/* BlankFactor (C)2023 */
package com.accelerator.authorization.mapper.impl;

import com.accelerator.authorization.dto.Authorization;
import com.accelerator.authorization.dto.AuthorizationError;
import com.accelerator.authorization.dto.AuthorizationStatus;
import com.accelerator.authorization.dto.AuthorizationType;
import com.accelerator.authorization.dto.request.AuthorizationRequest;
import com.accelerator.authorization.exception.InvalidAuthorizationException;
import com.accelerator.authorization.mapper.AuthorizationMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorizationMapperImpl implements AuthorizationMapper {

    protected final ObjectMapper objectMapper;

    @Override
    public Authorization mapAuthorization(AuthorizationRequest authorizationRequest)
            throws InvalidAuthorizationException {
        Authorization authorization = new Authorization();
        authorization.setAmount(authorizationRequest.getAmount());
        authorization.setCardToken(authorizationRequest.getCardToken());
        authorization.setUserToken(authorizationRequest.getUserToken());
        authorization.setTransactionToken(authorizationRequest.getTransactionToken());
        authorization.setPrecedingTransactionToken(
                authorizationRequest.getPrecedingTransactionToken());
        authorization.setPayload(authorizationRequest.getPayload());
        authorization.setCurrency(authorizationRequest.getCurrency());
        authorization.setTimestamp(authorizationRequest.getTimestamp());
        authorization.setDetails(authorizationRequest.getDetails());
        authorization.setScheme(authorizationRequest.getScheme());
        authorization.setMerchantName(authorizationRequest.getMerchantName());
        authorization.setMerchantCity(authorizationRequest.getMerchantCity());
        authorization.setMerchantCountry(authorizationRequest.getMerchantCountry());

        // Authorization type
        authorization.setType(
                Arrays.stream(AuthorizationType.values())
                        .filter(
                                authorizationType ->
                                        authorizationType
                                                .getValue()
                                                .equals(authorizationRequest.getType()))
                        .findFirst()
                        .orElse(AuthorizationType.INVALID));

        if (AuthorizationType.INVALID == authorization.getType()) {
            throw new InvalidAuthorizationException(
                    AuthorizationError.INVALID_TRANSACTION_TYPE.getValue(), authorization);
        }

        // Authorization status
        authorization.setStatus(
                Arrays.stream(AuthorizationStatus.values())
                        .filter(
                                authorizationStatus ->
                                        authorizationStatus
                                                .getValue()
                                                .equals(authorizationRequest.getStatus()))
                        .findFirst()
                        .orElse(AuthorizationStatus.INVALID));

        if (AuthorizationStatus.INVALID == authorization.getStatus()) {
            throw new InvalidAuthorizationException(
                    AuthorizationError.INVALID_TRANSACTION_STATUS.getValue(), authorization);
        }

        return authorization;
    }
}
