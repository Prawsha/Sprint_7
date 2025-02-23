package ru.qa_scooter;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.Description;
import static ru.qa_scooter.OrderSteps.*;
import static ru.qa_scooter.UsefulData.*;

public class OrderListTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;

        sendCreateOrderRequest("Василий","Васильев","Москва","4", "+79006542211",
                "5", "2025-02-28", "Звонить в домофон", new String[] {"BLACK"});
    }

    @Test
    @DisplayName("Получить список заказов")
    @Description("Проверяем ответ: возвращён список заказов")
    public void testGetOrderList() {

        Response response = getOrderList();
        validateGetOrderList(response);
    }

    @After
    public void deleteData() {
        cancelOrder();
    }
}