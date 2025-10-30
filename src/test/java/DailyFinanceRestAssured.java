import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.configuration.ConfigurationException;
import org.junit.jupiter.api.Test;
import utils.Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class DailyFinanceRestAssured {
    Properties prop;


    public DailyFinanceRestAssured() throws IOException {
        prop= new Properties();
        FileInputStream fs= new FileInputStream("./src/test/resources/config.properties");
        prop.load(fs);

    }

    @Test
    public void adminLogin() throws ConfigurationException {
        RestAssured.baseURI="https://dailyfinanceapi.roadtocareer.net";
        Response res= given().contentType("application/json").body("{\"email\":\"admin@test.com\",\"password\":\"admin123\"}")
                .when().post("/api/auth/login");
//        System.out.println(res.asString());

        //extract token from jsonObj
        JsonPath jsonObj= res.jsonPath();
        String admintoken=jsonObj.get("token");
        System.out.println(admintoken);
        Utils.setEnv("admintoken",admintoken);
    }

    @Test
    public void createUser() throws ConfigurationException {
        RestAssured.baseURI="https://dailyfinanceapi.roadtocareer.net";
        Response res= given().contentType("application/json").header("Authorization","Bearer "+prop.getProperty("admintoken")).
                body("{\"firstName\":\"one\",\"lastName\":\"two\",\"email\":\"email11@gmail.com\",\"" +
                        "password\":\"12345\",\"phoneNumber\":\"01325\",\"address\":\"Chittagong\"," +
                        "\"gender\":\"Male\",\"termsAccepted\":true}").when().post("/api/auth/register");
        System.out.println(res.asString());



    }
@Test
    public void getUserList(){
        RestAssured.baseURI="https://dailyfinanceapi.roadtocareer.net";
        Response res=given().contentType("application/json").header("Authorization","Bearer "+prop.getProperty("admintoken")).when()
                .get("/api/user/users");
        System.out.println(res.asString());
    }
@Test
    public void getUserById(){
        RestAssured.baseURI="https://dailyfinanceapi.roadtocareer.net";
        Response res=given().contentType("application/json").header("Authorization","Bearer "+prop.getProperty("admintoken")).when()
                .get("/api/user/a5187f1f-b3c0-476d-b49a-f5b87da1ff4b");
        System.out.println(res.asString());

    }

@Test
    public void userLogin() throws ConfigurationException {
        RestAssured.baseURI="https://dailyfinanceapi.roadtocareer.net";
        Response res= given().contentType("application/json").body("{\"email\":\"onetwo5@gmail.com\",\"password\":\"12345\"}")
                .when().post("/api/auth/login");
        //extract token from jsonObj
        JsonPath jsonObj= res.jsonPath();
        String userToken=jsonObj.get("token");
        System.out.println(userToken);
        Utils.setEnv("userToken",userToken);

    }

    @Test
    public void editUserInfo() {
        RestAssured.baseURI = "https://dailyfinanceapi.roadtocareer.net";

        Response res = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + prop.getProperty("userToken"))
                .body("{\n" +
                        "  \"firstName\": \"one223edit\",\n" +
                        "  \"phoneNumber\": \"013252555\"\n" +
                        "}")
                .when()
                .put("/api/user/a5187f1f-b3c0-476d-b49a-f5b87da1ff4b");

        System.out.println("Response: " + res.asPrettyString());
    }

@Test
    public void addItem() throws ConfigurationException {
        RestAssured.baseURI = "https://dailyfinanceapi.roadtocareer.net";
        Response res= given().contentType("application/json").header("Authorization","Bearer "+prop.getProperty("userToken")).when()
                .body("{\"itemName\":\"addingitem2\",\"quantity\":1,\"amount\":\"479\",\"purchaseDate\":\"2025-10-28\",\"month\":\"October\",\"remarks\":\"\"}")
                .when().post("/api/costs");
    System.out.println("Response: " + res.asPrettyString());

    JsonPath jsonObj= res.jsonPath();
    String itemId=jsonObj.get("_id");

    // ✅ Save the ID to config.properties
    Utils.setEnv("itemId", itemId);
    }

    @Test
    public void getItemList(){
        RestAssured.baseURI = "https://dailyfinanceapi.roadtocareer.net";
        Response res=given().contentType("application/json").header("Authorization","Bearer "+prop.getProperty("userToken")).when()
                .get("/api/costs");
        System.out.println("Response: " + res.asPrettyString());

    }

    @Test
    public void editItem(){
        RestAssured.baseURI = "https://dailyfinanceapi.roadtocareer.net";

        Response res = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + prop.getProperty("userToken"))
                .body("{\"itemName\": \"edited name\",\n" +
                        "        \"quantity\": 1,\n" +
                        "        \"amount\": 4759,\n" +
                        "        \"purchaseDate\": \"2025-10-28T00:00:00.000Z\",\n" +
                        "        \"month\": \"October\",\n" +
                        "        \"remarks\": \"\"}")
                .when()
                .put("/api/costs/" + prop.getProperty("itemId"));

        System.out.println("Response: " + res.asPrettyString());
    }

    @Test
    public void deleteItem(){
        RestAssured.baseURI = "https://dailyfinanceapi.roadtocareer.net";
        Response res = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + prop.getProperty("userToken"))
                .when()
                .delete("/api/costs/" + prop.getProperty("itemId"));

        System.out.println("Response: " + res.asPrettyString());

    }





}
