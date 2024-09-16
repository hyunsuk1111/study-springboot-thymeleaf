package org.zerock.mreview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.mreview.entity.Member;

@Repository("mreviewMemberRepository")
public interface MemberRepository extends JpaRepository<Member, Long> {
}
