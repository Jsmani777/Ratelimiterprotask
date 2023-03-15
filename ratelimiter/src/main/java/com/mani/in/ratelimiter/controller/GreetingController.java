package com.mani.in.ratelimiter.controller;


import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mani.in.ratelimiter.model.ResponseType;
import com.mani.in.ratelimiter.response.ComputeResponse;

@RestController
public class GreetingController {
	
	
	@GetMapping("/double/{input}")
    public ComputeResponse doubleValue(@PathVariable int input){
        return ComputeResponse.of(input, 2*input, ResponseType.SUCCESS, Strings.EMPTY);
    }

    @GetMapping("/square/{input}")
    @RateLimiter(name = "squareLimit", fallbackMethod = "squareErrorResponse")
    public ComputeResponse getValue(@PathVariable int input){
        return ComputeResponse.of(input, input * input, ResponseType.SUCCESS, Strings.EMPTY);
    }

    public ComputeResponse squareErrorResponse(int input, Throwable throwable){
        return ComputeResponse.of(input, -1, ResponseType.FAILURE, throwable.getMessage());
    }

    @GetMapping("/greeting")
    @RateLimiter(name = "greetingRateLimit", fallbackMethod = "greetingFallBack")
    public ResponseEntity<String> greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return ResponseEntity.ok().body("Hello World: " + name);
    }


    public ResponseEntity<String> greetingFallBack(String name, io.github.resilience4j.ratelimiter.RequestNotPermitted ex) {
        System.out.println("Rate limit applied no further calls are accepted");

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Retry-After", "1"); //retry after one second

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .headers(responseHeaders) //send retry header
                .body("Too many request - No further calls are accepted");
    }
}
