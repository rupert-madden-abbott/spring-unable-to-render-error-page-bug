package com.github.rma.utrepb;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {

    @GetMapping("/secure")
    public ResponseEntity<Void> secure() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unsecure")
    public ResponseEntity<Void> unsecure() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unsecure-error")
    public ResponseEntity<Void> unsecureError() {
        throw new RuntimeException();
    }
}
