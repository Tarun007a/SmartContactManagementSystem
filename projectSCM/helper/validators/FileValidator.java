package com.scm.projectSCM.helper.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {
    private static final long MAX_FILE_SIZE = 1024 * 1024 * 2; // 2MB
    private static final long MAX_FILE_HEIGHT = 512;
    private static final long MAX_FILE_WIDTH = 512;
    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if(file == null || file.isEmpty()){
            return true;
        }
        if(file.getSize() > MAX_FILE_SIZE){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("file should be less than 2MB").addConstraintViolation();
            return false;
        }
        try {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if(bufferedImage.getHeight() > MAX_FILE_HEIGHT || bufferedImage.getWidth() > MAX_FILE_WIDTH){
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("max resolution 512 x 512").addConstraintViolation();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }
}
