package org.zerock.mreview.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mreview.dto.MovieDTO;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.MovieImage;
import org.zerock.mreview.repository.MovieImageRepository;
import org.zerock.mreview.repository.MovieRepository;

import java.util.List;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final MovieImageRepository imageRepository;

    @Override
    @Transactional
    public Long register(MovieDTO movieDTO) {
        Map<String, Object> entityMap = dtoToEntity(movieDTO);

        Movie movie = (Movie) entityMap.get("movie");
        List<MovieImage> movieImageList = (List<MovieImage>) entityMap.get("imgList");

        if (movieImageList == null || movieImageList.isEmpty()) {
            throw new IllegalArgumentException("영화 이미지 리스트가 비어있습니다.");
        }

        try {
            movieRepository.save(movie);
            movieImageList.forEach(imageRepository::save);

            return movie.getMno();
        } catch (Exception e) {
            log.error("영화 등록 중 오류 발생: ", e);
            throw new RuntimeException("영화 등록 실패", e);  // 예외를 던져 롤백
        }
    }
}
