package org.example.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.models.ProductRequest;
import org.example.models.ProductResponse;

import static io.restassured.RestAssured.given;
import static org.example.specs.ResponseSpecs.successResponseSpec;
import static org.example.specs.RestApiBuilder.getRequestSpec;

public class ProductApiClient {

    @Step("Отправить POST-запрос на добавление товара")
    public Response addProduct(ProductRequest productRequest) {
        return given()
                .spec(getRequestSpec())
                .body(productRequest)
                .when()
                .post("/add");
    }

    @Step("Отправить POST-запрос на добавление товара")
    public Integer addProduct(String name, Double price) {
        return addProduct(new ProductRequest(name, price))
                .then()
                .spec(successResponseSpec)
                .extract()
                .as(ProductResponse.class)
                .data()
                .get("id");
    }

    @Step("Удалить товар с ID")
    public Response deleteProductById(Integer id) {
        return given()
                .spec(getRequestSpec())
                .pathParam("id", id)
                .delete("/{id}");
    }
}
