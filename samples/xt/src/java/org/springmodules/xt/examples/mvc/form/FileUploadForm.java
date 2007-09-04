package org.springmodules.xt.examples.mvc.form;

import org.springframework.web.multipart.MultipartFile;

/**
 * Form bean for file uploading.
 *
 * @author Sergio Bossa
 */
public class FileUploadForm {
    
    private String uploadDir;
    private MultipartFile file;

    public MultipartFile getFile() {
        return this.file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getUploadDir() {
        return this.uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
