package com.baloot.dataAccess.utils;

public record QueryModel(Integer page, Integer limit, String search, String searchType, String sort,
                         Boolean available) {
}
