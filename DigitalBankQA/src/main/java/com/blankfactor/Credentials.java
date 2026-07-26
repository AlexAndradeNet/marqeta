/* BlankFactor (C)2023 */
package com.blankfactor;

import io.github.cdimascio.dotenv.Dotenv;
import java.net.MalformedURLException;
import java.net.URL;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class Credentials {

    private final String username;
    private final String password;
    private final String bfBankPublicUrl;
    private final String bfMarqetaPublicUrl;

    private Credentials() {
        Dotenv dotenv = Dotenv.configure().load();
        username = getEnv(dotenv, "MARQETA_API_USERNAME");
        password = getEnv(dotenv, "MARQETA_API_PASSWORD");
        bfBankPublicUrl = getEnvUrl(dotenv, "BF_CORE_PUBLIC_URL");
        bfMarqetaPublicUrl = getEnvUrl(dotenv, "BF_MARQETA_PUBLIC_URL");
    }

    private static final class InstanceHolder {
        private static final Credentials instance = new Credentials();
    }

    public static synchronized Credentials getInstance() {
        return InstanceHolder.instance;
    }

    private static String getEnv(Dotenv dotenv, String key) {
        String value = StringUtils.defaultIfBlank(System.getProperty(key), dotenv.get(key));

        if (StringUtils.isBlank(value)) {
            throw new IllegalStateException(
                    "No value found for "
                            + key
                            + ". Check your .env file or system environment variables.");
        }

        return value;
    }

    private static String getEnvUrl(Dotenv dotenv, String key) {
        String value = getEnv(dotenv, key);
        if (!isValidUrl(key, value)) {
            throw new IllegalArgumentException(key + " is not a valid URL.");
        }
        return value;
    }

    private static boolean isValidUrl(String variableName, String urlString) {
        try {
            new URL(urlString);
        } catch (MalformedURLException e) {
            return false;
        }

        if (urlString.endsWith("/")) {
            return false;
        }

        boolean isHttps = urlString.startsWith("https://");
        boolean isHttp = urlString.startsWith("http://");

        if ("BF_MARQETA_PUBLIC_URL".equals(variableName) && !isHttps) {
            return false;
        }

        return !"BF_CORE_PUBLIC_URL".equals(variableName) || (isHttps || isHttp);
    }
}
