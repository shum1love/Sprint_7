package org.example;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
//1
public class TestGetOrder {
    private Number order;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Первый тест")
    @Description("Проверяет, что при создании заказа можно указать один из цветов - BLACK или GREY, можно указать оба цвета, можно совсем не указывать цвет") // Описание
    public void testRequiredFields() {
        createAndStoreOrders();
        verifyOrderById();
    }

    @Step("Создание и сохранение заказов")
    private void createAndStoreOrders() {
        List<CreateOrder> createOrders = prepareOrderData();

        Response orderResponse = given()
                .header("Content-type", "application/json")
                .body(createOrders)
                .when()
                .post("/api/v1/orders")
                .then()
                .statusCode(201)
                .body("track", notNullValue())
                .extract().response();

        // Извлечение ID заказа из ответа
        this.order = orderResponse.jsonPath().getInt("track");
    }

    @Step("Верфикация заказа по ID")
    private void verifyOrderById() {
        given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders/track?t=" + this.order)
                .then()
                .statusCode(200); // Проверка кода ответа
    }

    @Step("Подготовка данных для создания заказов")
    private List<CreateOrder> prepareOrderData() {
        List<CreateOrder> createOrders = new ArrayList<>();
        createOrders.add(new CreateOrder("Rodion", "Shumilov", "Lomonosova 100, 100", "Metro", "88005553535", 3, "2024-11-11", "hello", new String[]{"BLACK"}));
        createOrders.add(new CreateOrder("Rodion", "Shumilov", "Lomonosova 100, 100", "Metro", "88005553535", 3, "2024-11-11", "hello", new String[]{"BLACK", "GREY"}));
        createOrders.add(new CreateOrder("Rodion", "Shumilov", "Lomonosova 100, 100", "Metro", "88005553535", 3, "2024-11-11", "hello", new String[]{})); // Разные случаи
        return createOrders;
    }
}
