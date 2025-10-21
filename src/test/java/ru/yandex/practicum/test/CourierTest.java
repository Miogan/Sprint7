package ru.yandex.practicum.test;

import io.qameta.allure.junit4.DisplayName;
import org.example.model.Courier;
import org.example.steps.CourierSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;

public class CourierTest extends BaseTest{
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
    // Создаем курьера без логина, код 400
    public void ShouldCreateCourierWithoutLoginTest(){
        courier.setFirstName("saske");
        courier.setLogin("");
        courierSteps
                .createCourier(courier)
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Create courier & authorization")
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
