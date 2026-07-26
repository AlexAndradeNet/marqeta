/* BlankFactor (C)2023 */
package com.marqeta.authorization.mapper.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marqeta.authorization.dto.Authorization;
import com.marqeta.authorization.dto.request.AuthorizationRequest;
import com.marqeta.authorization.dto.request.CoreAuthorizationRequest;
import com.marqeta.authorization.mapper.AuthorizationMapper;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorizationMapperImpl implements AuthorizationMapper {

    protected final ObjectMapper objectMapper;

    @Override
    public Authorization mapAuthorization(AuthorizationRequest authorizationRequest)
            throws JsonProcessingException, ValidationException {
        Authorization authorization = new Authorization();

        // Core processing fields
        authorization.setType(authorizationRequest.getType());
        authorization.setAmount(authorizationRequest.getAmount());
        authorization.setTransactionToken(authorizationRequest.getToken());
        authorization.setCardToken(authorizationRequest.getCardToken());
        authorization.setStatus(authorizationRequest.getState());
        authorization.setUserToken(authorizationRequest.getUserToken());
        authorization.setPrecedingTransactionToken(
                authorizationRequest.getPrecedingTransactionToken());
        authorization.setCurrency(authorizationRequest.getCurrencyCode());
        authorization.setTimestamp(authorizationRequest.getCreatedTime());
        authorization.setScheme(authorizationRequest.getNetwork());

        if (authorizationRequest.getCardAcceptor() != null) {
            authorization.setMerchantName(authorizationRequest.getCardAcceptor().getName());
            authorization.setMerchantCity(authorizationRequest.getCardAcceptor().getCity());
            authorization.setMerchantCountry(
                    authorizationRequest.getCardAcceptor().getCountryCode());
        }
        // Payload
        authorization.setPayload(objectMapper.writeValueAsString(authorizationRequest));

        // Marqeta processing fields
        AuthorizationRequest.GpaOrder gpaOrder = authorizationRequest.getGpaOrder();
        AuthorizationRequest.GpaOrder.JitFunding jitFunding = gpaOrder.getJitFunding();
        authorization.setJitFundingToken(jitFunding.getToken());
        authorization.setActingUserToken(authorizationRequest.getActingUserToken());
        authorization.setMethod(jitFunding.getMethod());
        authorization.setMemo(gpaOrder.getMemo());
        authorization.setTags(gpaOrder.getTags());

        return authorization;
    }

    @Override
    public CoreAuthorizationRequest mapCoreAuthorizationRequest(Authorization authorization) {
        return CoreAuthorizationRequest.builder()
                .type(authorization.getType())
                .status(authorization.getStatus())
                .transactionToken(authorization.getTransactionToken())
                .cardToken(authorization.getCardToken())
                .userToken(authorization.getUserToken())
                .precedingTransactionToken(authorization.getPrecedingTransactionToken())
                .amount(authorization.getAmount())
                .currency(authorization.getCurrency())
                .timestamp(authorization.getTimestamp())
                .details(authorization.getDetails())
                .scheme(authorization.getScheme())
                .merchantName(authorization.getMerchantName())
                .merchantCity(authorization.getMerchantCity())
                .merchantCountry(authorization.getMerchantCountry())
                .payload(authorization.getPayload())
                .build();
    }
}
