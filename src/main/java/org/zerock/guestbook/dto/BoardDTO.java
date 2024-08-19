package org.zerock.guestbook.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDTO {
    private Long bno;

    private String title;

    private String content;

    private String writerEmail;

    private String writerName;

    private LocalDateTime regDate, modDate;

    private int replyCount;
}
