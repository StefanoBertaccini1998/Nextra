package com.nextra.app.controller;

import com.nextra.app.model.Article;
import com.nextra.app.repository.ArticleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    // ✅ GET all articles
    @GetMapping
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    // ✅ GET article by ID
    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
        Optional<Article> article = articleRepository.findById(id);
        return article.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ POST new article
    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        Article saved = articleRepository.save(article);
        return ResponseEntity.ok(saved);
    }

    // ✅ PUT update article
    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody Article updated) {
        return articleRepository.findById(id)
            .map(existing -> {
                existing.setName(updated.getName());
                existing.setCode(updated.getCode());
                existing.setBasePrice(updated.getBasePrice());
                existing.setFrame(updated.getFrame());
                existing.setFeet(updated.getFeet());
                existing.setCustomFields(updated.getCustomFields());
                return ResponseEntity.ok(articleRepository.save(existing));
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ DELETE article
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        if (articleRepository.existsById(id)) {
            articleRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
