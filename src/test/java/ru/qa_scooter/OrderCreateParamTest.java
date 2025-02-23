package ru.qa_scooter;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;
import static ru.qa_scooter.OrderSteps.*;
import static ru.qa_scooter.UsefulData.*;
import io.qameta.allure.Description;

@RunWith(Parameterized.class)
public class OrderCreateParamTest {

    String[] colors;

    public OrderCreateParamTest(String[] colors) {
        this.colors = colors;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{null}}
        });
    }

    @Test
    @DisplayName("Разные цвета в заказе")
    @Description("Проверяем: можно создать заказ с разными цветами")
    public void testCreateOrderWithDifferentColors() {

        Response response = sendCreateOrderRequest("Василий", "Васильев", "Москва",
                "4", "+79006542211", "5", "2025-02-28",
                "Звонить в домофон", colors);
        validateCreateOrderResponse(response);
    }

    @After
    public void deleteData() {
        cancelOrder();
    }

}