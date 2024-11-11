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

    public String getImageURL() {
        return this.getEncodePath("/" + uuid + "_" + fileName);
    }

    public String getThumbnailURL() {
        return this.getEncodePath("/s_" + uuid + "_" + fileName);
    }

    private String getEncodePath(String path) {
        try {
            return URLEncoder.encode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  "";
    }
}
