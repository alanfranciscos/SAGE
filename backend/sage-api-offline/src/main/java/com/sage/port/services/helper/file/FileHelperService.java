package com.sage.port.services.helper.file;

import com.sage.model.file.FileType;

/**
 * FileHelperService provides methods to assist with file operations. This
 * interface can be extended to include methods for file handling, such as
 * reading, writing, and processing files.
 *
 * @author Alan Francisco Silva
 * @version 1.0
 */
public interface FileHelperService {

    /**
     * Saves the provided file data to a file with the specified name.
     *
     * @param fileData The byte array containing the file data.
     * @param fileName The name of the file to save.
     * @return The path where the file was saved, or null if the file could not
     * be saved.
     * @throws IllegalArgumentException if the file type is not supported.
     */
    public String saveBase64File(String base64, FileType fileType, String fileName) throws IllegalArgumentException;

    public String getBase64File(String path) throws IllegalArgumentException;

    /**
     * Reads the content of a file with the specified name.
     *
     * @param fileName The name of the file to read.
     * @return The byte array containing the file content, or null if the file
     * does not exist or cannot be read.
     */
    public byte[] readFile(String fileName);

    /**
     * Deletes the file with the specified name.
     *
     * @param fileName The name of the file to delete.
     * @return true if the file was deleted successfully, false otherwise.
     */
    public boolean deleteFile(String fileName);

    /**
     * Checks if the file with the specified name exists.
     *
     * @param fileName The name of the file to check.
     * @return true if the file exists, false otherwise.
     */
    public boolean fileExists(String fileName);
}
