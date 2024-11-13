package org.zerock.mreview.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<DTO, EN> {

    //dto 리스트
    private List<DTO> dtoList;

    //총 페이지 번호
    private int totalPage;

    //현재 페이지 번호
    private int page;
    //목록 사이즈
    private int size;

    //시작, 끝 페이지 번호
    private int start, end;

    //이전, 다음
    private boolean prev, next;

    //하단에 뿌려줄 페이지 목록
    private List<Integer> pageList;

    //페이지 결과 처리
    public PageResultDTO(Page<EN> result, Function<EN,DTO> fn) {
        this.dtoList = result.stream().map(fn).collect(Collectors.toList());

        this.totalPage = result.getTotalPages();
        
        //페이지 정보 출력
        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable) {
        //현재 페이지
        this.page = pageable.getPageNumber() + 1;
        //게시글의 수
        this.size = pageable.getPageSize();

        //마지막 페이지 
        int tempEnd = (int) (Math.ceil((double) this.page / 10)) * 10;

        // 1 ~ 10, 11 ~ 21 페이지씩 보여줄 예정
        this.start = tempEnd -  9;
        this.end = Math.min(this.totalPage, tempEnd);

        //이전, 다음
        this.prev = this.start > 1;
        this.next = this.totalPage > tempEnd;

        //하단 페이지 정보
        this.pageList = IntStream.rangeClosed(this.start, this.end).boxed().collect(Collectors.toList());


    }
}
