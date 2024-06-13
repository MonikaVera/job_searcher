package com.example.jobSearcher.repo;

import com.example.jobSearcher.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {
    @Query("SELECT j.id FROM Job j WHERE LOWER(j.location) = LOWER(:location) AND LOWER(j.jobName) LIKE CONCAT('%', LOWER(:keyword), '%')")
    List<UUID> searchJobs(@Param("keyword") String keyword, @Param("location") String location);
}
