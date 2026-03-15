package com.cocoland.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class FileService {

    @Value("${app.upload.dir:uploads/}")
    private String uploadDir;

    public String save(MultipartFile file) throws IOException {
        Path dir = Paths.get(uploadDir).toAbsolutePath();
        Files.createDirectories(dir);

        String ext = "";
        String original = file.getOriginalFilename();
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf("."));
        }
        String filename = UUID.randomUUID() + ext;
        Path dest = dir.resolve(filename);
        file.transferTo(dest.toFile());
        log.info("Saved file: {}", dest);
        return filename;
    }

    public void delete(String filename) {
        if (filename == null || filename.isBlank()) return;
        try {
            Path path = Paths.get(uploadDir).toAbsolutePath().resolve(filename);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            log.warn("Could not delete file {}: {}", filename, e.getMessage());
        }
    }
}
