package org.zerock.mreview.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@Log4j2
public class UploadController {

    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    @PostMapping("/uploadAjax")
    public void uploadFile(MultipartFile[] uploadFiles) {
        for (MultipartFile uploadFile : uploadFiles) {
            isValidImageType(uploadFile);

            String originalName = uploadFile.getOriginalFilename();
            //파일명 추출
            String fileName = extractFileName(originalName);

            //폴더생성
            String folderPath = makeFolder();
            //UUID 생성 후 파일명 구성
            String saveName = createSaveName(folderPath, fileName);
            //저장할 파일 경로
            Path savePath = Paths.get(saveName);

            // 파일 저장
            saveFile(uploadFile, savePath);

            log.info("fileName : " + fileName);
        }//endFor
    }
    private String extractFileName(String originalName) {
        return originalName.substring(originalName.lastIndexOf("\\") + 1);
    }

    private boolean isValidImageType(MultipartFile uploadFile) {
        if (!uploadFile.getContentType().startsWith("image")) {
            log.warn("this file is not image type");
            return false;
        }
        return true;
    }

    private String makeFolder() {
        String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderPath = format.replace("/", File.separator);

        File uploadPathFolder = new File(uploadPath, folderPath);
        createDirectoryIfNotExists(uploadPathFolder);

        return folderPath;
    }

    private void createDirectoryIfNotExists(File directory) {
        if (!directory.exists()) {
            directory.mkdirs();
            log.info("Directory created: " + directory.getAbsolutePath());
        }
    }

    private String createSaveName(String folderPath, String fileName) {
        String uuid = UUID.randomUUID().toString();
        return Paths.get(uploadPath, folderPath, uuid + "_" + fileName).toString();
    }

    private void saveFile(MultipartFile uploadFile, Path savePath) {
        try {
            uploadFile.transferTo(savePath);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
