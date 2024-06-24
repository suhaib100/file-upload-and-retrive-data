package com.example.file_upload_and_retrive_data.controller;

import com.example.file_upload_and_retrive_data.model.ApiResponse;
import com.example.file_upload_and_retrive_data.model.PdfDocument;
import com.example.file_upload_and_retrive_data.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pdf")

public class PdfController {

    @Autowired
    private PdfService pdfService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> uploadPdf(@RequestParam("file") MultipartFile file) {
        PdfDocument pdfDocument = pdfService.extractTextFromPdf(file);

        if (pdfDocument == null) {
            return ResponseEntity.status(404).body(new ApiResponse(404, "data not found"));

        }

        return ResponseEntity.ok().body(new ApiResponse(200, "success", pdfDocument));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getPdfById(@PathVariable String id) {
        PdfDocument pdfDocument = pdfService.getPdfDocumentById(id);

        if (pdfDocument == null) {
            return ResponseEntity.status(404).body(new ApiResponse(404, "data not found"));
        }

        return ResponseEntity.ok().body(new ApiResponse(200, "success", pdfDocument));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllPdfs() {
        List<PdfDocument> pdfDocuments = pdfService.getAllPdfDocuments();

        if (pdfDocuments.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse(404, "no data found"));
        }

        return ResponseEntity.ok().body(new ApiResponse(200, "success", pdfDocuments));
    }




    @PostMapping("/search")
    public ResponseEntity<ApiResponse> searchPdfs(@RequestBody Map<String, Object> searchParams) {
        int page = (int) searchParams.getOrDefault("page", 0);
        int limit = (int) searchParams.getOrDefault("limit", 10);
        Map<String, String> searchCriteria = (Map<String, String>) searchParams.get("search");

        Pageable pageable = PageRequest.of(page, limit);
        Page<PdfDocument> pdfDocuments = pdfService.search(searchCriteria, pageable);

        if (pdfDocuments.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse(404, "no data found"));
        }

        // Populate pagination-related fields
        long totalElementsInDb = pdfDocuments.getTotalElements();
        boolean hasPrevPage = pdfDocuments.hasPrevious();
        boolean hasNextPage = pdfDocuments.hasNext();
        long numberOfElementsInCurrentPage = pdfDocuments.getNumberOfElements();
        int totalPages = pdfDocuments.getTotalPages();
        long offset = pageable.getOffset();
        Integer prevPage = pdfDocuments.hasPrevious() ? pdfDocuments.previousPageable().getPageNumber() : null;
        Integer nextPage = pdfDocuments.hasNext() ? pdfDocuments.nextPageable().getPageNumber() : null;
        long pagingCounter = pdfDocuments.getTotalElements() - pageable.getOffset();

        ApiResponse response = new ApiResponse(200, "success", pdfDocuments.getContent(),
                totalElementsInDb, limit, hasPrevPage, hasNextPage, numberOfElementsInCurrentPage,
                page, totalPages, offset, prevPage, nextPage, pagingCounter);

        return ResponseEntity.ok().body(response);
    }
}
