package org.example;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class TestCreateOrder extends ApiTestBase {

    private CreateOrder order;

    // Конструктор для передачи параметров
    public TestCreateOrder(CreateOrder order) {
        this.order = order;
    }

    @Parameterized.Parameters
    public static Collection<CreateOrder[]> data() {
        return Arrays.asList(new CreateOrder[][] {
                {
                        new CreateOrder("Rodion", "Shumilov", "Lomonosova 100, 100", "Metro", "88005553535", 3, "2024-11-11", "hello", new String[]{"BLACK"})
                },
                {
                        new CreateOrder("Rodion", "Shumilov", "Lomonosova 100, 100", "Metro", "88005553535", 3, "2024-11-11", "hello", new String[]{"BLACK", "GREY"})
                },
                {
                        new CreateOrder("Rodion", "Shumilov", "Lomonosova 100, 100", "Metro", "88005553535", 3, "2024-11-11", "hello", new String[]{})
                }
        });
    }

    @Test
    @DisplayName("Первый тест")
    @Description("Проверяет, что при создании заказа можно указать один из цветов - BLACK или GREY, можно указать оба цвета, можно совсем не указывать цвет")
    public void testRequiredFields() {
        createOrder(order);
    }

    @Step("Создание заказа")
    private void createOrder(CreateOrder order) {
        given()
                .body(order) // Передача заказа в теле запроса
                .when()
                .post("/api/v1/orders") // Отправка POST-запроса
                .then()
                .statusCode(201) // Проверка кода ответа
                .body("track", notNullValue()); // Проверка, что поле track не равно null
    }
}
