package com.alan10607.leaf.util;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@Component
public class ResponseUtil {
    private Map<String, Object> body = new LinkedHashMap<>();

    /**
     * response 200 ok
     * @return
     */
    public ResponseEntity ok() {
        return ok(null);
    }

    public ResponseEntity ok(Object result) {
        body.put("status", OK.toString());
        body.put("result", result);
        return ResponseEntity.ok().body(body);
    }

    /**
     * response 400 bad_request
     * @return
     */
    public ResponseEntity err(Exception e) {
        body.put("status", BAD_REQUEST.toString());
        body.put("result", e.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

}