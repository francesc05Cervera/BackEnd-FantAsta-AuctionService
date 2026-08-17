package com.example.fantasta.auction_service.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ping")
public class ping 
{
    @GetMapping
    public ResponseEntity<String> ping() 
    {
        return ResponseEntity.status(HttpStatus.OK).body("Server is up and running!");
    }
}
