package org.zerock.guestbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;

import java.util.Optional;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class GuestbookRepositoryTest {

    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test
    public void save() {
        //given
        LongStream.rangeClosed(1, 300).forEach(i ->{
            Guestbook guestbook = Guestbook.builder()
                    .gno(i)
                    .title("Title..." + i)
                    .content("Content..." + i)
                    .writer("user..." + (i % 10))
                    .build();

            guestbookRepository.save(guestbook);
        });

        //when
        Optional<Guestbook> result = guestbookRepository.findById(300L);

        //then
        if (result.isPresent()) {
            Guestbook guestbook = result.get();

            assertThat(guestbook.getGno()).isEqualTo(300L);
            assertThat(guestbook.getContent()).isEqualTo("Content...300");
        }

    }

    @Test
    public void update() {
        //given
        Optional<Guestbook> result = guestbookRepository.findById(300L);

        //when
        if (result.isPresent()) {
            Guestbook guestbook = result.get();

            guestbook.changeTitle("Changed Title");
            guestbook.changeContent("Changed Content");

            guestbookRepository.save(guestbook);
        }

        Optional<Guestbook> updatedResult = guestbookRepository.findById(300L);

        //then
        assertThat(updatedResult.get().getTitle()).isEqualTo("Changed Title");
    }

    @Test
    public void findByTitle() {
        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        BooleanExpression expression = qGuestbook.title.contains(keyword);
        booleanBuilder.and(expression);

        Page<Guestbook> result = guestbookRepository.findAll(booleanBuilder, pageable);

        result.forEach(guestbook -> {
            System.out.println("guestbook = " + guestbook);
        });
    }

    @Test
    public void findByMultiConditions() {
        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        BooleanExpression exTitle = qGuestbook.title.contains(keyword);
        BooleanExpression exContent = qGuestbook.content.contains(keyword);
        BooleanExpression exAll = exTitle.or(exContent);

        booleanBuilder.and(exAll);
        booleanBuilder.and(qGuestbook.gno.gt(0L));

        Page<Guestbook> result = guestbookRepository.findAll(booleanBuilder, pageable);

        result.forEach(guestbook -> {
            System.out.println("guestbook = " + guestbook);
        });

    }

}