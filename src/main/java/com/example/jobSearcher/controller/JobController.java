package com.example.jobSearcher.controller;
import com.example.jobSearcher.dto.AuthRequest;
import com.example.jobSearcher.dto.JobRequest;
import com.example.jobSearcher.dto.SearchRequest;
import com.example.jobSearcher.entity.Job;
import com.example.jobSearcher.service.JobService;
import com.example.jobSearcher.service.UserService;
import com.example.jobSearcher.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class JobController {
    @Autowired
    UserService userService;
    @Autowired
    JobService jobService;
    @Autowired
    ValidationService validationService;
    private final static String positionUrl = "http://localhost:8080/position/" ;

    @PostMapping("/client")
    public ResponseEntity<?> register(@RequestBody AuthRequest registerRequest) {
        Optional<String> error = validationService.createAuthRequestError(registerRequest);
        if(error.isPresent()) {
            return ResponseEntity.badRequest().body(error.get());
        }

        UUID userId = userService.register(registerRequest.getUsername(), registerRequest.getEmail());
        return ResponseEntity.ok(userId);
    }

    @PostMapping("/position")
    public ResponseEntity<?> createPosition(@RequestHeader("X-API-KEY") String apiKey,
                                            @RequestBody JobRequest jobRequest)
    {
        Optional<String> apikeyError = validationService.createApiKeyError(apiKey);
        if(apikeyError.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apikeyError.get());
        }

        Optional<String> error = validationService.createJobRequestError(jobRequest);
        if(error.isPresent()) {
            return ResponseEntity.badRequest().body(error.get());
        }

        UUID jobId = jobService.registerJob(jobRequest.getJobName(), jobRequest.getLocation());
        String url = positionUrl + jobId;
        return ResponseEntity.ok(url);
    }

    @GetMapping("/position/{id}")
    public ResponseEntity<?> getPosition(@PathVariable String id,
                                         @RequestHeader("X-API-KEY") String apiKey) {
        Optional<String> apikeyError = validationService.createApiKeyError(apiKey);
        if(apikeyError.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apikeyError.get());
        }

        UUID jobId;
        try {
            jobId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect position Id!");
        }

        Job job = jobService.getJob(jobId);
        if(job==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Position not found!");
        }
        return ResponseEntity.ok(job);
    }

    @GetMapping("/position/search")
    public ResponseEntity<?> searchPosition(@RequestHeader("X-API-KEY") String apiKey,
                                            @RequestBody SearchRequest searchRequest) {
        Optional<String> apikeyError = validationService.createApiKeyError(apiKey);
        if(apikeyError.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apikeyError.get());
        }

        Optional<String> error = validationService.createSearchRequestError(searchRequest);
        if(error.isPresent()) {
            return ResponseEntity.badRequest().body(error.get());
        }

        List<UUID> jobs = jobService.searchJobs(searchRequest.getKeyword(), searchRequest.getLocation());
        List<String> urls = new ArrayList<>();
        for(UUID id : jobs) {
            urls.add(positionUrl + id);
        }
        return ResponseEntity.ok().body(urls);
    }
}
