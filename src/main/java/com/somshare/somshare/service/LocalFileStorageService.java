package com.somshare.somshare.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Service
public class LocalFileStorageService {

    private final Path uploadDir;
    private static final Set<String> ALLOWED_MIME = Set.of("application/pdf");

    public LocalFileStorageService(@Value("${app.upload-dir}") String uploadDir) {
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    public StoredFile storePdf(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }

        String contentType = file.getContentType();
        String originalName = StringUtils.cleanPath(file.getOriginalFilename() == null ? "" : file.getOriginalFilename());

        boolean mimeOk = contentType != null && ALLOWED_MIME.contains(contentType);
        boolean extOk = originalName.toLowerCase().endsWith(".pdf");

        if (!mimeOk && !extOk) {
            throw new IllegalArgumentException("PDF 파일만 업로드할 수 있습니다.");
        }

        Files.createDirectories(uploadDir);

        String safeOriginal = originalName.replaceAll("[\\\\/:*?\"<>|]", "_");
        String storedName = UUID.randomUUID() + "-" + safeOriginal;

        Path target = uploadDir.resolve(storedName).normalize();
        if (!target.startsWith(uploadDir)) {
            throw new IllegalArgumentException("잘못된 파일 경로입니다.");
        }

        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        String url = "/uploads/" + storedName;

        return new StoredFile(originalName, storedName, url, file.getSize());
    }

    public record StoredFile(String originalName, String storedName, String url, long size) {}
}
