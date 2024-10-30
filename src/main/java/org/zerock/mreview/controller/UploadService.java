package org.zerock.mreview.controller;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Log4j2
public class UploadService {

    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    private static final int THUMBNAIL_WIDTH = 100;
    private static final int THUMBNAIL_HEIGHT = 100;

    public File getFileObj(String srcFileName) throws UnsupportedEncodingException {
        log.info("srcFileName : " + srcFileName);

        File file = new File(uploadPath + File.separator + srcFileName);

        return file;
    }

    public File getThumbnailObj(File file, String prefix) throws UnsupportedEncodingException {

        File thumbnail = new File(file.getParent(), prefix + "_" + file.getName());

        return thumbnail;
    }

    public HttpHeaders getHeaders(File file) throws IOException {
        HttpHeaders headers = new HttpHeaders();

        //MIME 타입 처리
        headers.add("Content-Type", Files.probeContentType(file.toPath()));

        return headers;
    }

    public String extractFileName(String originalName) {
        return originalName.substring(originalName.lastIndexOf("\\") + 1);
    }

    public boolean isValidImageType(MultipartFile uploadFile) {
        if (!uploadFile.getContentType().startsWith("image")) {
            log.warn("this file is not image type");
            return false;
        }
        return true;
    }

    public String makeFolder() {
        String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderPath = format.replace("/", File.separator);

        File uploadPathFolder = new File(uploadPath, folderPath);
        createDirectoryIfNotExists(uploadPathFolder);

        return folderPath;
    }

    public void createDirectoryIfNotExists(File directory) {
        if (!directory.exists()) {
            directory.mkdirs();
            log.info("Directory created: " + directory.getAbsolutePath());
        }
    }

    //원본파일명생성
    public String createSaveName(String folderPath, String uuid, String fileName) {
        return Paths.get(uploadPath, folderPath, uuid + "_" + fileName).toString();
    }

    //섬네일파일명생성
    public String createSaveName(String folderPath, String uuid, String fileName, String prefix) {
        return Paths.get(uploadPath, folderPath, prefix + "_" + uuid + "_" + fileName).toString();
    }

    public void saveFile(MultipartFile uploadFile, Path savePath) throws IOException {
        uploadFile.transferTo(savePath);
    }

    //섬네일 생성
    public void createThumbnail(String folderPath,String uuid, String fileName, Path savePath) throws IOException {
        String thumbnailSaveName = createSaveName(folderPath, uuid, fileName, "s");

        File thumbnailFile = new File(thumbnailSaveName);

        Thumbnails.of(savePath.toFile())
                .size(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT)
                .toFile(thumbnailFile);
    }
}
