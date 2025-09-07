package homework.tests;

import homework.api.BookApi;
import homework.api.LoginApi;
import homework.models.*;
import homework.pages.ProfilePage;
import org.junit.jupiter.api.Test;
import homework.ui.DeleteUI;

import static io.qameta.allure.Allure.step;
import static homework.tests.TestData.PASSWORD;
import static homework.tests.TestData.USERNAME;

public class DemoQATests extends TestBase {

    String testBookIsbn = "9781449325862";

    @Test
    public void deleteOneOfItemsTest() {
        AuthBodyModel userData = new AuthBodyModel(USERNAME, PASSWORD);
        LoginApi loginApi = new LoginApi();

        BookApi bookApi = new BookApi(testBookIsbn);
        AddBooksBodyModel bookData = new AddBooksBodyModel();

        DeleteUI deleteUI = new DeleteUI();

        AuthResponseModel loginResponse = step("Отправляем данные на авторизацию", () ->
                loginApi.login(userData));

        step("Авторизация прошла успешно", () -> {
            loginApi.loginCheck(userData, loginResponse);
        });

        bookApi.addBookToISBNCollection(bookData, loginResponse);
        AddBooksResponseModel bookResponse = step("Добавляем книгу пользователю", () ->
                bookApi.bookAdd(bookData, loginResponse));

        step("Проверяем, что книга добавлена в коллекцию", () -> {
            bookApi.booksCheck(bookResponse);
        });

        step("Удаляем книгу из коллекции через UI", () -> {
            deleteUI.DeleteBookWithUI(loginResponse, userData, bookResponse);
        });

        GetListOfBooksResponseModel userBookResponse = step("Отправляем запрос на получение коллеции книг пользователя", () ->
                loginApi.getUserBookResponse(loginResponse));

        step("Подтверждаем удаление по API", () -> {
            loginApi.usersBookListCheck(userData, loginResponse, testBookIsbn, userBookResponse);
        });
    }
}