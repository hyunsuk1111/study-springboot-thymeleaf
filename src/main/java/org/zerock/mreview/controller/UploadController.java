package org.zerock.mreview.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.mreview.dto.UploadResultDTO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
public class UploadController {

    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    private String uuid;

    @PostMapping("/uploadAjax")
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles) {

        List<UploadResultDTO> list = new ArrayList<>();

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
            list.add(new UploadResultDTO(fileName, uuid, folderPath));

            log.info("fileName : " + fileName);
        }//endFor

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName) {
        try {
            String srcFileName = URLDecoder.decode(fileName, "UTF-8");

            File file = getFileObj(srcFileName);

            log.info("file : " + file);

            HttpHeaders headers = getHeaders(file);

            byte[] fileContent = FileCopyUtils.copyToByteArray(file);

            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        } catch (FileNotFoundException e) {
            log.error("File not found: " + fileName, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            log.error("File I/O error: " + fileName, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    private File getFileObj(String srcFileName) throws UnsupportedEncodingException {
        log.info("srcFileName : " + srcFileName);

        File file = new File(uploadPath + File.separator + srcFileName);

        return file;
    }

    private static HttpHeaders getHeaders(File file) throws IOException {
        HttpHeaders headers = new HttpHeaders();

        //MIME 타입 처리
        headers.add("Content-Type", Files.probeContentType(file.toPath()));

        return headers;
    }

    private String extractFileName(String originalName) {
        return originalName.substring(originalName.lastIndexOf("\\") + 1);
    }

    private ResponseEntity isValidImageType(MultipartFile uploadFile) {
        if (!uploadFile.getContentType().startsWith("image")) {
            log.warn("this file is not image type");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.OK);
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
        uuid = UUID.randomUUID().toString();
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
