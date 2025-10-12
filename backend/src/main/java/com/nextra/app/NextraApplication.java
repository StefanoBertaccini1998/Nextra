package com.nextra.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
public class NextraApplication {
    public static void main(String[] args) {
        SpringApplication.run(NextraApplication.class, args);
    }
}

@RestController
@RequestMapping("/api/health")
class HealthController {
    @GetMapping
    public String health() {
        return "Ciao Aurora XD";
    }
}
