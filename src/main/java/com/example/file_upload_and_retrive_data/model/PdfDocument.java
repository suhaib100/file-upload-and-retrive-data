package com.example.file_upload_and_retrive_data.model;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "pdf_documents")
@Data
public class PdfDocument {

    @Id
    private String id;
    private String filename;
    private String content;
}
