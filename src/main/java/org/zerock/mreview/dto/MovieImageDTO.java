package org.zerock.mreview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieImageDTO {
    private String imgName;
    private String uuid;
    private String path;

    public String getImageURL() {
        return this.getEncodePath(path + "/" + uuid + "_" + imgName);
    }

    public String getThumbnailURL() {
        return this.getEncodePath(path + "/s_" + uuid + "_" + imgName);
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
