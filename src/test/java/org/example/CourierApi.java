package org.example;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class CourierApi {

    private final RequestSpecification requestSpecification;

    public CourierApi(RequestSpecification requestSpecification) {
        this.requestSpecification = requestSpecification;
    }

    public Response createCourier(CreateCourier createCourier) {
        return requestSpecification.body(createCourier)
                .when()
                .post("/api/v1/courier");
    }

    public Response loginCourier(IdCourier idCourier) {
        return requestSpecification.body(idCourier)
                .when()
                .post("/api/v1/courier/login");
    }
}
