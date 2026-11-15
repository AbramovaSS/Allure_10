package org.example.tests;

import com.codeborne.selenide.*;
import com.github.javafaker.Faker;
import org.example.config.ConfigReader;
import org.example.models.ProductRequest;
import org.example.models.ProductResponse;
import org.example.pages.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static org.example.pages.MainPageAssert.assertThat;
import static org.example.pages.AdminPageAssert.assertThat;
import static org.example.specs.ResponseSpecs.successResponseSpec;


public class UiTestsSelenide extends BaseTest {
    Faker faker = new Faker();

    String product = faker.commerce().productName();
    Double price = faker.number().randomDouble(2, 1, 100);
    String login = ConfigReader.CONFIG.userLogin();
    String password = ConfigReader.CONFIG.userPassword();
    String product1 = "Очки";
    String product2 = "Шляпа";
    String product3 = "Покрывало";
    Double price1 = 50.50;
    Double price2 = 60.10;
    Double price3 = 70.10;

    ProductRequest productRequest = new ProductRequest(product, price);
    MainPage mainPage = new MainPage();
    AdminLoginPage adminLoginPage = new AdminLoginPage();
    AdminPage adminPage = new AdminPage();


    @DisplayName("Покупка 3х единиц товара")
    @Test
    void shouldPurchaseThreeUnitsOfProduct() {
        ProductResponse createResponse = api.productApiClient.addProduct(productRequest)
                .then()
                .spec(successResponseSpec)
                .extract()
                .as(ProductResponse.class);

        Integer actualProductId = createResponse
                .data()
                .get("id");

        createdProductIds.add(actualProductId);

        mainPage.refreshPage()
                .addToCartThreeTimes(actualProductId);

        sleep(2000);

        mainPage.clickButtonOpenCart()
                .clickMakeOrderBtn();

        assertThat(mainPage)
                .hasNotificationOrderWithText("Заказ принят в обработку!");

    }

    @DisplayName("Проверка расчета общей цены")
    @Test
    void totalPriceCalculationTest() {
        Integer id1 = api.productApiClient.addProduct(product1, price1);
        Integer id2 = api.productApiClient.addProduct(product2, price2);
        Integer id3 = api.productApiClient.addProduct(product3, price3);

        createdProductIds.addAll(List.of(id1, id2, id3));

        mainPage.refreshPage();
        mainPage.addToCart(id1);
        mainPage.addToCart(id2);
        mainPage.addToCart(id3);

        double expectedTotalPrice = price1 + price2 + price3;

        mainPage.clickButtonOpenCart();

        assertThat(mainPage)
                .verifyTotalPrice(expectedTotalPrice);

    }

    @DisplayName("Проверка уведомления после добавления товара")
    @Test
    void productAddedNotificationTest() {
        mainPage.clickButtonAdminPanel();
        adminLoginPage.setLogin(login)
                .setPassword(password);
        adminPage.setNameField(product)
                .setPriceField(String.valueOf(price))
                .clickSaveButton();

        assertThat(adminPage)
                .hasNotificationWithText("Товар успешно добавлен!");


    }

    @DisplayName("Редактирование товара")
    @Test
    void shouldSuccessfullyEditProduct() {
        ProductResponse createResponse = api.productApiClient.addProduct(productRequest)
                .then()
                .spec(successResponseSpec)
                .extract()
                .as(ProductResponse.class);

        Integer actualProductId = createResponse
                .data()
                .get("id");

        createdProductIds.add(actualProductId);

        mainPage.refreshPage()
                .addToCartThreeTimes(actualProductId);

        sleep(2000);

        mainPage.clickButtonAdminPanel();
        adminLoginPage.setLogin(login)
                .setPassword(password);
        adminPage.setInputs(product)
                .clickUpdateButton()
                .clickExitAdminPanelBtn();

        assertThat(mainPage)
                .hasProductCardWithName(product);
    }
}
