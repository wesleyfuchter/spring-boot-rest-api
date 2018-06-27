package com.wesleyfuchter.imageuploader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;

@Controller
public class ImageController {

    private static final String BASE_PATH = "/images";
    private static final String FILENAME = "{filename:.+}";

    private final ImageService service;

    @Autowired
    public ImageController(final ImageService service) {
        this.service = service;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = BASE_PATH + "/" + FILENAME + "/raw")
    public ResponseEntity oneRowImage(@PathVariable final String filename) {
        try {
            final Resource file = service.findOneImage(filename);
            return ResponseEntity.ok().
                    contentLength(file.contentLength()).
                    contentType(MediaType.IMAGE_JPEG).
                    body(new InputStreamResource(file.getInputStream()));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Couldn't find " + filename + " => " + e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = BASE_PATH)
    public ResponseEntity createFile(@RequestParam("file") final MultipartFile file, HttpServletRequest request) {
        try {
            service.createImage(file);
            final URI locationUri = new URI(request.getRequestURL().toString() + "/").
                    resolve(file.getOriginalFilename() + "/raw");
            return ResponseEntity.created(locationUri).
                    body("Successfully upload " + file.getOriginalFilename());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body("Failed to upload " + file.getOriginalFilename() + " => " + e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = BASE_PATH + "/" + FILENAME)
    public ResponseEntity deleteFile(@PathVariable final String filename) {
        try {
            service.deleteImage(filename);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).
                    body("Successfully delete " + filename);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body("Failed to delete " + filename + " => " + e.getMessage());
        }
    }

}
