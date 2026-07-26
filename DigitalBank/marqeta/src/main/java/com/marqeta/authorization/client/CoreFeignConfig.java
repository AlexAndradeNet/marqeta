/* BlankFactor (C)2023 */
package com.marqeta.authorization.client;

import org.springframework.context.annotation.Bean;

public class CoreFeignConfig {

    @Bean
    public CoreErrorDecoder clientErrorDecoder() {
        return new CoreErrorDecoder();
    }
}
