package org.zerock.mreview.service;

import org.springframework.data.domain.Pageable;
import org.zerock.mreview.dto.MovieDTO;
import org.zerock.mreview.dto.MovieImageDTO;
import org.zerock.mreview.dto.PageResultDTO;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.MovieImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public interface MovieService {
    Long register(MovieDTO movieDTO);

    PageResultDTO<MovieDTO, Object[]> getListPage(Pageable pageable);

    default Map<String, Object> dtoToEntity(MovieDTO movieDTO) {
        Map<String, Object> entityMap = new HashMap<>();

        Movie movie = this.getMovieEntity(movieDTO);

        entityMap.put("movie", movie);
        entityMap.put("imgList", this.getMovieImageEntity(movieDTO, movie));

        return entityMap;
    }

    private Movie getMovieEntity(MovieDTO movieDTO) {
        return Movie.builder()
                .mno(movieDTO.getMno())
                .title(movieDTO.getTitle())
                .build();
    }

    private List<MovieImage> getMovieImageEntity(MovieDTO movieDTO, Movie movie) {
        List<MovieImageDTO> imageDTOList = movieDTO.getImageDTOList();
        List<MovieImage> movieImageList = null;

        if (imageDTOList != null && !imageDTOList.isEmpty()) {
            movieImageList = imageDTOList.stream().map(movieImageDTO -> {
                MovieImage movieImage = MovieImage.builder()
                        .path(movieImageDTO.getPath())
                        .imgName(movieImageDTO.getImgName())
                        .uuid(movieImageDTO.getUuid())
                        .movie(movie)
                        .build();
                return movieImage;
            }).collect(Collectors.toList());
        }//if
        return movieImageList;
    }

    default MovieDTO entitiesToDTO(Movie movie, List<MovieImage> movieImages,  Double avg, Long reviewCnt) {
        return MovieDTO.builder()
                .mno(movie.getMno())
                .title(movie.getTitle())
                .regDate(movie.getRegDate())
                .modDate(movie.getModDate())
                .imageDTOList(this.getMovieImageDTO(movieImages))
                .avg(avg)
                .reviewCnt(reviewCnt.intValue())
                .build();
    }

    private List<MovieImageDTO> getMovieImageDTO(List<MovieImage> movieImages) {
        return  movieImages.stream()
                .map(movieImage -> MovieImageDTO.builder()
                        .imgName(movieImage.getImgName())
                        .path(movieImage.getPath())
                        .uuid(movieImage.getUuid())
                        .build())
                .collect(Collectors.toList());
    }
}
