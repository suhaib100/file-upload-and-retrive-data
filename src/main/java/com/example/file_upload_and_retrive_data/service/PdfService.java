package com.example.file_upload_and_retrive_data.service;
import com.example.file_upload_and_retrive_data.exception.FileStorageException;
import com.example.file_upload_and_retrive_data.model.PdfDocument;
import com.example.file_upload_and_retrive_data.repository.PdfDocumentRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@Service
public class PdfService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PdfDocumentRepository pdfDocumentRepository;

    public PdfDocument extractTextFromPdf(MultipartFile file) {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document).trim();

            if (text.isEmpty()) {
                return null;
            }

            // Save PDF document details to MongoDB
            PdfDocument pdfDocument = new PdfDocument();
            pdfDocument.setFilename(file.getOriginalFilename());
            pdfDocument.setContent(text);
            return pdfDocumentRepository.save(pdfDocument);
        } catch (IOException e) {
            throw new FileStorageException("Could not read PDF file: " + file.getOriginalFilename(), e);
        }
    }



    public PdfDocument getPdfDocumentById(String id) {
        Optional<PdfDocument> pdfDocument = pdfDocumentRepository.findById(id);
        return pdfDocument.orElse(null);
    }

    public List<PdfDocument> getAllPdfDocuments() {
        return pdfDocumentRepository.findAll();
    }

    public Page<PdfDocument> search(Map<String, String> searchCriteria, Pageable pageable) {
        Query query = new Query();

        // Build criteria for regex search on each field in searchCriteria
        List<Criteria> criteriaList = new ArrayList<>();
        searchCriteria.forEach((key, value) -> {
            if (!StringUtils.isEmpty(value)) {
                criteriaList.add(Criteria.where(key).regex(value, "i")); // Case insensitive regex search
            }
        });

        // Combine all criteria with AND operator
        if (!criteriaList.isEmpty()) {
            Criteria criteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));
            query.addCriteria(criteria);
        }

        // Add pagination and sorting to the query
        query.with(pageable);

        // Execute query to get matching documents
        List<PdfDocument> pdfDocuments = mongoTemplate.find(query, PdfDocument.class);

        // Count total number of documents matching the query criteria
        long count = mongoTemplate.count(query, PdfDocument.class);

        // Return a Page object containing results, pagination information, and total count
        return new PageImpl<>(pdfDocuments, pageable, count);
    }

}
