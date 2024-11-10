package org.example;
//1

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class TestCreateOrder {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Первый тест")
    @Description("Проверяет, что при создании заказа можно указать один из цветов - BLACK или GREY, можно указать оба цвета, можно совсем не указывать цвет") // Описание
    public void testRequiredFields() {
        List<CreateOrder> createOrders = prepareOrderData();
        createOrders.forEach(this::createOrder);
    }

    @Step("Подготовка данных для создания заказов")
    private List<CreateOrder> prepareOrderData() {
        List<CreateOrder> createOrders = new ArrayList<>();
        createOrders.add(new CreateOrder("Rodion", "Shumilov", "Lomonosova 100, 100", "Metro", "88005553535", 3, "2024-11-11", "hello", new String[]{"BLACK"}));
        createOrders.add(new CreateOrder("Rodion", "Shumilov", "Lomonosova 100, 100", "Metro", "88005553535", 3, "2024-11-11", "hello", new String[]{"BLACK", "GREY"}));
        createOrders.add(new CreateOrder("Rodion", "Shumilov", "Lomonosova 100, 100", "Metro", "88005553535", 3, "2024-11-11", "hello", new String[]{})); // Разные случаи
        return createOrders;
    }

    @Step("Создание заказа")
    private void createOrder(CreateOrder order) {
        given()
                .header("Content-type", "application/json") // Передача Content-type в заголовке для указания типа файла
                .and()
                .body(order) // Передача заказа в теле запроса
                .when()
                .post("/api/v1/orders") // Отправка POST-запроса
                .then()
                .statusCode(201) // Проверка кода ответа
                .body("track", notNullValue()); // Проверка, что поле track не равно null
    }
}
