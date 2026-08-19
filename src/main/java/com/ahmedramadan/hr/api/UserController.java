package com.ahmedramadan.hr.api;

import com.ahmedramadan.hr.api.dto.CreateUserRequest;
import com.ahmedramadan.hr.api.dto.PageResponse;
import com.ahmedramadan.hr.api.dto.UserResponse;
import com.ahmedramadan.hr.domain.RoleName;
import com.ahmedramadan.hr.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "List users with database-backed pagination")
    public PageResponse<UserResponse> list(@PageableDefault(sort = "id") Pageable pageable) {
        return userService.list(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get one user")
    public UserResponse get(@PathVariable Long id) {
        return userService.get(id);
    }

    @GetMapping("/by-role/{role}")
    @Operation(summary = "List users by role")
    public PageResponse<UserResponse> listByRole(
            @PathVariable RoleName role,
            @PageableDefault(sort = "id") Pageable pageable
    ) {
        return userService.listByRole(role, pageable);
    }

    @PostMapping
    @Operation(summary = "Create a user; administrator role required")
    public ResponseEntity<UserResponse> create(
            @Valid @RequestBody CreateUserRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        UserResponse created = userService.create(request);
        return ResponseEntity.created(
                uriBuilder.path("/api/v1/users/{id}").buildAndExpand(created.id()).toUri()
        ).body(created);
    }
}
