package com.somshare.somshare.controller;

import com.somshare.somshare.dto.UploadResponse;
import com.somshare.somshare.service.LocalFileStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final LocalFileStorageService storageService;

    public FileController(LocalFileStorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<UploadResponse> upload(@RequestParam("file") MultipartFile file) throws Exception {
        var stored = storageService.storePdf(file);
        return ResponseEntity.ok(new UploadResponse(
                stored.originalName(),
                stored.storedName(),
                stored.url(),
                stored.size()
        ));
    }
}