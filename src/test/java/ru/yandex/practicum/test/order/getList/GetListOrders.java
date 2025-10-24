package ru.yandex.practicum.test.order.getList;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import org.example.model.Courier;
import org.example.model.Order;
import ru.yandex.practicum.test.BaseTest;
import steps.CourierSteps;
import steps.OrderSteps;
import org.junit.After;
import org.junit.Before;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Random;


import static org.hamcrest.CoreMatchers.notNullValue;

import static org.hamcrest.Matchers.greaterThan;

@Epic("Заказы")
@Feature("Управление заказами")
public class GetListOrders extends BaseTest {
    private CourierSteps courierSteps = new CourierSteps();
    private OrderSteps orderSteps = new OrderSteps();
    private Order order;
    private Courier courier;

    @Before
    public void setUp(){
        order = new Order();
        order.setFirstName("ninja");
        order.setLastName(RandomStringUtils.randomAlphanumeric(5));
        order.setAddress("г. Москва" + System.currentTimeMillis());
        order.setMetroStation(RandomStringUtils.randomAlphanumeric(10));
        order.setPhone("+7" + System.currentTimeMillis());
        order.setRentTime(new Random().nextInt(30 - 1 + 1) + 1);
        order.setDeliveryDate(LocalDate.now().plusDays(3).toString());
        order.setComment(RandomStringUtils.randomAlphanumeric(10) + System.currentTimeMillis());

        // Инициализируем курьера
        courier = new Courier();
        courier.setLogin("ninja" + System.currentTimeMillis());
        courier.setPassword("pas"  + System.currentTimeMillis());
    }



    @Test
    @DisplayName("Get list order without courier id")
    @Feature("Получение списка заказа без id курьера")
    // Получить список заказов, код 200
    public void ShouldGetOrdersListWithoutCourierIdTest(){
        courierSteps
                .createCourier(courier);
        courierSteps
                .login(courier);
        orderSteps
                .createOrder(order);
        orderSteps
                .getListOrders(order)
                .statusCode(200)
                .body("orders.size()", greaterThan(0))
                .body("orders[0].id", notNullValue())
                .body("orders[0].track", notNullValue());
    }

    @Test
    @DisplayName("Get list order without courier id & limit = 10")
    @Feature("Получение списка заказа без id курьера и лимит = 10")
    // Получить список заказов, код 200
    public void ShouldGetOrdersListWithoutCourierIdWithLimitTest(){
        Integer limit = 10;
        Integer page = 0;
        courierSteps
                .createCourier(courier);
        courierSteps
                .login(courier);
        orderSteps
                .createOrder(order);
        orderSteps
                .getListOrdersWithLimit(order, limit, page)
                .statusCode(200)
                .body("orders.size()", greaterThan(0))
                .body("orders[0].id", notNullValue())
                .body("orders[0].track", notNullValue());
    }

    @Test
    @DisplayName("Get list order  with courier id")
    @Feature("Получение списка заказа с id  курьера")
    // Получить список заказов курьера, код 200
    public void ShouldGetOrdersListCourierTest(){
        courierSteps
                .createCourier(courier);
        courierSteps
                .login(courier);
        courier.setId(courierSteps.login(courier)
                .extract().body().path("id"));
        orderSteps
                .createOrder(order);
        orderSteps
                .listOrdersCourier(courier)
                .statusCode(200)
                .body("orders.size()", greaterThan(0))
                .body("orders[0].id", notNullValue())
                .body("orders[0].track", notNullValue());
    }

    @Test
    @DisplayName("Get list order  with courier id & on station")
    @Feature("Получение списка заказов курьера на станциях из списка")
    // Получить список заказов курьера на станциях из списка, код 200
    public void shouldGetOrdersListCourierOnStationTest(){
          Integer[] nearestStation =  new Integer[] { 1, 2 };
        courierSteps
                .createCourier(courier);
        courierSteps
                .login(courier);
        courier.setId(courierSteps.login(courier)
                .extract().body().path("id"));
        orderSteps
                .createOrder(order);
        orderSteps
                .listOrdersCourierOnStation(courier, nearestStation)
                .statusCode(200)
                .body("orders.size()", greaterThan(0))
                .body("orders[0].id", notNullValue())
                .body("orders[0].track", notNullValue());
    }

    @Test
    @DisplayName("Get list order  with courier id & on station & limit")
    @Feature("Получение списка заказов курьера на станциях из списка с лимитом")
    // Получить список заказов курьера на станциях из списка с лимитом, код 200
    public void shouldGetOrdersListCourierOnStationWithLimitTest(){
        Integer limit = 10;
        Integer page = 0;
        Integer[] nearestStation =  new Integer[] { 1, 2 };
        courierSteps
                .createCourier(courier);
        courierSteps
                .login(courier);
        courier.setId(courierSteps.login(courier)
                .extract().body().path("id"));
        orderSteps
                .createOrder(order);
        orderSteps
                .listOrdersCourierOnStationWithLimit(courier, nearestStation, limit, page)
                .statusCode(200)
                .body("orders.size()", greaterThan(0))
                .body("orders[0].id", notNullValue())
                .body("orders[0].track", notNullValue());
    }

    @After
    // Прибираем за собой
    @DisplayName("Clear order")
    @Feature("Удаление заказа")
    public void tearDown(){
        if (order != null) {
            Integer idOrder = orderSteps.createOrder(order)
                    .extract().body().path("track");
            if (idOrder == null) {
                order.setId(idOrder);
                orderSteps.cancellationOrder(order);
            }
        }
    }


}
