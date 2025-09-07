package homework.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import homework.models.AuthBodyModel;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

public class ProfilePage {
    public SelenideElement
            userName = $("#userName-value"),
            confirmDeleteButton = $("#closeSmallModal-ok"),
            tableBooks = $(".rt-noData"),
            bookTitle = $(".rt-tbode .rt-tr-group:first-child .rt-td:nth-child(2)");
    public ElementsCollection
            bookNames = $$(".mr-2");
    public String
            deletedRow = ".rt-tr",
            binIcon = "#delete-record-undefined";

    @Step("Открыть страницу пользователя")
    public ProfilePage openPage(AuthBodyModel userData) {
        open("/profile");
        userName.shouldHave(text(userData.getUserName()));

        return this;
    }

    @Step("Закрыть банеры")
    public ProfilePage removeAds() {
        executeJavaScript("$('footer').remove();");
        executeJavaScript("$('#fixedban').remove();");

        return this;
    }

    @Step("Удалить книгу из коллекции")
    public ProfilePage clickOnBinIcon(String deletedBookTitle) {
        bookNames.findBy(text(deletedBookTitle)).closest(deletedRow).$(binIcon).click();

        return this;
    }

    @Step("Подтвердить удаление книги из коллекции")
    public ProfilePage clickOnConfirmDeleteButton() {
        confirmDeleteButton.click();

        return this;
    }

    @Step("Закрыть окно подтверждения")
    public ProfilePage closeConfirmationWindow() {
        Selenide.confirm();

        return this;
    }

    @Step("Проверяем, что коллекция пустая")
    public ProfilePage checkCollectionEmpty() {
        tableBooks.shouldHave(text("No rows found"));

        return this;
    }

    @Step("Проверяем, что нет книгги с названием Git Pocket Guide")
    public ProfilePage checkBookExists() {
        bookTitle.shouldHave(text("Git Pocket Guide"));

        return this;
    }
}