/* BlankFactor (C)2023 */
package com.blankfactor.marqeta.digitalbank.qa;

import static org.junit.Assert.assertTrue;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        plugin = {"pretty"},
        glue = {"com.blankfactor.marqeta.digitalbank.qa.steps", "com.marqeta.api.commons"},
        features = "classpath:features")
public class RunnerTests {
    @Test
    public void emptyTest() {
        // Empty test to allow the runner to be executed
        assertTrue(true);
    }
}
