package com.dcc.api.utiility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Builder;

import java.util.HashMap;
import java.util.Map;

@Builder()
public class ResponseHandler {

    public static ResponseEntity<Object> responseBuilder(
            String message, HttpStatus httpStatus, Object object
    ) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("status", httpStatus.value());
        map.put("items", object);
        return new ResponseEntity<>(map, httpStatus);
    }
}
