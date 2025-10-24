package steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.model.Courier;

import static io.restassured.RestAssured.given;


public class CourierSteps {
    public static final String COURIERCREATE = "/api/v1/courier";
    public static final String COURIERLOGIN = "/api/v1/courier/login";
    public static final String COURIERDELETE = "/api/v1/courier/{id}";


    @Step("Создание курьера")
    public ValidatableResponse createCourier(Courier courier){
        return given()
                .body(courier)
                .when()
                .post(COURIERCREATE)
                .then();
    }

    @Step("Авторизация курьера")
    public ValidatableResponse login(Courier courier){
        return given()
                .body(courier)
                .when()
                .post(COURIERLOGIN)
                .then();
    }

    @Step("Удаление курьера")
    public ValidatableResponse deleteCourier(Courier courier){
        return given()
                .pathParams("id", courier.getId())
                .when()
                .delete(COURIERDELETE)
                .then();
    }
}
