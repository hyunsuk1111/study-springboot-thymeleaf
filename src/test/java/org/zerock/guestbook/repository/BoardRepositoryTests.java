package org.zerock.guestbook.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.guestbook.entity.Board;
import org.zerock.guestbook.entity.Member;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BoardRepositoryTests {
    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void insert() {
        //given
        IntStream.rangeClosed(1, 100).forEach(i ->{
            Member member = Member.builder().email("user" + i + "@aaa.com").build();

            Board board = Board.builder()
                    .title("Title..." + i)
                    .content("Content..." + i)
                    .writer(member)
                    .build();


            boardRepository.save(board);
        });

        //when
        Optional<Board> byId = boardRepository.findById(1L);

        //then
        byId.ifPresent(board -> {
            assertThat(board.getTitle()).isEqualTo("Title...1");
        });

    }

    @Test
    @Transactional
    public void select() {
        Optional<Board> byId = boardRepository.findById(100L);

        Board board = byId.get();

        System.out.println("board = " + board);
        System.out.println("board.getWriter() = " + board.getWriter());
    }

    @Test
    public void getBoardWithWriter() {
        Object result = boardRepository.getBoardWithWriter(100L);

        Object[] arr = (Object[]) result;

        System.out.println("Arrays.toString(arr) = " + Arrays.toString(arr));

    }

    @Test
    public void getBoardWithReply() {
        List<Object[]> result = boardRepository.getBoardWithReply(100L);

        for (Object[] objects : result) {
            System.out.println("Arrays.toString(objects) = " + Arrays.toString(objects));
        }
    }

    @Test
    public void getBoardWithReplyCount() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);

        result.forEach(i -> {
            Object[] row = (Object[]) i;

            System.out.println("Arrays.toString(row) = " + Arrays.toString(row));

        });
    }

    @Test
    public void getBoardByBno() {
        Object boardByBno = boardRepository.getBoardByBno(100L);

        Object[] result = (Object[]) boardByBno;

        System.out.println("Arrays.toString(result) = " + Arrays.toString(result));
    }
}
