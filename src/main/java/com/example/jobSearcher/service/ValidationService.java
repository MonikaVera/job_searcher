package com.example.jobSearcher.service;

import com.example.jobSearcher.dto.AuthRequest;
import com.example.jobSearcher.dto.JobRequest;
import com.example.jobSearcher.dto.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ValidationService {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    @Autowired
    UserService userService;

    public Optional<String> createApiKeyError(String apikey) {
        UUID key;
        try {
            key = UUID.fromString(apikey);
        } catch (IllegalArgumentException e) {
            return Optional.of("Incorrect api key format!");
        }
        if(!userService.isUserIdExists(key)) {
            return Optional.of("Invalid api key!");
        }
        return Optional.empty();
    }

    public Optional<String> createAuthRequestError(AuthRequest registerRequest) {
        String userName = registerRequest.getUsername();
        String email = registerRequest.getEmail();

        Optional<String> userNameError = createErrorMessage(userName, 100, "Username");
        Optional<String> emailError = Optional.empty();
        if(email==null) {
            emailError = Optional.of("Email is null!");
        } else if(email.isEmpty()) {
            emailError = Optional.of("Email is empty!");
        } else if(userService.isEmailRegistered(email)) {
            emailError = Optional.of("Email is already registered!");
        } else {
            Pattern pattern = Pattern.compile(EMAIL_REGEX);
            Matcher matcher = pattern.matcher(email);
            if(!matcher.matches()) {
                emailError = Optional.of("Incorrect email format!");
            }
        }

        if(userNameError.isPresent() && emailError.isPresent()) {
            return Optional.of(userNameError.get() + " " + emailError.get());
        }
        if(userNameError.isPresent()) {
            return userNameError;
        }
        return emailError;
    }

    public Optional<String> createJobRequestError(JobRequest jobRequest) {
        String jobName = jobRequest.getJobName();
        String location = jobRequest.getLocation();
        Optional<String> errorJobName= createErrorMessage(jobName, 50, "Position name");
        Optional<String> errorLocation = createErrorMessage(location, 50, "Location");
        if(errorJobName.isPresent() && errorLocation.isPresent()) {
            return Optional.of(errorJobName.get() + " " + errorLocation.get());
        }
        if(errorJobName.isPresent()) {
            return errorJobName;
        }
        return errorLocation;
    }

    public Optional<String> createSearchRequestError(SearchRequest searchRequest) {
        String keyword = searchRequest.getKeyword();
        String location = searchRequest.getLocation();
        Optional<String> errorKeyword = createErrorMessage(keyword, 50, "Keyword");
        Optional<String> errorLocation = createErrorMessage(location, 50, "Location");
        if(errorKeyword.isPresent() && errorLocation.isPresent()) {
            return Optional.of(errorKeyword.get() + " " + errorLocation.get());
        }
        if(errorKeyword.isPresent()) {
            return errorKeyword;
        }
        return errorLocation;
    }

    private Optional<String> createErrorMessage(String str, int maxLength, String nameOfStr) {
        if(str==null) return Optional.of(nameOfStr + " is null!");
        if(str.isEmpty()) return Optional.of(nameOfStr + " is empty!");
        if(str.length()>maxLength) return Optional.of(nameOfStr + " exceeds the maximum length of " + maxLength + " characters!");
        return Optional.empty();
    }
}
