package com.sage.services.helper.file;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.sage.model.file.FileType;
import com.sage.port.services.helper.file.FileHelperService;

@Service
public class FileHelperServiceImpl implements FileHelperService {

    private static final Logger logger = Logger.getLogger(FileHelperServiceImpl.class.getName());

    @Override
    public String saveBase64File(String base64, FileType fileType, String fileName) {
        boolean valid_extension = false;
        if (fileType == FileType.CARREGIVER_IMAGE || fileType == FileType.RESIDENT_IMAGE) {
            valid_extension = true;
        }

        if (!valid_extension) {
            throw new IllegalArgumentException("Invalid file type: " + fileType);
        }

        String format = base64.split(";")[0].split(":")[1];

        if (!format.equals("image/jpg") && !format.equals("image/png")) {
            throw new IllegalArgumentException("Unsupported file type: " + format);
        }

        String applicationPath = System.getProperty("user.dir");
        String filePath = applicationPath + "/output-files/" + fileType.getType() + "/";
        filePath += fileName + ".base64";

        try {
            java.nio.file.Files.write(java.nio.file.Paths.get(filePath), base64.getBytes());
        } catch (java.io.IOException e) {
            logger.log(Level.SEVERE, "Failed to save file: {0}", e.getMessage());
            throw new RuntimeException("Failed to save file: " + fileName, e);
        }

        return filePath;
    }

    @Override
    public byte[] readFile(String fileName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readFile'");
    }

    @Override
    public boolean deleteFile(String fileName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteFile'");
    }

    @Override
    public boolean fileExists(String fileName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fileExists'");
    }

}
