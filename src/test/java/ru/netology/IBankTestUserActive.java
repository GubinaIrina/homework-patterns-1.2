package ru.netology;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;

public class IBankTestUserActive {
    private Faker faker;

    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    @BeforeAll
    static void setUpAll() {
        given()
                .spec(requestSpec)
                .body(new RegistrationByLogin("vasya", "password", "active"))
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    @BeforeEach
    void openWeb() {
        faker = new Faker(new Locale("eng"));
        open("http://localhost:9999");
    }

    @Test
    void shouldLogIfExists() {
        $("[data-test-id='login'] input").setValue("vasya");
        $("[data-test-id='password'] input").setValue("password");
        $("[data-test-id='action-login']").click();

        $("div").shouldHave(exactText("  Личный кабинет"));
    }

    @Test
    void shouldNotLog() {
        $("[data-test-id='login'] input").setValue(faker.internet().emailAddress());
        $("[data-test-id='password'] input").setValue(faker.internet().password());
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']").waitUntil(visible, 10000)
                .shouldHave(exactText("Ошибка Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldNotLogNonValidLogin() {
        $("[data-test-id='login'] input").setValue(faker.internet().emailAddress());
        $("[data-test-id='password'] input").setValue("password");
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']").waitUntil(visible, 10000)
                .shouldHave(exactText("Ошибка Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldNotLogNonValidPassword() {
        $("[data-test-id='login'] input").setValue("vasya");
        $("[data-test-id='password'] input").setValue(faker.internet().password());
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']").waitUntil(visible, 10000)
                .shouldHave(exactText("Ошибка Ошибка! Неверно указан логин или пароль"));
    }
}