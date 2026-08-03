package com.insurance.backend.controller;

import com.insurance.backend.dto.response.ApiResponse;
import com.insurance.backend.dto.response.SearchResultDto;
import com.insurance.backend.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<ApiResponse<SearchResultDto>> globalSearch(@RequestParam String query) {
        return ResponseEntity.ok(ApiResponse.<SearchResultDto>builder()
                .success(true)
                .message("Search completed")
                .data(searchService.globalSearch(query))
                .build());
    }
}
