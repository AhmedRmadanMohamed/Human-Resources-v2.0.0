package com.ahmedramadan.hr.api;

import com.ahmedramadan.hr.api.dto.EmployerResponse;
import com.ahmedramadan.hr.api.dto.PageResponse;
import com.ahmedramadan.hr.service.DirectoryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employers")
public class EmployerController {

    private final DirectoryService directoryService;

    public EmployerController(DirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    @GetMapping
    @Operation(summary = "List employer profiles")
    public PageResponse<EmployerResponse> list(@PageableDefault(sort = "id") Pageable pageable) {
        return directoryService.listEmployers(pageable);
    }
}
