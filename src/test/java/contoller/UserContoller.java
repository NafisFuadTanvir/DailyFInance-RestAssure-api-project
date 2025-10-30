package contoller;

import config.UserModel;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.Properties;

import static io.restassured.RestAssured.given;

public class UserContoller {
    Properties prop;
    public UserContoller(Properties prop){
        RestAssured.baseURI="https://dailyfinanceapi.roadtocareer.net";
        this.prop=prop;

    }

    public Response adminLogin(UserModel userModel){

        Response res= given().contentType("application/json").body(userModel)
                .when().post("/api/auth/login");
        return  res;
    }
    public Response createUser(UserModel userModel){
        Response res= given().contentType("application/json").header("Authorization","Bearer "+prop.getProperty("admintoken")).
                body(userModel).when().post("/api/auth/register");
        return  res;


    }
    public Response getUserList(){
        Response res=given().contentType("application/json").header("Authorization","Bearer "+prop.getProperty("admintoken")).when()
                .get("/api/user/users");
        return res;

    }

    public Response getUserById(String userId){
        Response res=given().contentType("application/json").header("Authorization","Bearer "+prop.getProperty("admintoken")).when()
                .get("/api/user/"+userId);
        return  res;

    }

    public Response useLogin(UserModel userModel) {
        Response res= given().contentType("application/json").body(userModel)
                .when().post("/api/auth/login");

        return res;

    }

    public Response editUserInfo(UserModel userModel, String userId) {
        Response res = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + prop.getProperty("usertoken"))
                .body(userModel)
                .when()
                .put("/api/user/"+userId);
        return  res;

    }

    public Response addItem(ItemController itemController){
        Response res= given().contentType("application/json").header("Authorization","Bearer "+prop.getProperty("usertoken")).when()
                .body(itemController)
                .when().post("/api/costs");
        return  res;

    }

    public Response getItemList(){
        Response res=given().contentType("application/json").header("Authorization","Bearer "+prop.getProperty("usertoken")).when()
                .get("/api/costs");

        return  res;
    }

    public Response editItem(ItemController itemController){
        Response res = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + prop.getProperty("usertoken"))
                .body(itemController)
                .when()
                .put("/api/costs/" + prop.getProperty("itemId"));
        return  res;

    }

    public Response deleteItem(){
        Response res = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + prop.getProperty("usertoken"))
                .when()
                .delete("/api/costs/" + prop.getProperty("itemId"));

        return res;

    }






}
