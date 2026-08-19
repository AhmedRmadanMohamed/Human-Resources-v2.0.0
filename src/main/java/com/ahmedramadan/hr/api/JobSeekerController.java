package com.ahmedramadan.hr.api;

import com.ahmedramadan.hr.api.dto.JobSeekerResponse;
import com.ahmedramadan.hr.api.dto.PageResponse;
import com.ahmedramadan.hr.service.DirectoryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/job-seekers")
public class JobSeekerController {

    private final DirectoryService directoryService;

    public JobSeekerController(DirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    @GetMapping
    @Operation(summary = "List job-seeker profiles, optionally filtered by desired position")
    public PageResponse<JobSeekerResponse> list(
            @RequestParam(required = false) String position,
            @PageableDefault(sort = "id") Pageable pageable
    ) {
        if (position == null || position.isBlank()) {
            return directoryService.listJobSeekers(pageable);
        }
        return directoryService.listJobSeekersByPosition(position.trim(), pageable);
    }
}
