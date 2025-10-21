package ru.yandex.practicum.test;

import io.qameta.allure.junit4.DisplayName;
import org.example.model.Courier;
import org.example.model.Order;
import org.example.steps.CourierSteps;
import org.example.steps.OrderSteps;
import org.junit.After;
import org.junit.Before;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Random;

import static org.hamcrest.CoreMatchers.notNullValue;


public class OrderTest extends BaseTest{
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
    @DisplayName("Create order test")
    // Создаем заказ и не указываем цвета, код 201
    public void ShouldCreateOrderTest(){
        courierSteps
                .createCourier(courier);
        courierSteps
                .login(courier);
        orderSteps
                .createOrder(order)
                .statusCode(201)
                .body("track", notNullValue());
    }
    @Test
    @DisplayName("Create order test with color black or grey")
    // Создаем заказ c цветом BLACK OR GREY, код 201
    public void ShouldCreateOrderWithColorTest(){
        courierSteps
                .createCourier(courier);
        courierSteps
                .login(courier);
        String[] color = new String[] { "BLACK", "GREY" };
        int randomcolor = new Random().nextInt(color.length);
        order.setColor(color);
        orderSteps
                .createOrder(order)
                .statusCode(201)
                .body("track", notNullValue());
    }
     // Создаем заказ. Указываем два цвета — BLACK и GREY

     @Test
     // Создаем заказ c цветом BLACK OR GREY, код 201
     @DisplayName("Create order test with color black & grey")
     public void ShouldCreateOrderWithTwoColorTest(){
         courierSteps
                 .createCourier(courier);
         courierSteps
                 .login(courier);
         String[] color = new String[] { "BLACK", "GREY" };
         order.setColor(color);
         orderSteps
                 .createOrder(order)
                 .statusCode(201)
                 .body("track", notNullValue());
     }

    @Test
    @DisplayName("Get list order without courier id")
    // Получить список заказов, код 200
    // Ошибка, код 200 <> 201
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
                .body("track", notNullValue());
    }

    @Test
    @DisplayName("Get list order without courier id & limit = 10")
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
                .body("track", notNullValue());
    }

    @Test
    @DisplayName("Get list order  with courier id")
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
                .body("track", notNullValue());
    }

    @Test
    @DisplayName("Get list order  with courier id & on station")
    // Получить список заказов курьера на станциях из списка, код 200
    public void ShouldGetOrdersListCourierOnStationTest(){
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
                .body("track", notNullValue());
    }

    @Test
    @DisplayName("Get list order  with courier id & on station & limit")
    // Получить список заказов курьера на станциях из списка с лимитом, код 200
    public void ShouldGetOrdersListCourierOnStationWithLimitTest(){
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
                .body("track", notNullValue());
    }

    @After
    // Прибираем за собой
    @DisplayName("Clear order")
    public void tearDown(){
        if (order == null) {
            Integer idOrder = orderSteps.createOrder(order)
                    .extract().body().path("track");
            if (idOrder == null) {
                order.setId(idOrder);
                orderSteps.cancellationOrder(order);
            }
        }
    }

}
