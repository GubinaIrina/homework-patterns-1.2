package ru.netology;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private DataGenerator() {
    }

    public static String generateInalidLogin() {
        FakeValuesService fakeValuesService = new FakeValuesService(new Locale("eng"), new RandomService());
        return fakeValuesService.bothify("******");
    }

    public static String generateInalidPassword() {
        FakeValuesService fakeValuesService = new FakeValuesService(new Locale("eng"), new RandomService());
        return fakeValuesService.bothify("******");
    }

    public static RegistrationByLogin generateValidUser() {
        Faker faker = new Faker(new Locale("eng"));
        String login = faker.internet().emailAddress();
        String password = faker.internet().password();
        String status = "active";
        createUser(new RegistrationByLogin(login, password, status));
        return new RegistrationByLogin(login, password, status);
    }

    public static RegistrationByLogin generateBlockedUser() {
        Faker faker = new Faker(new Locale("eng"));
        String login = faker.internet().emailAddress();
        String password = faker.internet().password();
        String status = "blocked";
        createUser(new RegistrationByLogin(login, password, status));
        return new RegistrationByLogin(login, password, status);
    }

    public static RegistrationByLogin generateNonValidLogin() {
        Faker faker = new Faker(new Locale("eng"));
        String login = faker.internet().emailAddress();
        String password = faker.internet().password();
        String status = "active";
        createUser(new RegistrationByLogin(login, password, status));
        return new RegistrationByLogin(generateInalidLogin(), password, status);
    }

    public static RegistrationByLogin generateNonValidPassword() {
        Faker faker = new Faker(new Locale("eng"));
        String login = faker.internet().emailAddress();
        String password = faker.internet().password();
        String status = "active";
        createUser(new RegistrationByLogin(login, password, status));
        return new RegistrationByLogin(login, generateInalidPassword(), status);
    }

    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static void createUser(RegistrationByLogin registrationByLogin) {
        given()
                .spec(requestSpec)
                .body(registrationByLogin)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }
}
