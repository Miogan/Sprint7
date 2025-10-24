package ru.yandex.practicum.test.order.create;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.AllureJunit4;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.model.Courier;
import org.example.model.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.yandex.practicum.test.BaseTest;
import steps.CourierSteps;
import steps.OrderSteps;

import java.time.LocalDate;
import java.util.Random;

import static org.hamcrest.CoreMatchers.notNullValue;

@Epic("Заказы")
@Feature("Управление заказами")
public class OrderCreate extends BaseTest{
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
        @DisplayName("Create order")
        @Feature("Создание заказа")
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
