package org.zerock.board.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyDTO {

    private Long rno;
    private String text;
    private String replyer;
    private Long bno;
    private LocalDateTime regDate, modDate;
}
