package com.automation.platform.shared.services.api;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class RestResource {

    public static Response post(String path, int port ,Object request){
        return given(SpecBuilder.getRequestSpec()).
                port(port).
                body(request).
        when().post(path).
        then().spec(SpecBuilder.getResponseSpec()).
                extract().
                response();
    }

    public static Response get(String path, String token){
        return given(SpecBuilder.getRequestSpec()).
                auth().oauth2(token).
        when().get(path).
        then().spec(SpecBuilder.getResponseSpec()).
                extract().
                response();
    }

}
