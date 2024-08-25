package org.zerock.board.repository.search;

import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.QBoard;

import java.util.List;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository{

    public SearchBoardRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public Board search() {
        log.info("search.........");

        QBoard board = QBoard.board;

        JPQLQuery<Board> jpqlQuery = from(board);

        List<Board> result = jpqlQuery.select(board).where(board.bno.eq(1L)).fetch();

        return null;
    }
}
