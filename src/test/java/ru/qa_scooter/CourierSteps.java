package ru.qa_scooter;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static ru.qa_scooter.UsefulData.*;

public class CourierSteps {

    static String login;
    static String password;
    static String courierId;

    static String bearerToken = USER_TOKEN;

    static CourierJSON courierJSON;

    @Step("Создать курьера")
    static Response createCourierRequest(String login, String password, String firstName) {
        courierJSON = new CourierJSON(login, password, firstName);
        Response response = given()
                .filter(new AllureRestAssured())
                .auth().oauth2(bearerToken)
                .header("Content-type", "application/json")
                .body(courierJSON)
                .when()
                .post(COURIER_API);

        return response;
    }

    @Step("Проверяем ответ: ошибка создания курьера")
    static void validateResponseMistake(Response response, int expectedStatusCode, String expectedMessage) {
        response.then()
                .statusCode(expectedStatusCode)
                .body("message", equalTo(expectedMessage));
    }

    @Step("Проверяем ответ: курьер создан")
    static void validateResponseOk(Response response, int expectedStatusCode, Boolean expectedMessage) {
        response.then()
                .statusCode(expectedStatusCode)
                .body("ok", equalTo(expectedMessage));
    }

    @Step("Проверяем ответ: успешная авторизация")
    static void loginResponseOk(Response response) {
        response.then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Step("Проверяем ответ: не успешная авторизация")
    static void loginResponseMistake(Response response, int expectedStatusCode, String expectedMessage) {
        response.then()
                .statusCode(expectedStatusCode)
                .body("message", equalTo(expectedMessage));
    }

    @Step("Авторизация курьера")
    static Response loginCourierRequest(String login, String password) {
        courierJSON = new CourierJSON(login, password);
        Response loginResponse = given()
                .filter(new AllureRestAssured())
                .auth().oauth2(bearerToken)
                .header("Content-type", "application/json")
                .body(courierJSON)
                .when()
                .post(COURIER_API+"/login");

        return loginResponse;
    }

    @Step("Удаление курьера")
    static void deleteCourier() {
        if (login != null && !login.isEmpty() && password != null && !password.isEmpty()) {
            Response loginResponse = loginCourierRequest(login, password);
            courierId = loginResponse.then().extract().path("id").toString();

            if (loginResponse.statusCode() == 200) {

                given()
                        .log().all()
                        .filter(new AllureRestAssured())
                        .auth().oauth2(bearerToken)
                        .header("Content-type", "application/json")
                        .when()
                        .delete(COURIER_API + courierId)
                        .then()
                        .log().all()
                        .statusCode(200);
            }
        }
    }

}
