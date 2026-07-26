/* BlankFactor (C)2023 */
package com.marqeta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MarqetaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarqetaApplication.class, args);
    }
}
