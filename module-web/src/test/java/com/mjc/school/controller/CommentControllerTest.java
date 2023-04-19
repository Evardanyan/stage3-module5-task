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

public class CommentControllerTest {
    private CommentService commentService;

    private Long commentId;

    private Long newsId;

    private long authorId;


    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/api/v1";

        Map<String, Object> authorBody = new HashMap<>();
        authorBody.put("name", "No One");

        int aId = given()
                .contentType("application/json")
                .body(authorBody)
                .when().post("/authors")
                .then()
                .statusCode(201)
                .extract().path("id");

        authorId = Long.valueOf(aId);

        Map<String, Object> newsBody = new HashMap<>();
        newsBody.put("title", "No One");
        newsBody.put("content", "No One");
        newsBody.put("authorId", authorId);

        int nId = given()
                .contentType("application/json")
                .body(newsBody)
                .when().post("/news")
                .then()
                .statusCode(201)
                .extract().path("id");

        newsId = Long.valueOf(nId);


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

        System.out.println("Created comment ID: " + id);


    }

    @Test
    public void testGetComments() {
        given().get("/comments").then().assertThat().statusCode(200);
    }


    @Test
    public void testGetCommentsById() {
        given().get("/comments/" + commentId).then().assertThat().statusCode(200);
    }

    @Test
    public void testCreateComment() {
        Map<String, Object> commentBody = new HashMap<>();
        commentBody.put("content", "No One");
        commentBody.put("newsId", newsId);

        given()
                .contentType("application/json")
                .body(commentBody)
                .when().post("/comments").then()
                .statusCode(201);

    }


    @Test
    public void testUpdateComment() {
        Map<String, Object> commentBody = new HashMap<>();
        commentBody.put("id", commentId);
        commentBody.put("content", "No One updated");
        commentBody.put("newsId", newsId);

        given()
                .contentType("application/json")
                .body(commentBody)
                .when()
                .put("/comments")
                .then()
                .statusCode(anyOf(equalTo(200), equalTo(204)));
    }

    @Test
    public void testDeleteComment() {
        Long commentIdToDelete = commentId;

        given()
                .contentType("application/json")
                .when()
                .delete("/comments/" + commentIdToDelete)
                .then()
                .statusCode(204);

        given()
                .contentType("application/json")
                .when()
                .get("/comments/" + commentIdToDelete)
                .then()
                .statusCode(404);

        given()
                .contentType("application/json")
                .when()
                .delete("/news/" + newsId)
                .then()
                .statusCode(204);

        given()
                .contentType("application/json")
                .when()
                .delete("/authors/" + authorId)
                .then()
                .statusCode(204);
    }


}
