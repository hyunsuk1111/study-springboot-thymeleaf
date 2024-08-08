package org.zerock.guestbook.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.guestbook.entity.Guestbook;

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

}