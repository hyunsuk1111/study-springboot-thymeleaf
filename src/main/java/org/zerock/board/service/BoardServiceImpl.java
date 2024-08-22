package org.zerock.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.board.dto.BoardDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.repository.BoardRepository;

import java.util.Optional;
import java.util.function.Function;

@Controller
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;

    @Override
    public Long register(BoardDTO dto) {

        Board entity = dtoToEntity(dto);

        boardRepository.save(entity);

        return entity.getBno();
    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
        Function<Object[], BoardDTO> fn = (en -> entityToDTO( (Board) en[0], (Member)en[1], (Long)en[2] ));

        Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageRequestDTO.getPageable(Sort.by("bno").descending()));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public BoardDTO get(Long bno) {
        Object boardByBno = boardRepository.getBoardByBno(bno);

        Object[] result = (Object[]) boardByBno;

        return entityToDTO((Board) result[0], (Member) result[1], (Long) result[2]);
    }

    @Override
    @Transactional
    public void removeWithReplies(Long bno) {
        boardRepository.deleteReplyByBno(bno);

        boardRepository.deleteById(bno);
    }

    @Override
    @Transactional
    public void modify(BoardDTO boardDTO) {
        Optional<Board> byId = boardRepository.findById(boardDTO.getBno());

        byId.ifPresent(board -> {
            updateBoardDetails(board, boardDTO);

            boardRepository.save(board);
        });
    }

    private void updateBoardDetails(Board board, BoardDTO boardDTO) {
        board.changeTitle(boardDTO.getTitle());
        board.changeContent(boardDTO.getContent());
    }
}
