# RestAPITestExample
This is just a basic Example framework which uses JUnit, RestAssured with Cucumber.

### STEPS TO CREATE YOUR OWN CUCUMBER FRAMEWORK

1 <br>
**Create a new Project in Intellij:(Or another IDE)** <br>
File > New > Project > Maven Project > Enter Name of your project eg: RestAPITestExample > Click on Finish
 <br>
 <br>
2  <br>
**Installation:**<br>
Select the latest version here and Add maven dependency in the pom.xml file<br>
cucumber-java  : https://mvnrepository.com/artifact/io.cucumber/cucumber-java <br>
cucumber-junit : https://mvnrepository.com/artifact/io.cucumber/cucumber-junit <br>
cucumber-core : https://mvnrepository.com/artifact/io.cucumber/cucumber-core <br>
<br>
3 <br>
Create 2 folders inside java directory.
- Features and <br>
- StepDefinition<br>
 <br>
 
4 <br>
Create a new file under Features folder with the file name - example.feature and add the Steps.<br>
Example content is given here: <br>

```
Feature: ContactExample

@Regression @UNIQUE-PerformContactCreateandDelete
Scenario: Perform Contact Create and Delete
  Given Create a contact 98765432101
  Then Assert created contact 98765432101
  And Delete created contact 98765432101
  And Assert deleted contact
```
 <br>
5 <br>
Create new java file inside StepDefinition folder with the name - Steps.java and add definition for the mentioned steps.<br>
Example content of the java file : <br>

```
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

    @Then("Assert created contact (.*)$")
    public void assertCreatedContact(Long phoneNumber){
        String responseStr = response.getBody().prettyPrint();
        JsonObject jsonObject = new Gson().fromJson(responseStr, JsonObject.class);
        Assert.assertEquals("Phone Number Mismatch", phoneNumber.longValue(), Long.parseLong(jsonObject.get("phoneNumber").getAsString()));
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

    @Then("Assert modified contact")
    public void assertModifiedContact(){
        String responseStr = response.getBody().prettyPrint();
        JsonObject jsonObject = new Gson().fromJson(responseStr, JsonObject.class);
        Assert.assertEquals("Phone Number Mismatch", "Modified Name", jsonObject.get("userName").getAsString());
    }

    @When("Delete created contact (.*)$")
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

}
```

 <br>
6 <br>
We have feature file and we have step definition ready now we need to mp them.<br>
We need to use @CucumberOptions to map these 2 above things.<br>
Example content is added here:<br>

```
package Runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features="src/test/java/Features",
        glue={"StepDefinition"},
        plugin = {"pretty", "html:target/cucumber-reports.html"})
public class RunTest
{

}
```
 
 <br>
Perfect, This framework is now ready to run.<br>
<br>
7 <br>

- Simple right click on the file which we created just now > and Click on Run 'RunTest' option.<br>

- To run from command line, use the following command :<br>

```java -cp <CLASS_PATH> cucumber.api.cli.Main features```
 <br><br>
- To use maven command , use the following command :<br>

```mvn clean install ```
 <br>
<br>
CLASS_PATH all the dependency jars should be mentioned.<br>
 <br>
This will execute the feature file and will create the HTML report in target/cucumber-reports.html
 <br> <br>
To automate more cases, We just have to add more files in the Features folder and corresponding Step definition in StepDefinition folder.

### Please refer this link for RestAPI Jar for the above test cases
https://github.com/shreyas18jan/RestAPIExample
<br>
<br>
<br>
<br>