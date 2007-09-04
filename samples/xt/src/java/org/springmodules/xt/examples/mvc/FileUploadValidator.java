package org.springmodules.xt.examples.mvc;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springmodules.xt.examples.domain.codes.OtherErrorCodes;
import org.springmodules.xt.examples.mvc.form.FileUploadForm;

/**
 * Validate the upload file form.
 *
 * @author Sergio Bossa
 */
public class FileUploadValidator implements Validator {
     
    public boolean supports(Class aClass) {
        return FileUploadForm.class.equals(aClass);
    }

    public void validate(Object object, Errors errors) {
        if (this.supports(object.getClass())) {
            FileUploadForm form = (FileUploadForm) object;
            
            if (form.getUploadDir() == null || form.getUploadDir().trim().equals("")) {
                errors.rejectValue("uploadDir", OtherErrorCodes.NO_DIR_CODE, "No directory specified!");
            }
            
            if (form.getFile() == null || form.getFile().isEmpty()) {
                errors.rejectValue("file", OtherErrorCodes.NO_FILE_CODE, "No file selected!");
            }
        }
    }
}
