/* BlankFactor (C)2018 */
package com.commons;

import static org.hamcrest.Matchers.lessThan;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import java.util.concurrent.TimeUnit;
import org.apache.http.HttpHeaders;

public class ReusableSpecifications {

    private ReusableSpecifications() {
        // Private constructor to prevent instantiation
    }

    public static RequestSpecification getGenericRequestSpec() {
        RequestSpecBuilder rspec;
        RequestSpecification requestSpecification;

        rspec = new RequestSpecBuilder();
        rspec.setContentType(ContentType.JSON);
        requestSpecification = rspec.build();
        return requestSpecification;
    }

    public static ResponseSpecification getGenericResponseSpec() {
        ResponseSpecBuilder resSpec;
        ResponseSpecification responseSpecification;

        resSpec = new ResponseSpecBuilder();
        resSpec.expectHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        resSpec.expectResponseTime(lessThan(10L), TimeUnit.SECONDS);
        responseSpecification = resSpec.build();
        return responseSpecification;
    }
}
