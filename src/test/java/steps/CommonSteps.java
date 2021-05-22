package steps;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

import java.io.File;
import java.io.FileReader;

public class CommonSteps extends ScenarioSteps {

    /**
     * Context : For logging the content on to report
     *
     * @param message message to be logged
     * @author Hidayath
     * @since 22/05/2021
     */
    @Step
    public void log(String message) {
    }

    /**
     * Context: Return JSON STring from JSON file
     *
     * @author Hidayath
     * @since 22/05/2021
     */
    public String returnJsonAsString(File jsonFile) {
        String jsonString = "";
        try {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(jsonFile));
            jsonString = gson.fromJson(reader, JsonObject.class).toString();
        } catch (Exception e) {
            System.out.println("Exception reading the json file:" + e.getMessage());
        }
        return jsonString;

    }
}
