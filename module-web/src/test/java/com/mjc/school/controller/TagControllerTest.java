package com.mjc.school.controller;


import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
//import org.testng.annotations.Test;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

public class TagControllerTest {

    private Long tagId;


    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/api/v1";


        Map<String, String> tagBody = new HashMap<>();
        tagBody.put("name", "No One");

        int id = given()
                .contentType("application/json")
                .body(tagBody)
                .when().post("/tags")
                .then()
                .statusCode(201)
                .extract().path("id");

        tagId = Long.valueOf(id);

        System.out.println("Created comment ID: " + id);


    }

    @Test
    public void testGetTags() {
        given().get("/tags").then().assertThat().statusCode(200);
    }


    @Test
    public void testGetTagsById() {
        given().get("/tags/" + tagId).then().assertThat().statusCode(200);
    }

    @Test
    public void testCreateTag() {
        Map<String, String> tagBody = new HashMap<>();
        tagBody.put("name", "No One");

        given()
                .contentType("application/json")
                .body(tagBody)
                .when().post("/tags").then()
                .statusCode(201);
    }


    @Test
    public void testUpdateTag() {
        Map<String, Object> tagBody = new HashMap<>();
        tagBody.put("id", tagId);
        tagBody.put("name", "No One updated");

        given()
                .contentType("application/json")
                .body(tagBody)
                .when()
                .put("/tags")
                .then()
                .statusCode(anyOf(equalTo(200), equalTo(204)));
    }

    @Test
    public void testDeleteTag() {
        Long tagIdToDelete = tagId;

        given()
                .contentType("application/json")
                .when()
                .delete("/tags/" + tagIdToDelete)
                .then()
                .statusCode(204);

        given()
                .contentType("application/json")
                .when()
                .get("/tags/" + tagIdToDelete)
                .then()
                .statusCode(404);


    }


}

