package org.zerock.guestbook.service;

import com.querydsl.core.BooleanBuilder;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;
import org.zerock.guestbook.repository.GuestbookRepository;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class GuestbookServiceImpl implements GuestbookService {

    private final GuestbookRepository guestbookRepository;

    @Override
    @Transactional
    public Long register(GuestbookDTO guestbookDTO) {
        if (guestbookDTO.getGno() == null) {
            throw new IllegalArgumentException("dto null or empty");
        }

        Guestbook entity = dtoToEntity(guestbookDTO);

        try {
            guestbookRepository.save(entity);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to save entity with ID " + entity.getGno(), e);
        }

        return entity.getGno();
    }

    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());

        BooleanBuilder search = getSearch(requestDTO);

        Page<Guestbook> result = guestbookRepository.findAll(search, pageable);

        Function<Guestbook, GuestbookDTO> fn = (this::entityToDto);

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public BooleanBuilder getSearch(PageRequestDTO requestDTO) {
        String type = requestDTO.getType();
        String keyword = requestDTO.getKeyword();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QGuestbook qGuestbook = QGuestbook.guestbook;

        if (type == null) {
            booleanBuilder.and(qGuestbook.gno.gt(0L));
        } else{
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            if (type.contains("t")) {
                conditionBuilder.or(qGuestbook.title.contains(keyword));
            }
            if (type.contains("c")) {
                conditionBuilder.or(qGuestbook.content.contains(keyword));
            }
            if (type.contains("2")) {
                conditionBuilder.or(qGuestbook.writer.contains(keyword));
            }

            booleanBuilder.and(conditionBuilder);
        }

        return booleanBuilder;
    }

    @Override
    public GuestbookDTO read(Long gno) {
        Optional<Guestbook> result = guestbookRepository.findById(gno);

        return result.map(this::entityToDto).orElse(null);
    }

    @Override
    @Transactional
    public void remove(Long gno) {
        if (!guestbookRepository.existsById(gno)) {
            throw new EntityNotFoundException("Entity with ID " + gno + " not found");
        }

        try {
            guestbookRepository.deleteById(gno);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to save entity with ID " + gno, e);
        }
    }

    @Override
    @Transactional
    public void modify(GuestbookDTO dto) {
        if (dto.getGno() == null) {
            throw new IllegalArgumentException("DTO must not be null");
        }

        //수정
        Guestbook entity = updateDetails(dto);

        try {
            guestbookRepository.save(entity);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to update entity with ID " + dto.getGno(), e);
        }

    }

    private Guestbook updateDetails(GuestbookDTO dto) {
        Guestbook entity = guestbookRepository.findById(dto.getGno())
                .orElseThrow(() -> new EntityNotFoundException("Guestbook entity with ID " + dto.getGno() + " not found."));

        entity.changeTitle(dto.getTitle());
        entity.changeContent(dto.getContent());

        return entity;
    }
}
