package features.createPaymentAPI;

import io.restassured.RestAssured;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.thucydides.core.annotations.*;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import steps.CommonSteps;
import steps.CreatePaymentSteps;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import org.hamcrest.Matchers;

@WithTags({
        @WithTag("API:CreatePayment")
})
@Narrative(text = {"To Create a payment between accounts",
        " As a Create Payment API User",
        " I should be able to create a payment between accounts via Create Payment API"})
@UseTestDataFrom("$DATADIR/CreatePaymentTestData.csv")
@RunWith(SerenityParameterizedRunner.class)
public class CreatePaymentAPIBasicTests {
    @Steps
    CommonSteps commonSteps;
    @Steps
    CreatePaymentSteps createPaymentSteps;

    private EnvironmentVariables environmentVariables;
    private Actor apiUser;

    private String custId;
    private String fromAcctNo;
    private String toAcctNo;
    private String amount;
    private String json;

    /**
     * Context: Perform common steps required for all the tests
     *
     * @author Hidayath
     * @since 22/05/2021
     */
    @Before
    public void setUp() {
        String theAPIBaseURL = EnvironmentSpecificConfiguration.from(environmentVariables).getProperty("createPayment" +
                ".base.url");
        RestAssured.baseURI = theAPIBaseURL;
        apiUser = Actor.named("API User").whoCan(CallAnApi.at(theAPIBaseURL));

        // Reading the request payload for create payment API call
        File createPaymentReqBody = new File(EnvironmentSpecificConfiguration.from(environmentVariables).getProperty(
                "serenity.data.dir"), json);
        String updatedRequestBody =
                commonSteps.returnJsonAsString(createPaymentReqBody).replace("{custId1}", custId).replace(
                        "{fromAcctNo}", fromAcctNo).replace("{toAcctNo}", toAcctNo).replace("{amount}", amount);
        createPaymentSteps.makeCreatePaymentAPICall(apiUser, updatedRequestBody);

    }

    /**
     * Context: To verify Create Payment API Response Status Code and Structure.
     *
     * @author Hidayath
     * @since 22/05/2021
     */
    @Test
    @Title("CreatePaymentAPI - To verify API Response Status Code and Structure")
    @WithTags({
            @WithTag("Suite:HealthCheck"),
            @WithTag("Suite:Dev")
    })
    public void verifyCreatePaymentAPIStatusCodeAndStructure() {
        apiUser.should(seeThatResponse("Verify if the Response status Code is as expected",
                response -> response.statusCode(200)));
        apiUser.should(seeThatResponse("Verify if the Response time is as expected",
                response -> response.time(Matchers.lessThanOrEqualTo(15L), TimeUnit.SECONDS)));
        apiUser.should(seeThatResponse("Verify if the Response Structure is as per the JSON Schema",
                response -> response.body(matchesJsonSchemaInClasspath("schema/createPaymentSchema.json"))));
    }

    /**
     * Context: To verify Create Payment API Response Details.
     *
     * @author Hidayath
     * @since 22/05/2021
     */
    @Test
    @Title("CreatePaymentAPI - To verify API Response Details")
    @WithTags({
            @WithTag("Suite:Dev")
    })
    public void verifyCreatePaymentAPIResponseDetails() {
        apiUser.should(seeThatResponse("Verify if the Response status Code is as expected",
                response -> response.statusCode(200)));
        createPaymentSteps.verifyCreatePaymentAPIResponseDetails(apiUser);
    }
}