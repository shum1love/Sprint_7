package org.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class TestLoginCourier extends ApiTestBase {
    private String courierId;
    private CourierApi courierApi;

    @Before
    public void setUp() {
        super.setUp(); // Вызов метода установления базового URL
        courierApi = new CourierApi(requestSpecification); // Инициализируйте CourierApi
    }

    @Test
    @DisplayName("Первый тест")
    @Description("Проверяет может ли курьер авторизоваться")
    public void testCanLogin() {
        CreateCourier createCourier = new CreateCourier("Ricardo444667", "12345", "Ricardo3245");
        Response createResponse = courierApi.createCourier(createCourier); // Используем экземпляр CourierApi для создания курьера
        createResponse.then().statusCode(201); // Проверка кода ответа

        String login = createCourier.getLogin(); // Логин
        String password = createCourier.getPassword(); // Пароль
        IdCourier idCourier = new IdCourier(login, password);
        Response loginResponse = courierApi.loginCourier(idCourier); // Вызов метода логина

        String idFromResponse = loginResponse.jsonPath().getString("id"); // Извлекаем ID из ответа
        given()
                .then()
                .body("id", equalTo(idFromResponse)); // Сравниваем извлеченный ID с ожидаемым

        this.courierId = idFromResponse; // Сохраняем ID курьера
    }

    @Test
    @DisplayName("Второй тест")
    @Description("Для авторизации нужно передать все обязательные поля. Ошибка, если какого-то поля нет")
    public void testAllField() {
        CreateCourier createCourier = new CreateCourier("Ricardo662277", "12345", "Ricardo3245");
        Response createResponse = courierApi.createCourier(createCourier); // Используем экземпляр CourierApi
        createResponse.then().statusCode(201); // Проверка кода ответа

        String login = createCourier.getLogin(); // Логин
        String password = createCourier.getPassword(); // Пароль
        IdCourier idCourier = new IdCourier(login, password);
        Response loginResponse = courierApi.loginCourier(idCourier); // Вызов метода логина

        String idFromResponse = loginResponse.jsonPath().getString("id"); // Извлекаем ID из ответа
        given()
                .then()
                .body("id", equalTo(idFromResponse)); // Сравниваем извлеченный ID с ожидаемым

        this.courierId = idFromResponse; // Сохраняем ID курьера
    }

    @After
    public void tearDown() {
        if (courierId != null) {
            given()
                    .pathParam("id", courierId)
                    .when()
                    .delete("/api/v1/courier/" + "{id}")
                    .then()
                    .statusCode(200); // Проверяем, что курьер успешно удален
        }
    }
}
