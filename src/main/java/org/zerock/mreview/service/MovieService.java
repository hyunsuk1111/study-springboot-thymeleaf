package org.zerock.mreview.service;

import org.zerock.mreview.dto.MovieDTO;
import org.zerock.mreview.dto.MovieImageDTO;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.MovieImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MovieService {
    Long register(MovieDTO movieDTO);

    //Map 타입으로 변환
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

}
