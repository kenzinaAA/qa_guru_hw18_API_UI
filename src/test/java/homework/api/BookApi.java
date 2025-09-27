package homework.api;

import homework.models.*;

import static io.restassured.RestAssured.given;
import static homework.specs.BaseSpec.requestSpec;
import static homework.specs.BaseSpec.responseSpec;

public class BookApi {

    public AddBooksResponseModel bookAdd(AddBooksBodyModel bookData, AuthResponseModel loginResponse) {
        return given(requestSpec)
                .header("Authorization", "Bearer " + loginResponse.getToken())
                .body(bookData)
                .when()
                .post("/BookStore/v1/Books")
                .then()
                .spec(responseSpec(201))
                .extract().as(AddBooksResponseModel.class);
    }

}