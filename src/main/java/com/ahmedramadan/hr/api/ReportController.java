package com.ahmedramadan.hr.api;

import com.ahmedramadan.hr.api.dto.PageResponse;
import com.ahmedramadan.hr.api.dto.UserReportResponse;
import com.ahmedramadan.hr.domain.RoleName;
import com.ahmedramadan.hr.service.DirectoryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final DirectoryService directoryService;

    public ReportController(DirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    @GetMapping("/users")
    @Operation(summary = "Generate a paginated user report by role")
    public PageResponse<UserReportResponse> users(
            @RequestParam(defaultValue = "JOB_SEEKER") RoleName role,
            @PageableDefault(sort = "id") Pageable pageable
    ) {
        return directoryService.userReport(role, pageable);
    }
}
