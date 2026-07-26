/* BlankFactor (C)2023 */
package com.marqeta.authorization.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marqeta.authorization.dto.response.CoreAuthorizationResponse;
import com.marqeta.authorization.exception.CoreRequestException;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.http.HttpStatus;

public class CoreErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == HttpStatus.BAD_REQUEST.value()) {
            CoreAuthorizationResponse responseBody;
            try (InputStream bodyIs = response.body().asInputStream()) {
                responseBody = objectMapper.readValue(bodyIs, CoreAuthorizationResponse.class);
            } catch (IOException e) {
                return new CoreRequestException("Invalid response");
            }
            return new CoreRequestException(responseBody);
        }
        return errorDecoder.decode(methodKey, response);
    }
}
