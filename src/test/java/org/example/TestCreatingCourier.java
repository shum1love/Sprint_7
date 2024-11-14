package org.example;
//1

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
// дополнительный статический импорт нужен, чтобы использовать given(), get() и then()
import static io.restassured.RestAssured.*;
import static org.hamcrest.core.IsEqual.equalTo;

public class TestCreatingCourier extends ApiTestBase {
    private String courierId; // Переменная для хранения ID созданного курьера

    //курьера можно создать;
    @Test
    @DisplayName("Первый тест")
    @Description("проверяет Можно ли создать курьера, какой код ответа") //Описание
    public void testCreateCourier() {
        CreateCourier createCourier = new CreateCourier("Alabyyeqquo", "4444", "Roman");
        createCourier(createCourier);
        this.courierId = loginCourier(createCourier);
    }

    //нельзя создать двух одинаковых курьеров;
    @Test
    @DisplayName("Второй тест")
    @Description("Проверка ошибки при создании двух одинаковых курьеров") //Описание
    public void testCreateDoubleCourier() {
        CreateCourier createCourier = new CreateCourier("Vitalikkk444", "4444", "Roman");
        createCourier(createCourier);

        requestSpecification
                .body(createCourier)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(409); // Ожидаем статус код 409

        this.courierId = loginCourier(createCourier);
    }

    @Step("Создаем курьера")
    private void createCourier(CreateCourier createCourier) {
        requestSpecification // использует requestSpecification из базового класса
                .body(createCourier)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(201) // проверка кода ответа
                .body("ok", equalTo(true));
    }

    @Step("Логин курьера")
    private String loginCourier(CreateCourier createCourier) {
        String login = createCourier.getLogin(); // Логин
        String password = createCourier.getPassword(); // Пароль
        IdCourier idCourier = new IdCourier(login, password);
        Response loginResponse = requestSpecification // использует requestSpecification из базового класса
                .body(idCourier) // Передача логина и пароля в теле запроса
                .when()
                .post("/api/v1/courier/login"); // Отправка POST-запроса

        // Получаем ID курьера из ответа
        return loginResponse.jsonPath().getString("id"); // Возвращаем ID курьера
    }
    @After
    public void tearDown() {
        if (courierId != null) {
            given()
                    .pathParam("id", courierId)
                    .when()
                    //.delete("/api/v1/courier/" + "/{id}")
                    .delete("/api/v1/courier/" + "{id}")
                    .then()
                    .statusCode(200); // Проверяем, что курьер успешно удален
        }
    }
}
