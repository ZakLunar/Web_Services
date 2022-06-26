import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;

public class RestAssuredTest {

    public static final String BASE_URL = "https://petstore.swagger.io/v2";
    public static final String PET_ID = "2020";

    @Test
    public void getAllPetsWithSoldStatus() {
        RestAssured.get(BASE_URL + "/pet/findByStatus?status=sold")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body("status", hasItems("sold"));
    }


    @Test (priority = 1)
    public void createNewPet() {
        given()
                .header("Content-type", "application/json")
                .body(requestBody)
                .when().post(BASE_URL + "/pet")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body("name", equalTo("Mark"))
                .body("status", equalTo("available"));
    }

    private final static String requestBody =
            "{" +
                    " \"name\": \"Mark\", " +
                    " \"status\": \"available\", " +
                    " \"id\": \"2020\" " +
                    "}";

    @Test (priority = 2)
    public void getPetByID() {
        RestAssured.get(BASE_URL + "/pet/" + PET_ID)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body("name", equalTo("Mark"))
                .body("status", equalTo("available"));
    }

    @Test (priority = 3)
    public void updatePetWithFormData() {
        given()
                .formParam("name", "New Mark")
                .formParam("status", "sold")
                .when().post(BASE_URL + "/pet/" + PET_ID)
                .then()
                .statusCode(HttpStatus.SC_OK);
        RestAssured.get(BASE_URL + "/pet/" + PET_ID)
                .then().assertThat()
                .body("name", equalTo("New Mark"))
                .body("status", equalTo("sold"));
    }

    @Test (priority = 4)
    public void deletePetByIdFromDB() {
        RestAssured.delete(BASE_URL + "/pet/" + PET_ID)
                .then().statusCode(HttpStatus.SC_OK);
        RestAssured.get(BASE_URL + "/pet/" + PET_ID)
                .then().statusCode(HttpStatus.SC_NOT_FOUND)
                .assertThat()
                .body("message", equalTo("Pet not found"));
    }

}