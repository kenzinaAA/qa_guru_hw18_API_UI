package homework.ui;

import homework.models.AddBooksResponseModel;
import homework.models.AuthBodyModel;
import homework.models.AuthResponseModel;
import org.openqa.selenium.Cookie;
import homework.pages.ProfilePage;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;

public class DeleteUI {
    ProfilePage profilePage = new ProfilePage();

    public void deleteBookWithUI(AuthResponseModel loginResponse, AuthBodyModel userData, AddBooksResponseModel bookResponse) {
        step("Авторизация через API", () -> {
            open("/favicon.ico");
            getWebDriver().manage().addCookie(new Cookie("userName", loginResponse.getUsername()));
            getWebDriver().manage().addCookie(new Cookie("userID", loginResponse.getUserId()));
            getWebDriver().manage().addCookie(new Cookie("token", loginResponse.getToken()));
            getWebDriver().manage().addCookie(new Cookie("expires", loginResponse.getExpires()));
        });

        profilePage.openPage(userData)
                .removeAds()
                .clickOnBinIcon("Git Pocket Guide")
                .clickOnConfirmDeleteButton()
                .closeConfirmationWindow();
    }
}