/* BlankFactor (C)2023 */
package com.commons;

import static net.serenitybdd.rest.SerenityRest.restAssuredThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesRegex;

import io.restassured.module.jsv.JsonSchemaValidator;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

public class CommonAssertions {

    private static final String GUID_REGEX =
            "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";

    @Step("Verify response code 200")
    public void shouldSeeSuccessfulStatusCode() {
        restAssuredThat(response -> response.statusCode(HttpStatus.SC_OK));
    }

    @Step("Verify response code 201 - Created")
    public void shouldSeeCreatedStatusCode() {
        restAssuredThat(response -> response.statusCode(HttpStatus.SC_CREATED));
    }

    @Step("Verify response and Schema for status code 200")
    public void verifySuccessResponseAndSchema(String schemaToVerify) {
        shouldSeeSuccessfulStatusCode();
        validateJSONSchema(schemaToVerify);
    }

    @Step("Verify response and Schema for status code 201")
    public void verifyCreatedResponseAndSchema(String schemaToVerify) {
        shouldSeeCreatedStatusCode();
        validateJSONSchema(schemaToVerify);
    }

    @Step("Verify response schema")
    public void validateJSONSchema(String schemaPath) {
        String finalSchemaPath = EnvironmentProperties.getProperty(schemaPath);
        if (StringUtils.isEmpty(finalSchemaPath)) {
            throw new IllegalArgumentException("Schema path cannot be null or empty");
        }
        restAssuredThat(
                response ->
                        response.body(
                                JsonSchemaValidator.matchesJsonSchemaInClasspath(finalSchemaPath)));
    }

    @Step("Verify that the response contains a valid GUID token")
    public String validateTokenIsGuidAndReturnIt(String responseField) {
        restAssuredThat(response -> response.body(responseField, matchesRegex(GUID_REGEX)));
        return SerenityRest.then().extract().path(responseField);
    }

    @Step("Verify that the response contains the field '{0}' with value '{1}'")
    public void validateFieldValue(String responseField, String expectedValue) {
        restAssuredThat(response -> response.body(responseField, equalTo(expectedValue)));
    }
}
