package org.zerock.guestbook.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.guestbook.dto.BoardDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.repository.BoardRepository;

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
}
