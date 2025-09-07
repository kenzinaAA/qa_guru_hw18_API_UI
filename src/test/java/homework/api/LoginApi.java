package homework.api;

import homework.models.*;

import java.util.List;

import static homework.specs.BaseSpec.requestSpec;
import static homework.specs.BaseSpec.responseSpec;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

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

    public void loginCheck(AuthBodyModel userData, AuthResponseModel loginResponse) {
        assertEquals(userData.getUserName(), loginResponse.getUsername());
        assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyTmFtZSI6ImtlbnppbmFfYWEiLCJwYXNzd29yZCI6IiFTaW5pY2hrYTk5OSIsImlhdCI6MTc1NzE4MTE3Nn0.GAYGiPRgrwY_B5A8E4ZtjAwGCRHWSqpQ31Ob9W3QBsw", loginResponse.getToken());
        assertEquals("690e76f9-4d4b-42a4-8655-019f7041345e", loginResponse.getUserId());
    }

    public GetListOfBooksResponseModel getUserBookResponse(AuthResponseModel loginResponse) {
        return given(requestSpec)
                .header("Authorization", "Bearer " + loginResponse.getToken())
                .when()
                .get("/Account/v1/User/" + loginResponse.getUserId())
                .then()
                .spec(responseSpec(200))
                .extract().as(GetListOfBooksResponseModel.class);
    }

    public void usersBookListCheck(AuthBodyModel userData, AuthResponseModel loginResponse, String testBook, GetListOfBooksResponseModel userBookResponse) {
        assertEquals(loginResponse.getUserId(), userBookResponse.getUserId());
        assertEquals(userData.getUserName(), userBookResponse.getUsername());
        List<Book> bookList = userBookResponse.getBooks();
        for (Book each : bookList) {
            assertNotEquals(each.getIsbn(), testBook);
        }
    }
}