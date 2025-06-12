package com.lms.petshopfrontend.controller;


import com.lms.petshopfrontend.dto.PetDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class PetController {

    @Autowired
    private org.springframework.web.client.RestTemplate restTemplate;



    private final String BASE_API_URL = "http://localhost:9090/api/v1/pets";

    private final String CATEGORY_API_URL = "http://localhost:9090/api/v1/categories";

    // Redirect mistaken /api/v1/pets requests
    @GetMapping("/api/v1/pets")
    public String redirectApiToPets() {
        return "redirect:/pets";
    }

    // List pets with search and pagination
    @GetMapping("/pets")
    public String listPets(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            Model model) {
        try {
            ResponseEntity<PetDto[]> response = restTemplate.getForEntity(BASE_API_URL, PetDto[].class);
            List<PetDto> pets = Arrays.asList(response.getBody() != null ? response.getBody() : new PetDto[0]);

            if (search != null && !search.trim().isEmpty()) {
                String keyword = search.toLowerCase();
                pets = pets.stream()
                        .filter(p -> p.getName() != null && p.getName().toLowerCase().contains(keyword))
                        .collect(Collectors.toList());
            }

            int total = pets.size();
            int start = page * size;
            int end = Math.min(start + size, total);
            List<PetDto> pagedPets = (start < total) ? pets.subList(start, end) : new ArrayList<>();

            model.addAttribute("pets", pagedPets);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", (int) Math.ceil((double) total / size));
            model.addAttribute("search", search);
        } catch (RestClientException e) {
            model.addAttribute("error", "Failed to fetch pets: " + e.getMessage());
            return "error";
        }
        return "dishank/pet-list";
    }

    @GetMapping("/pets/add")
    public String showAddForm(Model model) {
        try {
            model.addAttribute("pet", new PetDto());
            ResponseEntity<PetDto.PetCategoryDto[]> response = restTemplate.getForEntity(
                    CATEGORY_API_URL, PetDto.PetCategoryDto[].class);
            List<PetDto.PetCategoryDto> categories = Arrays.asList(response.getBody() != null ? response.getBody() : new PetDto.PetCategoryDto[0]);
            System.out.println("Fetched categories: " + categories);
            model.addAttribute("categories", categories);
            if (categories.isEmpty()) {
                model.addAttribute("error", "No categories available. Please add categories in the backend.");
            }
        } catch (RestClientException e) {
            model.addAttribute("error", "Failed to fetch categories: " + e.getMessage());
            System.out.println("Category fetch error: " + e.getMessage());
        }
        return "pet-add";
    }

    @PostMapping("/pets/add")
    public String addPet(@ModelAttribute PetDto petDto, Model model) {
        // Ensure category object is initialized to avoid null pointer
        if (petDto.getCategory() == null) {
            petDto.setCategory(new PetDto.PetCategoryDto());
        }

        System.out.println("Submitting PetDto: " + petDto);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<PetDto> request = new HttpEntity<>(petDto, headers);

            // ✅ Point to correct backend endpoint
            String backendUrl = "http://localhost:9090/api/v1/pets/add";

            ResponseEntity<String> response = restTemplate.postForEntity(backendUrl, request, String.class);
            System.out.println("Backend response: " + response.getStatusCode() + " - " + response.getBody());

            return "redirect:/pets";
        } catch (RestClientException e) {
            String errorMessage = "Failed to add pet: " + e.getMessage();
            System.out.println("Backend error: " + e.getMessage());

            if (e instanceof HttpClientErrorException.BadRequest) {
                HttpClientErrorException ex = (HttpClientErrorException) e;
                errorMessage = "Validation errors: " + ex.getResponseBodyAsString();
                System.out.println("Detailed error: " + ex.getResponseBodyAsString());
            }

            model.addAttribute("error", errorMessage);

            // Fetch category list for re-rendered form
            try {
                ResponseEntity<PetDto.PetCategoryDto[]> response = restTemplate.getForEntity(
                        "http://localhost:9090/api/v1/categories", PetDto.PetCategoryDto[].class
                );
                List<PetDto.PetCategoryDto> categories = Arrays.asList(
                        response.getBody() != null ? response.getBody() : new PetDto.PetCategoryDto[0]
                );
                model.addAttribute("categories", categories);
            } catch (RestClientException ce) {
                model.addAttribute("error", model.getAttribute("error") + "; Failed to fetch categories: " + ce.getMessage());
            }

            model.addAttribute("pet", petDto);
            return "pet-add";
        }
    }



    // Show edit form
    @GetMapping("/pets/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        try {
            PetDto pet = restTemplate.getForObject(BASE_API_URL + "/" + id, PetDto.class);
            model.addAttribute("pet", pet);

            ResponseEntity<PetDto.PetCategoryDto[]> response = restTemplate.getForEntity(
                    CATEGORY_API_URL, PetDto.PetCategoryDto[].class);
            model.addAttribute("categories", Arrays.asList(response.getBody() != null ? response.getBody() : new PetDto.PetCategoryDto[0]));
        } catch (RestClientException e) {
            model.addAttribute("error", "Failed to fetch pet or categories: " + e.getMessage());
            return "error";
        }
        return "pet-edit";
    }

    // Handle POST to update pet
    @PostMapping("/pets/update")
    public String updatePet(@ModelAttribute PetDto petDto, Model model) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<PetDto> request = new HttpEntity<>(petDto, headers);

            restTemplate.exchange(
                    BASE_API_URL + "/update/" + petDto.getId(),
                    HttpMethod.PUT,
                    request,
                    PetDto.class
            );
            return "redirect:/pets";
        } catch (RestClientException e) {
            model.addAttribute("error", "Failed to update pet: " + e.getMessage());
            return "error";
        }
    }
}