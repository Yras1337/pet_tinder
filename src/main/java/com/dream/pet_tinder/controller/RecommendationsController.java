package com.dream.pet_tinder.controller;

import com.dream.pet_tinder.service.RecommendationsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recommendations")
@AllArgsConstructor
public class RecommendationsController {

    private final RecommendationsService recommendationsService;

    @GetMapping("/{id}")
    public String nextRecommendation(@PathVariable Long id, final Model model) {
        model.addAttribute("profile", recommendationsService.getNextRecommendation(id));
        model.addAttribute("our_profile", id);

        return "recommendations/next_recommendation";
    }

    @PostMapping("/{id}/match/{pId}")
    public String processMatch(@PathVariable Long id, @PathVariable Long pId, final String flag) {
        recommendationsService.processMatch(id, pId, flag);

        return "redirect:/recommendations/" + id.toString();
    }

    @GetMapping("/{id}/income_likes")
    public String potentialMatches(@PathVariable Long id, final Model model) {
        model.addAttribute("profile", recommendationsService.getPotentialRecommendation(id));
        model.addAttribute("our_profile", id);

        return "recommendations/potential_recommendation";
    }

    @PostMapping("/{id}/income_likes/match/{pId}")
    public String processIncomeMatch(@PathVariable Long id, @PathVariable Long pId, final String flag) {
        recommendationsService.processMatch(id, pId, flag);

        return "redirect:/recommendations/" + id.toString() + "/income_likes";
    }
}
