package com.nivor.search;import java.time.LocalDateTime;
public final class SearchDtos{private SearchDtos(){}public record Result(String type,Long id,String title,String description,String path,LocalDateTime updatedAt){}public record SearchResponse(java.util.List<Result> results){}}
