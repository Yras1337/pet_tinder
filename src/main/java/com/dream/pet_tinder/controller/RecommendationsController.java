package com.dream.pet_tinder.controller;

import com.dream.pet_tinder.service.RecommendationsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recommendations")
@AllArgsConstructor
public class RecommendationsController {

    private final RecommendationsService recommendationsService;

    @GetMapping("/{id}")
    public String nextRecommendation(@PathVariable Long id, final Model model) {
        model.addAttribute("profile", recommendationsService.getNextRecommendation(id));

        return "recommendations/next_recommendation";
    }
}
