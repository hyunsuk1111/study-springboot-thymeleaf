package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.board.service.BoardService;
import org.zerock.guestbook.dto.PageRequestDTO;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
@Log4j2
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public String list(PageRequestDTO requestDTO, Model model) {
        model.addAttribute("result", boardService.getList(requestDTO));

        return "board/list";
    }

    @GetMapping("/register")
    public String register() {
        return "board/register";
    }
}
