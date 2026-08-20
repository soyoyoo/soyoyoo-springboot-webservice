package org.soyoyoo.web;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.soyoyoo.config.auth.LoginUser;
import org.soyoyoo.config.auth.dto.SessionUser;
import org.soyoyoo.service.posts.PostsService;
import org.soyoyoo.web.dto.PostsListResponseDto;
import org.soyoyoo.web.dto.PostsResponseDto;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(IndexController.class);
    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
//        model.addAttribute("posts", postsService.findAllDesc());
        List<PostsListResponseDto> posts = postsService.findAllDesc();
//        System.out.println("========== posts size: " + posts.size()); // 데이터가 들어오는지 콘솔에 출력!
        log.info("========== POSTS SIZE: {} ==========", posts.size());
        model.addAttribute("posts", posts);
//        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "index";
    }
    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        log.info("========== UPDATE ID: {} ==========", id);
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }
}
