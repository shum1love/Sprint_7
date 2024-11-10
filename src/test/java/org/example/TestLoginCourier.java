package org.example;
//1

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
// дополнительный статический импорт нужен, чтобы использовать given(), get() и then()
import static io.restassured.RestAssured.*;
import static org.hamcrest.core.IsEqual.equalTo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestLoginCourier {
    private String courierId; // Переменная для хранения ID созданного курьера
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }
    @Test
    @DisplayName("Первый тест")
    @Description("Проверяет может ли курьер авторизоваться") //Описание
    public void testCanLogin(){
        CreateCourier createCourier = new CreateCourier("Ricardo4445577", "12345", "Ricardo3245");
        given()
                .header("Content-type", "application/json") // передача Content-type в заголовке для указания типа файла
                .and()
                .body(createCourier) // передача файла
                .when()
                .post("/api/v1/courier") // отправка POST-запроса
                .then().statusCode(201); // проверка кода ответа

        // Получаем ID курьера для последующего удаления
        String login = createCourier.getLogin(); // Логин
        String password = createCourier.getPassword(); // Пароль
        IdCourier idCourier = new IdCourier(login, password);
        Response loginResponse = (Response) given()
                .header("Content-type", "application/json") // Передача Content-type в заголовке
                .body(idCourier) // Передача логина и пароля в теле запроса
                .when()
                .post("/api/v1/courier/login"); // Отправка POST-запроса

        String idFromResponse = loginResponse.jsonPath().getString("id"); // Извлекаем ID из ответа

        // Проверяем, что полученный ID соответствует ID курьера
        given()
                .header("Content-type", "application/json") // Передача Content-type в заголовке
                .then()
                .body("id", equalTo(idFromResponse)); // Сравниваем извлеченный ID с ожидаемым

        // Сохраняем ID курьера для дальнейшего использования
        this.courierId = idFromResponse; // сохраняем ID курьера
    }
    @Test
    @DisplayName("Второй тест")
    @Description("Для авторизации нужно передать все обязательные поля. Ошибка, если какого-то поля нет") //Описание
    public void testAllField(){
        CreateCourier createCourier = new CreateCourier("Ricardo6655577", "12345", "Ricardo3245");
        given()
                .header("Content-type", "application/json") // передача Content-type в заголовке для указания типа файла
                .and()
                .body(createCourier) // передача файла
                .when()
                .post("/api/v1/courier") // отправка POST-запроса
                .then().statusCode(201); // проверка кода ответа

        // Получаем ID курьера для последующего удаления
        String login = createCourier.getLogin(); // Логин
        String password = createCourier.getPassword(); // Пароль
        IdCourier idCourier = new IdCourier(login, password);
        Response loginResponse = (Response) given()
                .header("Content-type", "application/json") // Передача Content-type в заголовке
                .body(idCourier) // Передача логина и пароля в теле запроса
                .when()
                .post("/api/v1/courier/login"); // Отправка POST-запроса
        String idFromResponse = loginResponse.jsonPath().getString("id"); // Извлекаем ID из ответа
        this.courierId = idFromResponse; // сохраняем ID курьера

        //Отправка запроса без логина
        IdCourier idCourier2 = new IdCourier("", password);
        given()
                .header("Content-type", "application/json") // Передача Content-type в заголовке
                .body(idCourier2) // Передача логина и пароля в теле запроса
                .when()
                .post("/api/v1/courier/login") // Отправка POST-запроса
                .then().statusCode(400);

        //Отправка запроса без пароля
        IdCourier idCourier3 = new IdCourier(login, "");
        given()
                .header("Content-type", "application/json") // Передача Content-type в заголовке
                .body(idCourier3) // Передача логина и пароля в теле запроса
                .when()
                .post("/api/v1/courier/login") // Отправка POST-запроса
                .then().statusCode(400);
    }

    @Test
    @DisplayName("Третий тест")
    @Description("Система вернёт ошибку, если неправильно указать логин или пароль") //Описание
    public void testWrongFields(){
        CreateCourier createCourier = new CreateCourier("Ricardo4445577", "12345", "Ricardo3245");
        given()
                .header("Content-type", "application/json") // передача Content-type в заголовке для указания типа файла
                .and()
                .body(createCourier) // передача файла
                .when()
                .post("/api/v1/courier") // отправка POST-запроса
                .then().statusCode(201); // проверка кода ответа

        // Получаем ID курьера для последующего удаления
        String login = createCourier.getLogin(); // Логин
        String password = createCourier.getPassword(); // Пароль
        IdCourier idCourier = new IdCourier(login, password);
        Response loginResponse = (Response) given()
                .header("Content-type", "application/json") // Передача Content-type в заголовке
                .body(idCourier) // Передача логина и пароля в теле запроса
                .when()
                .post("/api/v1/courier/login"); // Отправка POST-запроса

        String idFromResponse = loginResponse.jsonPath().getString("id"); // Извлекаем ID из ответа
        this.courierId = idFromResponse; // сохраняем ID курьера

        //Тест с неправильным логином
        IdCourier idCourier2 = new IdCourier("Buggg6546456", password);
        given()
                .header("Content-type", "application/json") // Передача Content-type в заголовке
                .body(idCourier2) // Передача логина и пароля в теле запроса
                .when()
                .post("/api/v1/courier/login") // Отправка POST-запроса
                .then().statusCode(404);

        //Тест с неправильным паролем
        IdCourier idCourier3 = new IdCourier(login, "Buggg6546456");
        given()
                .header("Content-type", "application/json") // Передача Content-type в заголовке
                .body(idCourier3) // Передача логина и пароля в теле запроса
                .when()
                .post("/api/v1/courier/login") // Отправка POST-запроса
                .then().statusCode(404);

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
