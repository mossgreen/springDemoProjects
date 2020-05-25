package com.mossj.springsecurity;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @GetMapping
    public String findAll() {
        return "list";
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('delete')")
    public Long delete(@PathVariable Long id) {
        return id;
    }
}
