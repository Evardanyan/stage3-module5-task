package com.mjc.school.controller.exception;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;

@RestController
@RequestMapping("${server.error.path:${error.path:/error}}")
public class CustomErrorController implements ErrorController {

    @Value("${server.port}")
    private int serverPort;


//    @ApiOperation(value = "Handle errors")
//    @ApiResponses(value = {
//            @ApiResponse(code = 400, message = "Bad request"),
//            @ApiResponse(code = 404, message = "Not found"),
//            @ApiResponse(code = 500, message = "Internal server error")
//    })
//    public ResponseEntity<CustomErrorResponse> handleError(HttpServletRequest request) {
//        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
//        String message = "An error occurred.";
//
//        if (statusCode != null) {
//            httpStatus = HttpStatus.valueOf(statusCode);
//            switch (httpStatus) {
//                case NOT_FOUND:
//                    message = "The requested URL was not found.";
//                    break;
//                case BAD_REQUEST:
//                    message = "The request is invalid.";
//                    break;
//            }
//        }


    @RequestMapping
    @ApiOperation(value = "Handle errors")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<CustomErrorResponse> handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "An error occurred.";

        if (statusCode != null) {
            httpStatus = HttpStatus.valueOf(statusCode);
            switch (httpStatus) {
                case NOT_FOUND:
                    message = "The requested URL was not found.";
                    break;
                case BAD_REQUEST:
                    message = "The request is invalid.";
                    break;
                case UNAUTHORIZED:
                    message = "Invalid username/password.";
                    break;
                case FORBIDDEN:
                    message = "You don't have permission to access this resource.";
                    break;
            }
        }

        CustomErrorResponse errorResponse = new CustomErrorResponse();
        errorResponse.setStatus(httpStatus.value());
        errorResponse.setMessage(message);
        errorResponse.setTimestamp(OffsetDateTime.now());

        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ApiOperation(value = "Get error path")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved error path")
    })
    public String getErrorPath() {
        return ":" + serverPort + "/error";
    }

}


