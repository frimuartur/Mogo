package com.itproger.Controllers;


import com.itproger.Models.Article;
import com.itproger.repo.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/")
    public String home(Map<String, Object> model) {
        return "home";
    }

    @GetMapping("/reg")
    public String reg(Map<String, Object> model) {
        return "reg";
    }

    @GetMapping("/auth")
    public String auth(Map<String, Object> model) {
        return "auth";
    }

    @PostMapping("/article-add")
    public String articleAdd(@RequestParam String title,@RequestParam String text, Map<String, Object> model){
        Article article = new Article(title,text);
        articleRepository.save(article);

        return "redirect:/article";
    }

    @GetMapping("/article")
    public String article(Map<String, Object> model) {
        Iterable<Article> articles = articleRepository.findAll();
        model.put("article",articles);
        return "article";
    }

    @GetMapping("/article/{id}")
    public String articleInfo(@PathVariable(value = "id") long id, Map<String, Object> model ){
        Optional<Article> article = articleRepository.findById(id);

        ArrayList<Article> result = new ArrayList<>();
        article.ifPresent(result::add);

        model.put("articles", result);
        return "article-info";
    }

    @GetMapping("/article/{id}/update")
    public String articleUpdate(@PathVariable(value = "id") long id, Map<String, Object> model ){
        Optional<Article> article = articleRepository.findById(id);

        ArrayList<Article> result = new ArrayList<>();
        article.ifPresent(result::add);

        model.put("article", result);
        return "article-update";
    }

    @PostMapping("/article/{id}/update")
    public String articleUpdateForm(@PathVariable(value = "id") long id, @RequestParam String title,@RequestParam String text, Map<String, Object> model) throws ClassNotFoundException{
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ClassNotFoundException());
        article.setTitle(text);
        article.setText(title);
        articleRepository.save(article);

        return "redirect:/article/" + id;
    }

    @PostMapping("/article/{id}/delete")
    public String articleDelete(@PathVariable(value = "id") long id, Map<String, Object> model) throws ClassNotFoundException {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ClassNotFoundException());

        articleRepository.delete(article);
        return "redirect:/article";
    }
}
