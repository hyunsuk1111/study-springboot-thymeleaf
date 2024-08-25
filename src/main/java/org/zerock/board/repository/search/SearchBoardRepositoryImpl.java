package org.zerock.board.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.QBoard;
import org.zerock.board.entity.QMember;
import org.zerock.board.entity.QReply;

import java.util.List;

import static org.zerock.board.entity.QBoard.board;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {

    public SearchBoardRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public Board search() {
        log.info("search.........");

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member.email, reply.count());
        tuple.groupBy(board);

        List<Tuple> result = tuple.fetch();

        log.info(result);

        return null;
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member.email, reply.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(board.bno.gt(0L));

        if (type != null) {
            addTypeConditions(booleanBuilder, type, keyword);
        }

        tuple.where(booleanBuilder);
        tuple.groupBy(board);

        List<Tuple> result = tuple.fetch();

        log.info(result);
        return null;
    }


    private void addTypeConditions(BooleanBuilder booleanBuilder, String type, String keyword) {
        String[] typeArr = type.split("");
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        for (String t : typeArr) {
            conditionBuilder.or(getCondition(t, keyword));
        }

        if (conditionBuilder.hasValue()) {
            booleanBuilder.and(conditionBuilder);
        }
    }

    private  BooleanExpression getCondition(String type, String keyword){
        switch (type) {
            case "t":
                return QBoard.board.title.contains(keyword);
            case "w":
                return QMember.member.email.contains(keyword);
            case "c":
                return QBoard.board.content.contains(keyword);
            default:
                return null;
        }
    }
}
