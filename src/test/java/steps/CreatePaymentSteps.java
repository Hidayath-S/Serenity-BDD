package steps;

import io.restassured.path.json.JsonPath;
import models.createPayment.PaymentResponse;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.questions.TheValue;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;
import utilities.RegExUtil;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.*;

public class CreatePaymentSteps extends ScenarioSteps {

    private final String createPaymentURL = "api/payment";

    /**
     * Context: Make Create Payment API Call
     *
     * @param apiUser     API driver
     * @param requestBody POST request-body for making API Call
     * @author Hidayath
     * @since 5/22/2021
     */
    @Step
    public void makeCreatePaymentAPICall(Actor apiUser, String requestBody) {
        apiUser.attemptsTo(Post.to(createPaymentURL).with(request -> request.header("Content-Type", "application/json"
        ).body(requestBody)));
        JsonPath jsonPath = new JsonPath(requestBody);
        apiUser.remember("custId", jsonPath.getString("custId"));
        apiUser.remember("fromAcctNo", jsonPath.getString("fromAcctNo"));
        apiUser.remember("toAcctNo", jsonPath.getString("toAcctNo"));
    }

    /**
     * Context: To Verify Create Payment API Response Details
     *
     * @author Hidayath
     * @since 22/05/2021
     */
    @Step
    public void verifyCreatePaymentAPIResponseDetails(Actor apiUser) {
        //Initializing expected values
        String paymentStatus = "SUCCESS";
        PaymentResponse response = SerenityRest.lastResponse().jsonPath().getObject("PaymentResponse",
                PaymentResponse.class);
        apiUser.should(seeThat(TheValue.of("amount", response.getAmount()), isA(Double.class)),
                seeThat(TheValue.of("paymentId", response.getPaymentId()), isA(Integer.class)),
                seeThat(TheValue.of("toAcctNo", response.getToAcctNo()), equalTo(apiUser.recall("toAcctNo"))),
                seeThat(TheValue.of("paymentCreatedDate", response.getPaymentCreatedDate()),
                        matchesPattern(RegExUtil.REGEX_PAYMENT_CREATED_DATE)),
                seeThat(TheValue.of("fromAcctNo", response.getFromAcctNo()), equalTo(apiUser.recall("fromAcctNo"))),
                seeThat(TheValue.of("paymentStatus", response.getPaymentStatus()), equalTo(paymentStatus)),
                seeThat(TheValue.of("customerDetails Object", response.getCustomerDetails()), notNullValue()));

        //Validating customerDetails Object fields
        if (response.getCustomerDetails() != null) {
            apiUser.should(seeThat(TheValue.of("ECN", response.getCustomerDetails().getEcn()), isA(Long.class)),
                    seeThat(TheValue.of("phoneNumber", response.getCustomerDetails().getPhoneNumber()),
                            isA(Long.class)),
                    seeThat(TheValue.of("FirstName", response.getCustomerDetails().getFirstName()),
                            not(emptyOrNullString())),
                    seeThat(TheValue.of("custId", response.getCustomerDetails().getCustId()), equalTo(apiUser.recall(
                            "custId"))),
                    seeThat(TheValue.of("LastName", response.getCustomerDetails().getLastName()),
                            not(emptyOrNullString())),
                    seeThat(TheValue.of("SSN", response.getCustomerDetails().getSsn()), isA(Long.class)));
        }
    }
}
