package org.zerock.ex3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.ex3.entity.Memo;

import java.util.List;

//JpaRepository 인터페이스를 상속받는것만으로 기능을 사용 할 수있다.
//스프링이 빈으로 등록해준다.
//제네릭에 엔티티와 Id의 타입 명시
public interface MemoRepository extends JpaRepository<Memo, Long> {
    //10L ~ 20L 데이터 조회 후 List<entity> 타입으로 반환
    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);

    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);
}
