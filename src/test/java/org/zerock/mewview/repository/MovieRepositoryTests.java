package org.zerock.mewview.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.MovieImage;
import org.zerock.mreview.repository.MovieImageRepository;
import org.zerock.mreview.repository.MovieRepository;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
public class MovieRepositoryTests {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieImageRepository movieImageRepository;

    @Test
    @Commit
    @Transactional
    public void insertMovie() {
        IntStream.rangeClosed(1, 100).forEach(i ->{
            Movie movie = Movie.builder()
                    .title("Movie..." + i)
                    .build();

            movieRepository.save(movie);
            
            //이미지 네이밍
            int count = (int) ((Math.random() * 5) + 1);

            for (int j = 0; j < count; j++) {
                MovieImage movieImage = MovieImage.builder()
                        .uuid(UUID.randomUUID().toString())
                        .movie(movie)
                        .imgName("test" + j + ".jpg")
                        .build();

                movieImageRepository.save(movieImage);
            }//endFor
        });//endIntStream
    }

    @Test
    public void getListPage() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "mno"));

        Page<Object[]> result = movieRepository.getListPage(pageRequest);
        
        result.forEach(i ->{
            System.out.println("Arrays.toString(i) = " + Arrays.toString(i));
        });

    }
}
