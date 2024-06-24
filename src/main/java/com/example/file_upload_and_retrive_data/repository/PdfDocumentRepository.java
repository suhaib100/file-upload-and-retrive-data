package com.example.file_upload_and_retrive_data.repository;
import com.example.file_upload_and_retrive_data.model.PdfDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.mongodb.repository.Query;

@Repository
public interface PdfDocumentRepository extends MongoRepository<PdfDocument, String>{

    @Query("{ 'content': { $regex: ?0, $options: 'i' } }")
    List<PdfDocument> findByContentRegex(String regex);

    List<PdfDocument> findByFilenameContainingIgnoreCase(String filename);

}
