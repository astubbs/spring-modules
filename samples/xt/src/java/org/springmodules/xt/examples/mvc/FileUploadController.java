package org.springmodules.xt.examples.mvc;

import java.io.File;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springmodules.xt.ajax.web.servlet.AjaxModelAndView;
import org.springmodules.xt.examples.mvc.form.FileUploadForm;

/**
 * Form controller for file uploading.
 *
 * @author Sergio Bossa
 */
public class FileUploadController extends SimpleFormController {
    
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        return new FileUploadForm();
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        FileUploadForm form = (FileUploadForm) command;
        String uploadDir = form.getUploadDir();
        MultipartFile file = form.getFile();
        
        file.transferTo(new File(uploadDir + File.separator + file.getOriginalFilename()));
        
        return new AjaxModelAndView(this.getSuccessView(), errors);
    }
}
