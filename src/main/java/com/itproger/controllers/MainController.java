package com.itproger.controllers;

import com.itproger.models.Article;
import com.itproger.models.Role;
import com.itproger.models.User;
import com.itproger.repo.ArticleRepository;
import com.itproger.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home");
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/article")
    public String article(Model model) {
        Iterable<Article> articles = articleRepository.findAll();
        model.addAttribute("article",articles);
        return "article";
    }


    @PostMapping("/article-add")
    public String articleAdd(@RequestParam String title,@RequestParam String text, Model model){
        Article article = new Article(title,text);
        articleRepository.save(article);

        return "redirect:/article";
    }



    @GetMapping("/article/{id}")
    public String articleInfo(@PathVariable(value = "id") long id, Model model){
        Optional<Article> article = articleRepository.findById(id);

        ArrayList<Article> result = new ArrayList<>();
        article.ifPresent(result::add);

        model.addAttribute("articles", result);
        return "article-info";
    }

    @GetMapping("/article/{id}/update")
    public String articleUpdate(@PathVariable(value = "id") long id, Model model){
        Optional<Article> article = articleRepository.findById(id);

        ArrayList<Article> result = new ArrayList<>();
        article.ifPresent(result::add);

        model.addAttribute("article", result);
        return "article-update";
    }

    @PostMapping("/article/{id}/update")
    public String articleUpdateForm(@PathVariable(value = "id") long id, @RequestParam String title,@RequestParam String text, Model model) throws ClassNotFoundException{
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ClassNotFoundException());
        article.setTitle(text);
        article.setText(title);
        articleRepository.save(article);

        return "redirect:/article/" + id;
    }

    @PostMapping("/article/{id}/delete")
    public String articleDelete(@PathVariable(value = "id") long id, Model model) throws ClassNotFoundException {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ClassNotFoundException());

        articleRepository.delete(article);
        return "redirect:/article";
    }

    @GetMapping("/reg")
    public String reg(Model model){
        return "reg";
    }

    @PostMapping("/reg")
    public String addUser(User user, Model model){
        user.setEnabled(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);
        return "redirect:/login";
    }
}
