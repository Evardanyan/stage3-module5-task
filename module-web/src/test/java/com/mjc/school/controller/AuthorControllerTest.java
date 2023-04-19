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

public class AuthorControllerTest {

    private Long authorId;


    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/api/v1";


        Map<String, String> authorBody = new HashMap<>();
        authorBody.put("name", "No One");

        int id = given()
                .contentType("application/json")
                .body(authorBody)
                .when().post("/authors")
                .then()
                .statusCode(201)
                .extract().path("id");

        authorId = Long.valueOf(id);

        System.out.println("Created comment ID: " + id);


    }

    @Test
    public void testGetAuthors() {
        given().get("/authors").then().assertThat().statusCode(200);
    }


    @Test
    public void testGetAuthorsById() {
        given().get("/authors/" + authorId).then().assertThat().statusCode(200);
    }

    @Test
    public void testCreateAuthor() {
        Map<String, String> authorBody = new HashMap<>();
        authorBody.put("name", "No One");

        given()
                .contentType("application/json")
                .body(authorBody)
                .when().post("/authors").then()
                .statusCode(201);
    }


    @Test
    public void testUpdateAuthor() {
        Map<String, Object> authorBody = new HashMap<>();
        authorBody.put("id", authorId);
        authorBody.put("name", "No One updated");

        given()
                .contentType("application/json")
                .body(authorBody)
                .when()
                .put("/authors")
                .then()
                .statusCode(anyOf(equalTo(200), equalTo(204)));
    }

    @Test
    public void testDeleteAuthor() {
        Long tagIdToDelete = authorId;

        given()
                .contentType("application/json")
                .when()
                .delete("/authors/" + tagIdToDelete)
                .then()
                .statusCode(204);

        given()
                .contentType("application/json")
                .when()
                .get("/authors/" + tagIdToDelete)
                .then()
                .statusCode(404);
    }


}

