package ru.qa_scooter;

import io.restassured.RestAssured;
import org.junit.Before;
import static ru.qa_scooter.CourierSteps.*;
import static ru.qa_scooter.UsefulData.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import io.qameta.allure.Description;

public class CourierLoginTest {

    private String login;
    private String password;
    private String firstName;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;

        login = generateRandomLogin();
        password = generateRandomPassword();
        firstName = generateRandomFirstName();

        createCourierRequest(login, password, firstName);
    }

    @Test
    @DisplayName("Успешная авторизация")
    @Description("Проверяем: успешная авторизация курьера с правильными кредами")
    public void testCourierLoginSuccess() {
        Response response = loginCourierRequest(login, password);
        loginResponseOk(response); // Проверка, что при авторизации возвращается непустой id
    }

    @Test
    @DisplayName("Авторизация с неправильным паролем")
    @Description("Проверяем: ошибка авторизации, если пароль неправильный")
    public void testCourierLoginWrongPassword() {
        Response response = loginCourierRequest(login, "wrong_password");
        loginResponseMistake(response, 404, "Ошибка авторизации, проверьте данные.");
    }

    @Test
    @DisplayName("Авторизация с неправильным логином")
    @Description("Проверяем: ошибка авторизации, если логин неправильный")
    public void testCourierLoginWrongLogin() {
        Response response = loginCourierRequest("wrong_login", password);
        loginResponseMistake(response, 404, "Ошибка авторизации, проверьте данные.");
    }

    @Test
    @DisplayName("Авторизация - пустой логин")
    @Description("Проверяем: ошибка авторизации, если нет логина")
    public void testCourierLoginWithoutLogin() {
        Response response = loginCourierRequest("", password);
        loginResponseMistake(response, 400, "Ошибка авторизации, недостаточно данных.");
    }

    @Test
    @DisplayName("Авторизация - пустой пароль")
    @Description("Проверяем: ошибка авторизации, если нет пароля")
    public void testCourierLoginWithoutPassword() {
        Response response = loginCourierRequest(login, "");
        loginResponseMistake(response, 400, "Ошибка авторизации, недостаточно данных.");
    }

    @Test
    @DisplayName("Авторизация не зарегистрированной учетки")
    @Description("Проверяем: ошибка авторизации, если нет такой учетки")
    public void testCourierLoginNonExistent() {
        Response response = loginCourierRequest("non_existent_login", "non_existent_password");
        loginResponseMistake(response, 404, "Учетная запись не существует");
    }

    @After
    public void deleteData() {
        deleteCourier();
    }

}