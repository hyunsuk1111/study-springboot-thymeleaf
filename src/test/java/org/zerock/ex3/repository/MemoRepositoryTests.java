package org.zerock.ex3.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.ex3.entity.Memo;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTests {

    @Autowired
    private MemoRepository memoRepository;

    @Test
    public void save() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Memo memo = Memo.builder()
                    .memoText("SameText " + i)
                    .build();

            memoRepository.save(memo);
        });
    }

    @Test
    public void selectFindById(){
        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno);

        if (result.isPresent()) {
            Memo memo = result.get();

            System.out.println("memo = " + memo);
        }

    }

    @Test
    public void update() {
        Memo memo = Memo.builder()
                .mno(100L)
                .memoText("Update Text")
                .build();

        memoRepository.save(memo);
    }

    @Test
    public void delete() {
        Long mno = 100L;

        memoRepository.deleteById(mno);
    }

    @Test
    public void pagination() {
        //JPA에서 페이징 처리 시 pageable 인터페이스를 만들어서 파라미터로 사용한다.
        //param(page, size, sort)
        Pageable pageable = PageRequest.of(0, 10);

        Page<Memo> result = memoRepository.findAll(pageable);

        //총 페이지
        System.out.println("result.getTotalPages() = " + result.getTotalPages());
        //전체 개수
        System.out.println("result.getTotalElements() = " + result.getTotalElements());
        //현재 페이지
        System.out.println("result = " + result.getNumber());
        //페이지당 데이터 개수
        System.out.println("result.getSize() = " + result.getSize());
        //다음 페이지 존재여부
        System.out.println("result.hasNext() = " + result.hasNext());
        //시작 페이지 여부
        System.out.println("result.isFirst() = " + result.isFirst());
    }

    @Test
    public void sortDesc() {
        Sort mnoDesc = Sort.by("mno").descending();

        Pageable pageable = PageRequest.of(0, 10, mnoDesc);

        Page<Memo> result = memoRepository.findAll(pageable);

        result.get().forEach(memo ->{
            System.out.println("memo = " + memo);
        });
    }

    @Test
    public void sortWithConditions() {
        Sort mnoDesc = Sort.by("mno").descending();
        Sort memoTextAsc = Sort.by("memoText").ascending();
        Sort andCondition = mnoDesc.and(memoTextAsc);

        Pageable pageable = PageRequest.of(0, 10, andCondition);
    }

    @Test
    public void findByMnoBetweenOrderByMnoDesc() {
        List<Memo> result = memoRepository.findByMnoBetweenOrderByMnoDesc(10L, 20L);

        for (Memo memo : result) {
            System.out.println("memo = " + memo);
        }
    }

    @Test
    public void findByMnoBetween() {
        Sort mnoDesc = Sort.by("mno").descending();

        Pageable pageable = PageRequest.of(0, 10, mnoDesc);

        Page<Memo> result = memoRepository.findByMnoBetween(10L, 50L, pageable);
        result.get().forEach(memo -> {
            System.out.println("memo = " + memo);
        });
    }
}
