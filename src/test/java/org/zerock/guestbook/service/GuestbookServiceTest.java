package org.zerock.guestbook.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.repository.GuestbookRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class GuestbookServiceTest {

    @Autowired
    private GuestbookService guestbookService;

    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test
    public void register() {
        //given
        GuestbookDTO guestbookDTO = GuestbookDTO.builder()
                .title("Sample Title")
                .content("Sample Content")
                .writer("user0")
                .build();

        guestbookService.register(guestbookDTO);

        //when
        Optional<Guestbook> result = guestbookRepository.findById(301L);

        //then
        result.ifPresent(guestbook -> assertThat(guestbookDTO.getTitle()).isEqualTo(guestbook.getTitle()));
    }

    @Test
    public void getList() {
        PageRequestDTO requestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .type("tc")
                .keyword("한글")
                .build();

        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = guestbookService.getList(requestDTO);

        System.out.println("PREV" + resultDTO.isPrev());
        System.out.println("NEXT" +resultDTO.isNext());
        System.out.println("resultDTO.getTotalPage() = " + resultDTO.getTotalPage());

        for (GuestbookDTO guestbookDTO : resultDTO.getDtoList()) {
            System.out.println("guestbookDTO = " + guestbookDTO);
        }

        resultDTO.getPageList().forEach(i ->{
            System.out.println("i = " + i);
        });

    }

    @Test
    public void modify() {
        //given
        GuestbookDTO dto = GuestbookDTO.builder()
                .gno(1L)
                .title("changed title")
                .content("changed content")
                .build();

        guestbookService.modify(dto);

        //when
        Optional<Guestbook> byId = guestbookRepository.findById(1L);

        //then
        byId.ifPresent(guestbook -> {
            assertThat(guestbook.getTitle()).isEqualTo("changed title");
        });
    }

}