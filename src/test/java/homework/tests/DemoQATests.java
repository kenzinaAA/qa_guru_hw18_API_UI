package homework.tests;

import homework.api.BookApi;
import homework.api.LoginApi;
import homework.models.*;
import org.junit.jupiter.api.Test;
import homework.ui.DeleteUI;

import java.util.ArrayList;
import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class DemoQATests extends TestBase {

    String testBookIsbn = "9781449325862";

    public void addBookToISBNCollection(AddBooksBodyModel bookData, AuthResponseModel loginResponse, String isbn) {
        bookData.setUserId(loginResponse.getUserId());
        List<CollectionOfIsbnsModel> isbnList = new ArrayList<>();
        CollectionOfIsbnsModel isbnFirst = new CollectionOfIsbnsModel();
        isbnFirst.setIsbn(isbn);
        isbnList.add(isbnFirst);
        bookData.setCollectionOfIsbns(isbnList);
    }

    public void booksCheck(AddBooksResponseModel bookResponse, String isbn) {
        assertEquals(isbn, bookResponse.getBooks().get(0).getIsbn());
    }

    public void loginCheck(AuthBodyModel userData, AuthResponseModel loginResponse) {
        assertEquals(userData.getUserName(), loginResponse.getUsername());
        assertEquals("690e76f9-4d4b-42a4-8655-019f7041345e", loginResponse.getUserId());
    }

    public void usersBookListCheck(AuthBodyModel userData, AuthResponseModel loginResponse, String testBook, ListOfBooksResponseModel userBookResponse) {
        assertEquals(loginResponse.getUserId(), userBookResponse.getUserId());
        assertEquals(userData.getUserName(), userBookResponse.getUsername());
        List<Book> bookList = userBookResponse.getBooks();
        for (Book each : bookList) {
            assertNotEquals(each.getIsbn(), testBook);
        }
    }

    @Test
    public void deleteOneOfItemsTest() {
        AuthBodyModel userData = new AuthBodyModel(user, password);
        LoginApi loginApi = new LoginApi();

        BookApi bookApi = new BookApi();
        AddBooksBodyModel bookData = new AddBooksBodyModel();

        DeleteUI deleteUI = new DeleteUI();

        AuthResponseModel loginResponse = step("Отправляем данные на авторизацию", () ->
                loginApi.login(userData));

        step("Авторизация прошла успешно", () -> {
            loginCheck(userData, loginResponse);
        });

        addBookToISBNCollection(bookData, loginResponse, testBookIsbn);

        AddBooksResponseModel bookResponse = step("Добавляем книгу пользователю", () ->
                bookApi.bookAdd(bookData, loginResponse));

        step("Проверяем, что книга добавлена в коллекцию", () -> {
            booksCheck(bookResponse, testBookIsbn);
        });

        step("Удаляем книгу из коллекции через UI", () -> {
            deleteUI.deleteBookWithUI(loginResponse, userData, bookResponse);
        });

        ListOfBooksResponseModel userBookResponse = step("Отправляем запрос на получение коллеции книг пользователя", () ->
                loginApi.getUserBookResponse(loginResponse));

        step("Подтверждаем удаление по API", () -> {
            usersBookListCheck(userData, loginResponse, testBookIsbn, userBookResponse);
        });
    }
}