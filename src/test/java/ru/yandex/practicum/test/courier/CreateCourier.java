package ru.yandex.practicum.test.courier;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import org.example.model.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.test.BaseTest;
import steps.CourierSteps;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

@Epic("Курьерская служба")
@Feature("Управление курьерами. Создание")
public class CreateCourier extends BaseTest{
        private CourierSteps courierSteps = new CourierSteps();
        private Courier courier;

        @Before
        public void setUp(){
            courier = new Courier();
            courier.setLogin("ninja" + System.currentTimeMillis());
            courier.setPassword("pas"  + System.currentTimeMillis());
        }

        @Test
        @DisplayName("Create courier")
        @Feature("Создание курьера")
        // Создаем курьера, код 201
        public void ShouldCreateCourierTest(){
            courier.setFirstName("saske");
            courierSteps
                    .createCourier(courier)
                    .statusCode(201)
                    .body("ok", is(true));
        }

        @Test
        @DisplayName("Create courier two")
        @Feature("Создание двух одинаковых курьеров")
        // Создаем двух курьеров c одним логином, код 409
        // Ошибка: message <> Этот логин уже используется
        public void ShouldCreateDoubleCourierTest(){
            courier.setFirstName("saske");
            courierSteps
                    .createCourier(courier);
            courierSteps
                    .createCourier(courier)
                    .statusCode(409)
                    .body("message", equalTo("Этот логин уже используется"));
        }

        @Test
        @DisplayName("Create courier without login")
        @Feature("Создание курьера без передачи логина")
        // Создаем курьера без логина, код 400
        public void ShouldCreateCourierWithoutLoginTest(){
            courier.setFirstName("saske");
            courier.setLogin("");
            courierSteps
                    .createCourier(courier)
                    .statusCode(400)
                    .body("message", equalTo("Недостаточно данных для создания учетной записи"));
        }

        @After
        @DisplayName("Clean courier")
        @Feature("Удаление курьера")
        // Прибираем за собой
        public void tearDown(){
            if (courier == null) {
                Integer idCourier = courierSteps.login(courier)
                        .extract().body().path("id");
                if (idCourier == null) {
                    courier.setId(idCourier);
                    courierSteps.deleteCourier(courier);
                }
            }
        }

}
