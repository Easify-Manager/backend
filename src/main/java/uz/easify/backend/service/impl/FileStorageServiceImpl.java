package uz.easify.backend.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import uz.easify.backend.exception.FileStorageException;
import uz.easify.backend.service.FileStorageService;

/**
 * Implementation of FileStorageService for managing file uploads and storage.
 */
@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageServiceImpl(@Value("${app.file-storage.base-path}") String baseStoragePath) {
        this.fileStorageLocation = Paths.get(baseStoragePath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
            log.info("File storage location initialized at: {}", this.fileStorageLocation);
        } catch (IOException ex) {
            throw new FileStorageException("Could not create file storage directory", ex);
        }
    }

    @Override
    public String storeFile(MultipartFile file, String directory) {
        // Validate file
        if (file.isEmpty()) {
            throw new FileStorageException("Cannot store empty file");
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        
        try {
            // Check for invalid characters in filename
            if (originalFilename.contains("..")) {
                throw new FileStorageException("Filename contains invalid path sequence: " + originalFilename);
            }

            // Generate unique filename
            String fileExtension = getFileExtension(originalFilename);
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
            
            // Create directory if it doesn't exist
            Path targetLocation = this.fileStorageLocation.resolve(directory);
            Files.createDirectories(targetLocation);
            
            // Store file
            Path filePath = targetLocation.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            String relativePath = directory + "/" + uniqueFilename;
            log.info("File stored successfully: {}", relativePath);
            
            return relativePath;
            
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file: " + originalFilename, ex);
        }
    }

    @Override
    public void deleteFile(String filePath) {
        try {
            Path file = this.fileStorageLocation.resolve(filePath).normalize();
            
            // Security check: ensure file is within storage location
            if (!file.startsWith(this.fileStorageLocation)) {
                throw new FileStorageException("Cannot delete file outside storage directory");
            }
            
            Files.deleteIfExists(file);
            log.info("File deleted successfully: {}", filePath);
            
        } catch (IOException ex) {
            log.error("Could not delete file: {}", filePath, ex);
            throw new FileStorageException("Could not delete file: " + filePath, ex);
        }
    }

    @Override
    public Path getFilePath(String filePath) {
        return this.fileStorageLocation.resolve(filePath).normalize();
    }

    @Override
    public String getFileUrl(String filePath) {
        // In a production environment, this would return a full URL
        // For now, return a relative API path
        return "/api/files/" + filePath;
    }

    /**
     * Extract file extension from filename.
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return filename.substring(lastDotIndex);
    }
}
