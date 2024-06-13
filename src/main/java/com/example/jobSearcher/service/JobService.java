package com.example.jobSearcher.service;

import com.example.jobSearcher.entity.Job;
import com.example.jobSearcher.repo.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class JobService {
    JobRepository jobRepository;

    @Autowired
    public JobService(JobRepository jobRepository) {
        this.jobRepository=jobRepository;
    }

    public UUID registerJob(String jobName, String location) {
        Job job = new Job(jobName, location);
        jobRepository.save(job);
        return job.getId();
    }

    public Job getJob(UUID id) {
        return jobRepository.findById(id).orElse(null);
    }

    public List<UUID> searchJobs(String keyword, String location) {
        return jobRepository.searchJobs(keyword, location);
    }
}
