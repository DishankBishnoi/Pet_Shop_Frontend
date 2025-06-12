package com.lms.petshopfrontend.controller;


import com.lms.petshopfrontend.dto.GroomingServiceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class IrshadController {

    @Autowired
    private RestTemplate restTemplate;

    private final String BASE_URL = "http://localhost:9090/api/v1/services";

//    @GetMapping("/home")
//    public String mainPage(){
//        return "tiles";
//    }

    // Show list of grooming services
    @GetMapping("/grooming-services")
    public String listServices(
            @RequestParam(value = "available", required = false) String available,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            Model model) {

        // Build URL based on available
        String url;
        if ("true".equalsIgnoreCase(available)) {
            url = BASE_URL + "/available";
        } else if ("false".equalsIgnoreCase(available)) {
            url = BASE_URL + "/unavailable";
        } else {
            url = BASE_URL;
        }

        // Fetch full list from backend
        ResponseEntity<GroomingServiceDto[]> response =
                restTemplate.getForEntity(url, GroomingServiceDto[].class);
        GroomingServiceDto[] allServices = response.getBody();

        // Optional: Filter by search manually
        List<GroomingServiceDto> filteredList = Arrays.stream(allServices)
                .filter(dto -> search == null || dto.getName().toLowerCase().contains(search.toLowerCase()))
                .toList();

        int totalItems = filteredList.size();
        int totalPages = (int) Math.ceil((double) totalItems / size);
        int fromIndex = Math.min(page * size, totalItems);
        int toIndex = Math.min(fromIndex + size, totalItems);

        List<GroomingServiceDto> paginatedList = filteredList.subList(fromIndex, toIndex);

        model.addAttribute("services", paginatedList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("filter", available);
        model.addAttribute("search", search);
        model.addAttribute("size", size);

        return "irshad/list";
    }


    // Show edit form with pre-filled values
    @GetMapping("/grooming-services/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        GroomingServiceDto service =
                restTemplate.getForObject(BASE_URL + "/" + id, GroomingServiceDto.class);
        model.addAttribute("service", service);
        return "irshad/edit-form"; // edit-form.html
    }

    // Handle form submission to update service
    @PostMapping("/grooming-services/update")
    public String updateService(@ModelAttribute GroomingServiceDto serviceDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GroomingServiceDto> request = new HttpEntity<>(serviceDto, headers);
        restTemplate.exchange(
                BASE_URL + "/" + serviceDto.getId(),
                HttpMethod.PUT,
                request,
                String.class
        );

        return "redirect:/grooming-services";
    }

    @GetMapping("/grooming-services/add")
    public String showAddForm(Model model) {
        model.addAttribute("service", new GroomingServiceDto());
        return "irshad/add-form";
    }

    // Handle insert form submission
    @PostMapping("/grooming-services/save")
    public String saveNewService(@ModelAttribute GroomingServiceDto serviceDto, Model model) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GroomingServiceDto> request = new HttpEntity<>(serviceDto, headers);

        String addUrl = BASE_URL + "/add"; // Important change here

        ResponseEntity<Map> response = restTemplate.postForEntity(addUrl, request, Map.class);

        if (response.getStatusCode() == HttpStatus.CREATED) {
            return "redirect:/grooming-services";
        } else {
            model.addAttribute("error", response.getBody().get("message"));
            model.addAttribute("service", serviceDto);
            return "irshad/add-form";
        }
    }

}

