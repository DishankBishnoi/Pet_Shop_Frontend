package com.lms.petshopfrontend.controller;

import lombok.RequiredArgsConstructor;
import org.example.frontend.dto.VaccinationDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/vaccinations")
public class HarsimranController {

    private final WebClient webClient = WebClient.create("http://localhost:8080/api/v1");

    @GetMapping
    public String getVaccinations(Model model) {
        List<VaccinationDto> vaccinations = webClient.get()
                .uri("/vaccinations")
                .retrieve()
                .bodyToFlux(VaccinationDto.class)
                .collectList()
                .block();

        model.addAttribute("vaccinations", vaccinations);
        return "vaccinations"; // maps to vaccinations.html
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("vaccination", new VaccinationDto());
        return "add-vaccination";
    }
//http://localhost:8080/api/v1/vaccinations/add

    @PostMapping("/add")
    public String addVaccination(@ModelAttribute VaccinationDto vaccination) {
        webClient.post()
                .uri("/vaccinations/add")
                .body(Mono.just(vaccination), VaccinationDto.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        return "redirect:/vaccinations";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        VaccinationDto vaccination = webClient.get()
                .uri("/vaccinations/{id}", id)
                .retrieve()
                .bodyToMono(VaccinationDto.class)
                .block();

        model.addAttribute("vaccination", vaccination);
        return "edit-vaccination";
    }

    @PostMapping("/edit")
    public String editVaccination(@ModelAttribute VaccinationDto vaccination) {
        webClient.put()
                .uri("/vaccinations/update/{id}", vaccination.getId())
                .body(Mono.just(vaccination), VaccinationDto.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        return "redirect:/vaccinations";
    }
}
