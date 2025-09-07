package homework.api;

import java.util.ArrayList;
import java.util.List;

import homework.models.*;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static homework.specs.BaseSpec.requestSpec;
import static homework.specs.BaseSpec.responseSpec;

public class BookApi {

    String testBook;

    public BookApi(String testBook) {
        this.testBook = testBook;
    }

    public void addBookToISBNCollection(AddBooksBodyModel bookData, AuthResponseModel loginResponse) {
        bookData.setUserId(loginResponse.getUserId());
        List<CollectionOfIsbnsModel> isbnList = new ArrayList<>();
        CollectionOfIsbnsModel isbnFirst = new CollectionOfIsbnsModel();
        isbnFirst.setIsbn(testBook);
        isbnList.add(isbnFirst);
        bookData.setCollectionOfIsbns(isbnList);
    }

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

    public void booksCheck(AddBooksResponseModel bookResponse) {
        assertEquals(testBook, bookResponse.getBooks().get(0).getIsbn());
    }
}