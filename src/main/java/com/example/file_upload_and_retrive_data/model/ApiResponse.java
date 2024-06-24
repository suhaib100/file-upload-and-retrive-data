package com.example.file_upload_and_retrive_data.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ApiResponse {
    private int status;
    private String message;
    private Object doc;


    private long totalElementsInDb;
    private int limit;
    private boolean hasPrevPage;
    private boolean hasNextPage;
    private long numberOfElementsInCurrentPage;
    private int page;
    private int totalPages;
    private long offset;
    private Integer prevPage;
    private Integer nextPage;
    private long pagingCounter;

    public ApiResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ApiResponse(int status, String message, Object doc) {
        this.status = status;
        this.message = message;
        this.doc = doc;
    }



    public ApiResponse(int status, String message, Object doc, long totalElementsInDb, int limit,
                       boolean hasPrevPage, boolean hasNextPage, long numberOfElementsInCurrentPage,
                       int page, int totalPages, long offset, Integer prevPage, Integer nextPage,
                       long pagingCounter) {
        this.status = status;
        this.message = message;
        this.doc = doc;
        this.totalElementsInDb = totalElementsInDb;
        this.limit = limit;
        this.hasPrevPage = hasPrevPage;
        this.hasNextPage = hasNextPage;
        this.numberOfElementsInCurrentPage = numberOfElementsInCurrentPage;
        this.page = page;
        this.totalPages = totalPages;
        this.offset = offset;
        this.prevPage = prevPage;
        this.nextPage = nextPage;
        this.pagingCounter = pagingCounter;
    }
}
