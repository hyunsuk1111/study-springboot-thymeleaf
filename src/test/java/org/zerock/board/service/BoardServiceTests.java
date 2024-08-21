package org.zerock.board.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.dto.BoardDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;

@SpringBootTest
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void register() {
        BoardDTO dto = BoardDTO.builder()
                .title("Test.")
                .content("content..")
                .writerEmail("user55@aaa.com")
                .build();

        Long bno = boardService.register(dto);

    }

    @Test
    public void getList() {
        PageRequestDTO requestDTO = new PageRequestDTO();

        PageResultDTO<BoardDTO, Object[]> result = boardService.getList(requestDTO);

        for (BoardDTO boardDTO : result.getDtoList()) {
            System.out.println("boardDTO = " + boardDTO);
        }
    }

    @Test
    public void get() {
        BoardDTO boardDTO = boardService.get(100L);

        System.out.println("boardDTO = " + boardDTO);
    }

    @Test
    public void removeWithReplies() {
        boardService.removeWithReplies(1L);
    }

    @Test
    public void modify() {
        BoardDTO dto = BoardDTO.builder()
                .bno(2L)
                .title("제목 변경")
                .content("내용 변경")
                .build();

        boardService.modify(dto);

    }
}
