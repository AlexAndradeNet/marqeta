/* BlankFactor (C)2023 */
package com.commons;

import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.thucydides.core.configuration.SystemPropertiesConfiguration;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.webdriver.Configuration;

public final class EnvironmentProperties {

    private static final Configuration<?> CONFIGURATION =
            new SystemPropertiesConfiguration(
                    SystemEnvironmentVariables.createEnvironmentVariables());
    private static final EnvironmentVariables ENVIRONMENT_VARIABLES =
            CONFIGURATION.getEnvironmentVariables();

    private EnvironmentProperties() {
        // Private constructor to prevent instantiation
    }

    public static String getProperty(String nameProperty) {
        return EnvironmentSpecificConfiguration.from(ENVIRONMENT_VARIABLES)
                .getProperty(nameProperty);
    }
}
