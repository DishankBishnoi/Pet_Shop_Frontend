package com.lms.petshopfrontend.controller;

import com.example.link.dto.PetFoodDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Controller
public class PetFoodController {

    @Autowired
    private RestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:9090/api/v1/pet_foods";

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/petfoods")
    public String showPetFoods(Model model) {
        PetFoodDto[] petFoodArray = restTemplate.getForObject(BASE_URL, PetFoodDto[].class);
        model.addAttribute("petFoods", Arrays.asList(petFoodArray));
        return "petfood";
    }


    @GetMapping("/petfoods/edit/{id}")
    public String editPetFood(@PathVariable Integer id, Model model) {
        PetFoodDto petFood = restTemplate.getForObject(BASE_URL + "/" + id, PetFoodDto.class);
        model.addAttribute("petFood", petFood);
        return "edit-petfood";
    }

    @PostMapping("/petfoods/update/{id}")
    public String updatePetFood(@PathVariable Integer id, @ModelAttribute PetFoodDto petFoodDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PetFoodDto> request = new HttpEntity<>(petFoodDto, headers);
        restTemplate.exchange(BASE_URL + "/update/" + id, HttpMethod.PUT, request, Void.class);
        return "redirect:/petfoods";
    }

    @GetMapping("/petfoods/create")
    public String createPetFoodForm(Model model) {
        model.addAttribute("petFood", new PetFoodDto());
        return "create-petfood";
    }

    @PostMapping("/petfoods/create")
    public String createPetFood(@ModelAttribute PetFoodDto petFoodDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PetFoodDto> request = new HttpEntity<>(petFoodDto, headers);

        restTemplate.postForObject("http://localhost:9090/api/v1/pet_foods/add", request, Void.class);
        return "redirect:/petfoods";
    }

}
