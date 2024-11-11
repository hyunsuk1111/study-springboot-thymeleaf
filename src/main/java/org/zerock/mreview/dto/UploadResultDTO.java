package org.zerock.mreview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@AllArgsConstructor
public class UploadResultDTO implements Serializable {

    private String fileName;
    private String uuid;
    private String folderPath;

    public String getImageURL() throws UnsupportedEncodingException {
        return encodeFilePath(folderPath + "/" + uuid + "_" + fileName);
    }

    public String getThumbnailURL() throws UnsupportedEncodingException {
        return encodeFilePath(folderPath + "/s_" + uuid + "_" + fileName);
    }

    private String encodeFilePath(String filePath) throws UnsupportedEncodingException {
        return URLEncoder.encode(filePath, "UTF-8");
    }
}
