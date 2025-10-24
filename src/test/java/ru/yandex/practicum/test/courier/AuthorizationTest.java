package ru.yandex.practicum.test.courier;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import org.example.model.Courier;
import ru.yandex.practicum.test.BaseTest;
import steps.CourierSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;

@Epic("Курьерская служба")
@Feature("Управление курьерами. Авторизация")
public class AuthorizationTest extends BaseTest {
    private CourierSteps courierSteps = new CourierSteps();
    private Courier courier;

    @Before
    public void setUp(){
        courier = new Courier();
        courier.setLogin("ninja" + System.currentTimeMillis());
        courier.setPassword("pas"  + System.currentTimeMillis());
    }

    @Test
    @DisplayName("Create courier & authorization")
    @Feature("Авторизация курьера")
    // Создаем и авторизуемся, код 200
    public void shouldLoginTest(){
        courierSteps
                .createCourier(courier);
        courierSteps
                .login(courier)
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Create courier authorization without login")
    @Feature("Авторизация курьера без логина")
    // Авторизуемся, но передаем не все данные, код 400
    public void shouldLoginWithoutLoginTest(){
        courier.setLogin("");
        courierSteps
                .createCourier(courier);
        courierSteps
                .login(courier)
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    // Авторизуемся, но такой учетки не существует, код 404
    @DisplayName("Create courier authorization non-existent user")
    @Feature("Авторизация под несуществующим курьером")
    public void shouldLoginWithoutCourierTest(){
        courier.setLogin("Ashura45");
        courier.setFirstName("Рыцарский");
        courier.setPassword("333456");
        courierSteps
                .login(courier)
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
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
