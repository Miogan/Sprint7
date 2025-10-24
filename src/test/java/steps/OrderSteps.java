package steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.model.Courier;
import org.example.model.Order;

import static io.restassured.RestAssured.given;

public class OrderSteps {
    public static final String CREATEORDER = "/api/v1/orders";
    public static final String GETORDERS = "/api/v1/orders";
    public static final String СANCELLATIONOORDER = "/api/v1/orders/cancel";
    public static final String LISTORDERSCOURIERS = "/v1/orders?courierId={courierId}";

    @Step("Создание заказа")
    public ValidatableResponse createOrder(Order order){
        return given()
                .body(order)
                .when()
                .post(CREATEORDER)
                .then();
    }

    @Step("Получение списка заказа")
    public ValidatableResponse getListOrders(Order order){
        return given()
                .body(order)
                .when()
                .get(GETORDERS) // исправлена ошибка в типе метода
                .then();
    }

    @Step("Получение списка заказа с лимитом")
    public ValidatableResponse getListOrdersWithLimit(Order order, Integer limit, Integer page){
        return given()
                .queryParam("limit", limit)  // передаем limit как query parameter
                .queryParam("page", 0)  // добавляем page параметр
                .body(order)
                .when()
                .get(GETORDERS) // исправлена ошибка в типе метода
                .then();
    }

    @Step("Получение списка заказа для конкретного курьера")
    public ValidatableResponse listOrdersCourier(Courier courier){
        return given()
                .pathParams("courierId", courier.getId())
                .when()
                .get(LISTORDERSCOURIERS)
                .then();
    }

   @Step("Получение списка на станциях из списка")
    public ValidatableResponse listOrdersCourierOnStation(Courier courier,Integer[] nearestStation){
        return given()
                .queryParam("nearestStation", nearestStation)
                .pathParams("courierId", courier.getId())
                .when()
                .get(LISTORDERSCOURIERS)
                .then();
    }

    @Step("Получение списка на станциях из списка с лимитом")
    public ValidatableResponse listOrdersCourierOnStationWithLimit(Courier courier,Integer[] nearestStation,Integer limit, Integer page){
        return given()
                .queryParam("limit", limit)  // передаем limit как query parameter
                .queryParam("page", 0)  // добавляем page параметр
                .queryParam("nearestStation", nearestStation)
                .pathParams("courierId", courier.getId())
                .when()
                .get(LISTORDERSCOURIERS)
                .then();
    }

    @Step("Удаление заказа")
    public ValidatableResponse cancellationOrder(Order order){
        return given()
                .pathParams("track", order.getId())
                .when()
                .delete(СANCELLATIONOORDER)
                .then();
    }
}
