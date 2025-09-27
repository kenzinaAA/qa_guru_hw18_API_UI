package homework.api;

import homework.models.*;

import static homework.specs.BaseSpec.requestSpec;
import static homework.specs.BaseSpec.responseSpec;
import static io.restassured.RestAssured.given;

public class LoginApi {
    public AuthResponseModel login(AuthBodyModel userData) {
        return given(requestSpec)
                .body(userData)
                .when()
                .post("/Account/v1/Login")
                .then()
                .spec(responseSpec(200))
                .extract().as(AuthResponseModel.class);
    }

    public ListOfBooksResponseModel getUserBookResponse(AuthResponseModel loginResponse) {
        return given(requestSpec)
                .header("Authorization", "Bearer " + loginResponse.getToken())
                .when()
                .get("/Account/v1/User/" + loginResponse.getUserId())
                .then()
                .spec(responseSpec(200))
                .extract().as(ListOfBooksResponseModel.class);
    }

}