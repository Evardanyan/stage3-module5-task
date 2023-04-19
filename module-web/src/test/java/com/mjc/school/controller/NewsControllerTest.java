package com.mjc.school.controller;


import com.mjc.school.service.impl.CommentService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
//import org.testng.annotations.Test;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

public class NewsControllerTest {

    private Long commentId;

    private Long newsId;

    private Long authorId;


    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/api/v1";

        Map<String, Object> authorBody = new HashMap<>();
        authorBody.put("name", "AuthorNoOne");

        int aId = given()
                .contentType("application/json")
                .body(authorBody)
                .when().post("/authors")
                .then()
                .statusCode(201)
                .extract().path("id");

        authorId = Long.valueOf(aId);

        Map<String, Object> newsBody = new HashMap<>();
        newsBody.put("title", "News No One");
        newsBody.put("content", "News no One");
        newsBody.put("authorId", authorId);
//        newsBody.put("tagId", null);

        int nId = given()
                .contentType("application/json")
                .body(newsBody)
                .when().post("/news")
                .then()
                .statusCode(201)
                .extract().path("id");

        newsId = Long.valueOf(nId);

        System.out.println("Created news ID: " + newsId);


        Map<String, Object> commentBody = new HashMap<>();
        commentBody.put("content", "No One");
        commentBody.put("newsId", newsId);

        int id = given()
                .contentType("application/json")
                .body(commentBody)
                .when().post("/comments")
                .then()
                .statusCode(201)
                .extract().path("id");

        commentId = Long.valueOf(id);




    }

    @Test
    public void testGetNews() {
        given().get("/news").then().assertThat().statusCode(200);
    }


    @Test
    public void testGetNewsById() {
        given().get("/news/" + newsId).then().assertThat().statusCode(200);
    }

    @Test
    public void testCreateNews() {
        Map<String, Object> newsBody = new HashMap<>();
        newsBody.put("title", "News No One");
        newsBody.put("content", "News No One");
        newsBody.put("authorId", authorId);


        given()
                .contentType("application/json")
                .body(newsBody)
                .when().post("/news").then()
                .statusCode(201);
    }


    @Test
    public void testUpdateNews() {
        Map<String, Object> newsBody = new HashMap<>();
        newsBody.put("id", newsId);
        newsBody.put("title", "No One Updated");
        newsBody.put("content", "No One Updated");
        newsBody.put("authorId", authorId);

        given()
                .contentType("application/json")
                .body(newsBody)
                .when()
                .put("/news")
                .then()
                .statusCode(anyOf(equalTo(200), equalTo(204)));
    }

    @Test
    public void testDeleteNews() {
        Long newsIdToDelete = newsId;

        given()
                .contentType("application/json")
                .when()
                .delete("/comments/" + commentId)
                .then()
                .statusCode(204);

        given()
                .contentType("application/json")
                .when()
                .delete("/news/" + newsIdToDelete)
                .then()
                .statusCode(204);

        given()
                .contentType("application/json")
                .when()
                .get("/news/" + newsIdToDelete)
                .then()
                .statusCode(404);

        given()
                .contentType("application/json")
                .when()
                .delete("/authors/" + authorId)
                .then()
                .statusCode(204);
    }


}

