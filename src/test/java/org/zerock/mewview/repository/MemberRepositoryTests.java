package org.zerock.mewview.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mreview.entity.Member;
import org.zerock.mreview.repository.MemberRepository;
import org.zerock.mreview.repository.ReviewRepository;

import java.util.stream.IntStream;

@SpringBootTest
public class MemberRepositoryTests {

    @Autowired
    @Qualifier("mreviewMemberRepository")
    private MemberRepository memberRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void insertMembers() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Member member = Member.builder()
                    .email("r" + i + "@zerock.org")
                    .pw("1111")
                    .nickName("reviewer" + i)
                    .build();

            memberRepository.save(member);
        });//endIntStream
    }

    @Test
    @Transactional
    @Commit
    public void deleteMember() {
        Long mid = 1L;
        Member member = Member.builder()
                .mid(mid)
                .build();

        reviewRepository.deleteByMember(member);
        memberRepository.deleteById(mid);

    }
}
