package StepDefinition;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.junit.Assert;

import static io.restassured.RestAssured.given;

public class Steps {
    Response response;

    public void prerequisite() {
        RestAssured.baseURI="http://localhost:7071";
        RestAssured.defaultParser = Parser.JSON;
    }

    @Given("Create a contact (.*)$")
    public void createAContact(Long phoneNumber){
        prerequisite();

        System.out.println("createAContact() - phoneNumber = " + phoneNumber);

        String contactBody = "{\n" +
                "    \"phoneNumber\" : " + phoneNumber + ",\n" +
                "    \"userName\" : \"User Name\",\n" +
                "    \"phNumberType\" : \"HOME\"\n" +
                "}";

        response = given()
                .header("Content-Type","application/json")
                .log()
                .all()
                .when()
                .body(contactBody)
                .post("/api/create_contact")
                .then()
                .extract()
                .response();
    }

    @Given("Create an empty phone number contact")
    public void createAContact(){
        prerequisite();

        String contactBody = "{\n" +
                "    \"phoneNumber\" : \"\",\n" +
                "    \"userName\" : \"User Name\",\n" +
                "    \"phNumberType\" : \"HOME\"\n" +
                "}";

        response = given()
                .header("Content-Type","application/json")
                .log()
                .all()
                .when()
                .body(contactBody)
                .post("/api/create_contact")
                .then()
                .extract()
                .response();
    }

    @Then("Assert created contact (.*)$")
    public void assertCreatedContact(Long phoneNumber){
        String responseStr = response.getBody().prettyPrint();
        JsonObject jsonObject = new Gson().fromJson(responseStr, JsonObject.class);
        Assert.assertEquals("Phone Number Mismatch", phoneNumber.longValue(), Long.parseLong(jsonObject.get("phoneNumber").getAsString()));
    }

    @Then("Assert already created contact")
    public void assertAlreadyCreatedContact(){
        String responseStr = response.getBody().prettyPrint();
        Assert.assertEquals("Contact should be available.", "Contact already created", responseStr);
    }

    @Then("Assert contact creation failed")
    public void assertContactCreationFailed(){
        String responseStr = response.getBody().prettyPrint();
        Assert.assertEquals("Contact should not be available.", "Contact creation failed", responseStr);
    }

    @When("Modify created contact (.*)$")
    public void modifyCreatedContact(Long phoneNumber){

        String modifyContactBody = "{\n" +
                "    \"phoneNumber\" : "+ phoneNumber + ",\n" +
                "    \"userName\" : \"Modified Name\",\n" +
                "    \"phNumberType\" : \"WORK\"\n" +
                "}";

        response = given()
                .header("Content-Type","application/json")
                .log()
                .all()
                .when()
                .body(modifyContactBody)
                .put("/api/" + phoneNumber)
                .then()
                .extract()
                .response();
    }

    @When("Modify created contact with empty phone number")
    public void modifyCreatedContactWithEmptyNumber(Long phoneNumber){

        String modifyContactBody = "{\n" +
                "    \"phoneNumber\" : \"\",\n" +
                "    \"userName\" : \"Modified Name\",\n" +
                "    \"phNumberType\" : \"WORK\"\n" +
                "}";

        response = given()
                .header("Content-Type","application/json")
                .log()
                .all()
                .when()
                .body(modifyContactBody)
                .put("/api/" + phoneNumber)
                .then()
                .extract()
                .response();
    }

    @Then("Assert modified contact")
    public void assertModifiedContact(){
        String responseStr = response.getBody().prettyPrint();
        JsonObject jsonObject = new Gson().fromJson(responseStr, JsonObject.class);
        Assert.assertEquals("Phone Number Mismatch", "Modified Name", jsonObject.get("userName").getAsString());
    }

    @Then("Assert contact modification failed")
    public void assertContactModificationFailed(){
        String responseStr = response.getBody().prettyPrint();
        Assert.assertEquals("Expected Phone Number Mismatch for modification", "Contact modification failed", responseStr);
    }

    @Then("Assert modify contact which doesn't exists")
    public void assertModifyWhenContactDoesntExist(){
        String responseStr = response.getBody().prettyPrint();
        Assert.assertEquals("Phone Number Mismatch", "Contact not found", responseStr);
    }

    @When("Delete created contact (.*)$")
    @When("Delete a contact which was not created (.*)$")
    public void deleteCreatedContactAndAssert(Long phoneNumber){
        response = given()
                .header("Content-Type","application/json")
                .log()
                .all()
                .when()
                .delete("/api/" + phoneNumber)
                .then()
                .extract()
                .response();

    }

    @Then("Assert deleted contact")
    public void assertDeletedContact(){
        Assert.assertEquals("Should be Null", "", response.body().print());
    }

    @Then("Assert delete contact which doesn't exists")
    @Then("Assert already deleted contact")
    public void assertAlreadyDeletedContact(){
        String responseStr = response.getBody().prettyPrint();
        Assert.assertEquals("Contact should be available.", "Contact not found", responseStr);
    }

    @Then("Assert contact deletion failed")
    public void assertContactDeletionFailed(){
        String responseStr = response.getBody().prettyPrint();
        Assert.assertEquals("Expected Phone Number Mismatch for deletion", "Contact deletion failed", responseStr);
    }

}
