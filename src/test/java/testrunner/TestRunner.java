package testrunner;

import com.github.javafaker.Faker;
import config.Setup;
import config.UserModel;
import contoller.ItemController;
import contoller.UserContoller;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.configuration.ConfigurationException;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.Utils;

public class TestRunner extends Setup {
    Faker faker= new Faker();

  //  @Test(priority = 1,description = "admin login")
    public void adminLogin() throws ConfigurationException {
        UserContoller userContoller= new UserContoller(prop);
        UserModel userModel=new UserModel();
        userModel.setEmail("admin@test.com");
        userModel.setPassword("admin123");
        Response res= userContoller.adminLogin(userModel);

        JsonPath jsonObj= res.jsonPath();
        String admintoken=jsonObj.get("token");
        System.out.println(admintoken);
        Utils.setEnv("admintoken",admintoken);
    }
   // @Test(priority = 2)
    public void createUser() throws ConfigurationException {
        UserContoller userContoller= new UserContoller(prop);
        UserModel userModel=new UserModel();
        userModel.setFirstName(faker.name().firstName());
        userModel.setLastName(faker.name().lastName());
        userModel.setEmail(faker.internet().emailAddress().toString());
        userModel.setPassword("1234");
        userModel.setPhoneNumber("0130"+ Utils.genarateRandomNumber(1000000,9999999));
        userModel.setAddress(faker.country().capital());
        userModel.setGender("Male");
        userModel.setTermsAccepted("true");
        Response res= userContoller.createUser(userModel);

        JsonPath jsonobj=res.jsonPath();
        String userId=jsonobj.get("_id");
        String name= jsonobj.get("firstName");
        String email= jsonobj.get("email");
        String password = userModel.getPassword();


        Utils.setEnv("userId",userId);
        Utils.setEnv("name",name);
        Utils.setEnv("email",email);
        Utils.setEnv("password",password);


        Assert.assertEquals(jsonobj.get("firstName"), name);

    }

   // @Test(priority = 3)
    public  void getUserList(){
        UserContoller userContoller= new UserContoller(prop);
        UserModel userModel=new UserModel();
        Response res= userContoller.getUserList();

    }

   // @Test(priority = 4)
    public void getUserById(){
        UserContoller userContoller= new UserContoller(prop);
        Response res=userContoller.getUserById(prop.getProperty("userId"));
        System.out.println(res.asString());
        JsonPath jsonobj=res.jsonPath();
        String id = jsonobj.getString("_id");
        Assert.assertEquals(id, prop.getProperty("userId"), "User ID mismatch");

    }

   // @Test(priority = 5)
    public void userLogin() throws ConfigurationException {
        UserContoller userContoller = new UserContoller(prop);
        UserModel userModel = new UserModel();

        String email = prop.getProperty("email");
        String password = prop.getProperty("password");

        userModel.setEmail(email);
        userModel.setPassword(password);
        Response res = userContoller.useLogin(userModel);

        JsonPath jsonObj = res.jsonPath();
        String usertoken = jsonObj.getString("token");
        System.out.println(usertoken);

        Utils.setEnv("usertoken",usertoken );
    }

   // @Test(priority = 6)
    public void editUserInfoTest() throws ConfigurationException {
        UserContoller userContoller = new UserContoller(prop);
        UserModel userModel = new UserModel();

        userModel.setEmail(prop.getProperty("email"));       // or saved user email
        userModel.setPassword(prop.getProperty("password")); // or saved password
        userModel.setGender("Male");
        userModel.setTermsAccepted("true");

        // Update user info
        userModel.setFirstName("UpdatedFirstName");
        userModel.setLastName("UpdatedLastName");
        userModel.setAddress("Dhaka");
        userModel.setPhoneNumber("01301234567");
        String userId = prop.getProperty("userId");
        Response res = userContoller.editUserInfo(userModel, userId);
        res.prettyPrint();
        JsonPath json = res.jsonPath();

        // Assert the updated fields
        Assert.assertEquals(json.getString("firstName"), userModel.getFirstName(), "First name mismatch");
        Assert.assertEquals(json.getString("lastName"), userModel.getLastName(), "Last name mismatch");
        Assert.assertEquals(json.getString("address"), userModel.getAddress(), "Address mismatch");
        Assert.assertEquals(json.getString("phoneNumber"), userModel.getPhoneNumber(), "Phone number mismatch");
    }

    //@Test(priority = 7)
    public void addItem() throws ConfigurationException, InterruptedException {

        UserContoller userContoller = new UserContoller(prop);
        ItemController itemController = new ItemController();

        itemController.setItemName("Laptop");
        itemController.setQuantity("2");
        itemController.setAmount("50000");
        itemController.setPurchaseDate("2025-10-30");
        itemController.setMonth("October");
        itemController.setRemarks("Office purchase");

        Response res = userContoller.addItem(itemController);

        JsonPath jsonobj = res.jsonPath();
        String itemId = jsonobj.getString("_id");
        String itemName = jsonobj.getString("itemName");
        String amount = jsonobj.getString("amount");
        Utils.setEnv("itemId", itemId);
        Utils.setEnv("itemName", itemName);
        Utils.setEnv("amount", amount);
        Thread.sleep(1000);
        System.out.println("Item created successfully: " + itemId);
        Assert.assertEquals(itemName, itemController.getItemName(), "Item name mismatch");
    }

    //@Test(priority = 8)
    public void  getItemList(){
        UserContoller userContoller = new UserContoller(prop);
        ItemController itemController = new ItemController();
        Response res = userContoller.getItemList();
        System.out.println(res.asString());

    }
 //  @Test(priority = 9)
    public void editItem() {
        UserContoller userContoller = new UserContoller(prop);
        ItemController item = new ItemController();

        item.setItemName("Updated Laptop");
        item.setQuantity("2");
        item.setAmount("60000");
        item.setPurchaseDate("2025-10-30");
        item.setMonth("October");
        item.setRemarks("Updated remarks");

        System.out.println("Editing item ID: " + prop.getProperty("itemId"));
        System.out.println("Using token: " + prop.getProperty("usertoken"));

        Response res = userContoller.editItem(item);
        res.prettyPrint();

        Assert.assertEquals(res.statusCode(), 200, "Status code mismatch");

        JsonPath json = res.jsonPath();
        Assert.assertEquals(json.getString("itemName"), item.getItemName(), "Item name mismatch");
    }

    @Test(priority = 10)
    public void deleteItem(){
        UserContoller userContoller = new UserContoller(prop);
        ItemController itemController = new ItemController();
        Response res = userContoller.deleteItem();
        System.out.println(res.asString());
        JsonPath json = res.jsonPath();
        Assert.assertEquals(json.getString("message"), "Cost deleted successfully", "Delete message mismatch");
    }






}
