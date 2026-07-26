/* BlankFactor (C)2023 */
package com.blankfactor.marqeta.digitalbank.qa.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import java.util.Collection;
import java.util.Optional;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

public class SharedSteps {
    Optional<String> testCaseIDOptional = Optional.empty();

    @Before(order = 1)
    public void setTheStage(Scenario sc) {
        OnStage.setTheStage(new OnlineCast());
        Collection<String> tags = sc.getSourceTagNames();
        testCaseIDOptional = tags.stream().findFirst();
    }

    @After
    public void afterScenario(Scenario sc) {}
}
