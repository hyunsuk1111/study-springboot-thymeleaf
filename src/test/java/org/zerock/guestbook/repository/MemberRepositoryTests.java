package org.zerock.guestbook.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.guestbook.entity.Member;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void insert() {
        //given
        IntStream.rangeClosed(1, 100).forEach(i ->{
            Member member = Member.builder()
                    .email("user" + i + "@aaa.com")
                    .password("1111")
                    .name("USER" + i)
                    .build();

            memberRepository.save(member);
        });

        //when
        Optional<Member> user = memberRepository.findById("user1@aaa.com");

        //then
        user.ifPresent(member -> {
            assertThat(member.getName()).isEqualTo("USER1");
        });

    }
}
