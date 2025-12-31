package com.somshare.somshare.dto;

public record UploadResponse(
        String originalName,
        String storedName,
        String url,
        long size
) {}