package ru.yandex.practicum.test.order.create;

import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.model.Courier;
import org.example.model.Order;
import ru.yandex.practicum.test.BaseTest;
import steps.CourierSteps;
import steps.OrderSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderTestParametrized extends BaseTest {
    private CourierSteps courierSteps = new CourierSteps();
    private OrderSteps orderSteps = new OrderSteps();
    private Order order;
    private Courier courier;

    private final String[] colors;

    public OrderTestParametrized(String[] colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters(name = "Тест цвета: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                // УБРАТЬ первый строковый параметр, оставить только массивы
                { new String[] { "BLACK" } },
                { new String[] { "GREY" } },
                { new String[] { "BLACK", "GREY" } }
        });
    }

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
    @DisplayName("Create order with different color combinations")
    @Feature("Создание заказа с разными комбинациями цветов")
    public void shouldCreateOrderWithDifferentColorsTest(){
        courierSteps.createCourier(courier);
        courierSteps.login(courier);

        order.setColor(colors);

        orderSteps.createOrder(order)
                .statusCode(201)
                .body("track", notNullValue());
    }

    @After
    @DisplayName("Clean courier")
    @Feature("Удаление курьера")
    // Прибираем за собой
    public void tearDown(){
        if (courier != null) {
            Integer idCourier = courierSteps.login(courier)
                    .extract().body().path("id");
            if (idCourier == null) {
                courier.setId(idCourier);
                courierSteps.deleteCourier(courier);
            }
        }
    }
}