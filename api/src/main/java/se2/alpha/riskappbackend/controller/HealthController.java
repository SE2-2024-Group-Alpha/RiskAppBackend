package se2.alpha.riskappbackend.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public String hello() {
        return "Api healthy!";
    }
}