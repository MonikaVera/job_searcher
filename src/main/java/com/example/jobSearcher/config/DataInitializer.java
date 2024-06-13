package com.example.jobSearcher.config;

import com.example.jobSearcher.entity.Job;
import com.example.jobSearcher.entity.User;
import com.example.jobSearcher.repo.JobRepository;
import com.example.jobSearcher.repo.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Component
public class DataInitializer {
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    @Autowired
    public DataInitializer(UserRepository userRepository, JobRepository jobRepository) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    @PostConstruct
    public void init() {
        String[] names = {"Kovacs Janos", "Horvath Regina", "Nemeth Barbara"};
        IntStream.range(1, 10).forEach(i -> {
            User user = new User(names[i%3] , "myname" + i + "@example.com");
            userRepository.save(user);
        });

        String[] jobs = {"Java Backend Developer", "React Frontend Developer", "UI/UX Designer"};
        String[] locations  = {"Budapest", "Berlin", "London", "Madrid"};
        IntStream.range(1, 10).forEach(i -> {
            Job job = new Job(jobs[i%3], locations[i%4]);
            jobRepository.save(job);
        });
    }
}
