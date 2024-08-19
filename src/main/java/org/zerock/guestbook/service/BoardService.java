package org.zerock.guestbook.service;

import org.springframework.data.domain.PageRequest;
import org.zerock.guestbook.dto.BoardDTO;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Board;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.Member;
import org.zerock.guestbook.entity.Reply;

public interface BoardService {
    //등록
    Long register(BoardDTO dto);

    //변환
    default Board dtoToEntity(BoardDTO dto) {
        Member member = Member.builder()
                .email(dto.getWriterEmail())
                .build();

        Board board = Board.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(member)
                .build();

        return board;
    }

    //변환
    default BoardDTO entityToDTO(Board board, Member member, Long replyCount) {
        BoardDTO dto = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .replyCount(replyCount.intValue())
                .build();

        return dto;
    }

    //목록 처리
    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);
}
