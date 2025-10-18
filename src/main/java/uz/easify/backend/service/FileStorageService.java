package uz.easify.backend.service;

import java.nio.file.Path;

import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for file storage operations.
 * Handles storing, retrieving, and deleting files from the filesystem.
 */
public interface FileStorageService {

    /**
     * Store a file in the filesystem.
     *
     * @param file the file to store
     * @param directory the subdirectory to store the file in
     * @return the path where the file was stored
     */
    String storeFile(MultipartFile file, String directory);

    /**
     * Delete a file from the filesystem.
     *
     * @param filePath the path of the file to delete
     */
    void deleteFile(String filePath);

    /**
     * Get the full path to a file.
     *
     * @param filePath the relative file path
     * @return the full Path object
     */
    Path getFilePath(String filePath);

    /**
     * Generate a public URL for accessing a file.
     *
     * @param filePath the relative file path
     * @return the public URL
     */
    String getFileUrl(String filePath);
}
