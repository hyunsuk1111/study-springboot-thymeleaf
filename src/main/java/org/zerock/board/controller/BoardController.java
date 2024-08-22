package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.board.dto.BoardDTO;
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

    @PostMapping("/register")
    public String register(BoardDTO dto, RedirectAttributes redirectAttributes) {
        Long bno = boardService.register(dto);

        redirectAttributes.addFlashAttribute("msg", bno);

        return "redirect:/board/list";
    }

    @GetMapping("/read")
    public String read(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Long bno, Model model) {
        BoardDTO dto = boardService.get(bno);

        model.addAttribute("dto", dto);

        return "board/read";
    }
}
