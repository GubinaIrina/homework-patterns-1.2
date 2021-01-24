package ru.netology;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;


public class IBankTestUserActive {

    @BeforeEach
    void openWeb() {
        open("http://localhost:9999");
    }

    @Test
    void shouldLogIfUserActive() {
        val registrationByLogin = DataGenerator.generateValidUser();
        $("[data-test-id='login'] input").setValue(registrationByLogin.getLogin());
        $("[data-test-id='password'] input").setValue(registrationByLogin.getPassword());
        $("[data-test-id='action-login']").click();

        $("div").shouldHave(exactText("  Личный кабинет"));
    }

    @Test
    void shouldNotLogNonValidLogin() {
        val registrationByLogin = DataGenerator.generateNonValidLogin();
        $("[data-test-id='login'] input").setValue(registrationByLogin.getLogin());
        $("[data-test-id='password'] input").setValue(registrationByLogin.getPassword());
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']").waitUntil(visible, 10000)
                .shouldHave(exactText("Ошибка Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldNotLogNonValidPassword() {
        val registrationByLogin = DataGenerator.generateNonValidPassword();
        $("[data-test-id='login'] input").setValue(registrationByLogin.getLogin());
        $("[data-test-id='password'] input").setValue(registrationByLogin.getPassword());
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']").waitUntil(visible, 10000)
                .shouldHave(exactText("Ошибка Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldNotLogIfUserBlocked() {
        val userBlocked = DataGenerator.generateBlockedUser();
        $("[data-test-id='login'] input").setValue(userBlocked.getLogin());
        $("[data-test-id='password'] input").setValue(userBlocked.getPassword());
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']").waitUntil(visible, 10000)
                .shouldHave(exactText("Ошибка Ошибка! Пользователь заблокирован"));
    }
}
