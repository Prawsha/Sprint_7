package ru.qa_scooter;

import io.restassured.RestAssured;
import org.junit.Before;
import static ru.qa_scooter.CourierSteps.*;
import static ru.qa_scooter.UsefulData.*;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

public class CourierCreateTest {

    private String login;
    private String password;
    private String firstName;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;

        login = generateRandomLogin();
        password = generateRandomPassword();
        firstName = generateRandomFirstName();
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Проверяем: курьер создан")
    public void testCreateCourierSuccess() {
        Response response = createCourierRequest(login, password, firstName);
        validateResponseOk(response, 201, true);
    }

    @Test
    @DisplayName("Дублировать курьера")
    @Description("Проверяем: нельзя создать дубль курьера")
    public void testCreateDuplicateCourier() {
        createCourierRequest(login, password, firstName);
        Response response = createCourierRequest(login, password, firstName);
        validateResponseMistake(response, 409, "Логин занят.");
    }

    @Test
    @DisplayName("Дублировать курьера, другие имя и пароль")
    @Description("Проверяем: нельзя создать дубль курьера с другими паролем и именем")
    public void testCreateCourierWithExistingLogin() {
        createCourierRequest(login, password, firstName);
        Response response = createCourierRequest(login, password+1, firstName+1);
        validateResponseMistake(response, 409, "Логин занят.");
    }

    @Test
    @DisplayName("Создать курьера без логина")
    @Description("Проверяем: нельзя создать курьера без логина")
    public void testCreateCourierWithoutLogin() {
        Response response = createCourierRequest("", password, firstName);
        validateResponseMistake(response, 400, "Для создания учетной записи нужен логин.");
    }

    @Test
    @DisplayName("Создать курьера без пароля")
    @Description("Проверяем: нельзя создать курьера без пароля")
    public void testCreateCourierWithoutPassword() {
        Response response = createCourierRequest(login, "", firstName);
        validateResponseMistake(response, 400, "Для создания учетной записи нужен пароль.");
    }

    @Test
    @DisplayName("Создать курьера без имени")
    @Description("Проверяем: имя - необязательное поле, учетная запись создана.")
    public void testCreateCourierWithoutFirstName() {
        Response response = createCourierRequest(login, password, "");
        validateResponseOk(response, 201, true);
    }

    @After
    public void deleteData() {
        deleteCourier();
    }

}