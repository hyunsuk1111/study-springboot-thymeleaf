package org.zerock.mreview.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    @PostMapping("/uploadAjax")
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles) throws IOException {

        List<UploadResultDTO> list = new ArrayList<>();

        for (MultipartFile uploadFile : uploadFiles) {
            if(!uploadService.isValidImageType(uploadFile)){
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            String originalName = uploadFile.getOriginalFilename();
            //파일명 추출
            String fileName = uploadService.extractFileName(originalName);

            //고유값 생성
            String uuid = UUID.randomUUID().toString();
            //폴더생성
            String folderPath = uploadService.makeFolder();
            //UUID 생성 후 파일명 구성
            String saveName = uploadService.createSaveName(folderPath, uuid, fileName);
            //저장할 파일 경로
            Path savePath = Paths.get(saveName);

            // 파일 저장
            uploadService.saveFile(uploadFile, savePath);
            //섬네일생성
            uploadService.createThumbnail(folderPath,uuid, fileName, savePath);

            list.add(new UploadResultDTO(fileName, uuid, folderPath));

            log.info("fileName : " + fileName);
        }//endFor

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName) {
        try {
            String srcFileName = URLDecoder.decode(fileName, "UTF-8");

            File file = uploadService.getFileObj(srcFileName);

            log.info("file : " + file);

            HttpHeaders headers = uploadService.getHeaders(file);

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
}
