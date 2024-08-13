package org.zerock.guestbook.service;

import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;

public interface GuestbookService {
    //등록
    Long register(GuestbookDTO dto);

    //변환
    default Guestbook dtoToEntity(GuestbookDTO dto) {
        Guestbook entity = Guestbook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();

        return entity;
    }
    
    //변환
    default GuestbookDTO entityToDto(Guestbook entity) {
        GuestbookDTO dto = GuestbookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .build();

        return dto;
    }

    //10개 조회
    PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO);

    //상세 조회
    GuestbookDTO read(Long gno);

    //삭제
    void remove(Long gno);

    //수정
    void modify(GuestbookDTO dto);
}
