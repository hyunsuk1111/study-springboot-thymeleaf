package org.zerock.board.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;
import org.zerock.board.repository.ReplyRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void insert() {
        //given
        IntStream.rangeClosed(1, 300).forEach(i ->{
            long rno = (long) (Math.random() * 100) + 1;

            Board board = Board.builder().bno(rno).build();

            Reply reply = Reply.builder()
                    .text("Reply...." + i)
                    .board(board)
                    .replyer("guest")
                    .build();

            replyRepository.save(reply);

            //when
            Optional<Reply> byId = replyRepository.findById(1L);

            //then
            byId.ifPresent(value -> {
                assertThat(value.getText()).isEqualTo("Reply....1");
            });
        });
    }

    @Test
    public void select() {
        Optional<Reply> byId = replyRepository.findById(1L);

        Reply reply = byId.get();

        System.out.println("reply = " + reply);
        System.out.println("replyBoard = " + reply.getBoard());
    }

    @Test
    public void getRepliesByBoardOderByRno() {
        List<Reply> replyList = replyRepository.getRepliesByBoardOrderByRno(Board.builder()
                .bno(97L)
                .build());

        replyList.forEach(reply -> {
            System.out.println("reply = " + reply);
        });
    }
}
