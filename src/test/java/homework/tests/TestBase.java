package homework.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import homework.helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

public class TestBase {

    @BeforeAll
    static void setup() {
        Configuration.baseUrl = "https://demoqa.com";
        RestAssured.baseURI = "https://demoqa.com";
        Configuration.pageLoadStrategy = "eager";
        Configuration.browserSize = System.getProperty("browserSize","1980x1080");
        Configuration.browser = System.getProperty("browser", "chrome");
        Configuration.browserVersion = System.getProperty("browserVersion","127.0");
        String SELENOID_URL = System.getProperty("selenoid.url");
        String SELENOID_LOGIN = System.getProperty("selenoid.login");
        String SELENOID_PASSWORD = System.getProperty("selenoid.password");
        Configuration.holdBrowserOpen = true;

        boolean isRemoteRun = SELENOID_URL != null && SELENOID_LOGIN != null && SELENOID_PASSWORD != null;

        if (isRemoteRun) {
            Configuration.remote = "https://" + SELENOID_LOGIN + ":" + SELENOID_PASSWORD + "@" + SELENOID_URL + "/wd/hub";

            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                    "enableVNC", true,
                    "enableVideo", true,
                    "videoCodec", "libx264",
                    "videoFrameRate", 24
            ));
            Configuration.browserCapabilities = capabilities;

            System.out.println("Running tests remotely in Selenoid");
        } else {
            Configuration.remote = null;
            System.out.println("Running tests locally");
        }
    }

    @BeforeEach
    void addListenerAndRuCookie() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    @AfterEach
    void addAttachments() {
        if (WebDriverRunner.hasWebDriverStarted()) {
            Attach.screenshotAs("Last screenshot");
            Attach.pageSource();
            Attach.browserConsoleLogs();
            Attach.addVideo();
        }
        Selenide.closeWebDriver();
    }
}
