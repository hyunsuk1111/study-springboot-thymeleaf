package org.zerock.mreview.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.mreview.dto.MovieDTO;
import org.zerock.mreview.service.MovieService;

@Controller
@Log4j2
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/register")
    public String register() {
        return "movie/register";
    }

    @PostMapping("/register")
    public String register(MovieDTO movieDTO, RedirectAttributes redirectAttributes) {
        log.info("movieDTO : " + movieDTO);
        String baseUrl  = "redirect:/movie";

        try {
            Long mno = movieService.register(movieDTO);
            redirectAttributes.addFlashAttribute("msg", mno);
            return baseUrl  + "/list";
        } catch (IllegalArgumentException e) {
            log.error("Invalid input: ", e);
            redirectAttributes.addFlashAttribute("errorMsg", "영화 등록 실패: " + e.getMessage());
            return baseUrl  + "/register"; // 등록 페이지로 리다이렉트
        }
    }
}
