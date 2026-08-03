package com.insurance.backend.service;

import com.insurance.backend.dto.response.SearchResultDto;

public interface SearchService {
    SearchResultDto globalSearch(String query);
}
