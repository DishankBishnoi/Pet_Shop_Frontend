package com.lms.petshopfrontend.controller;

import org.example.frontend.dto.SupplierDto;
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
public class HarmanController {

    @Autowired
    private RestTemplate restTemplate;

    private final String BASE_URL = "http://localhost:9090/api/v1/suppliers";

    //  List all suppliers (no backend pagination)
    @GetMapping("/suppliers")
    public String getAllSuppliers(Model model) {
        SupplierDto[] response = restTemplate.getForObject(BASE_URL, SupplierDto[].class);
        List<SupplierDto> suppliers = Arrays.asList(response != null ? response : new SupplierDto[0]);
        model.addAttribute("suppliers", suppliers);
        return "harman/supplier-list";
    }

    //  Search by name (returns matching suppliers)
    @GetMapping("/suppliers/search")
    public String searchSuppliersByName(@RequestParam("name") String name, Model model) {
        ResponseEntity<SupplierDto[]> response = restTemplate.getForEntity(
                BASE_URL + "/name/" + name, SupplierDto[].class);
        SupplierDto[] suppliers = response.getBody();
        model.addAttribute("suppliers", suppliers);
        return "harman/supplier-list";
    }

    //  Show form to create supplier
    @GetMapping("/suppliers/create")
    public String showCreateForm(Model model) {
        model.addAttribute("supplier", new SupplierDto());
        return "harman/supplier-create";
    }

    //  Create supplier
    @PostMapping("/suppliers/create")
    public String createSupplier(@ModelAttribute("supplier") SupplierDto supplierDto, Model model) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<SupplierDto> request = new HttpEntity<>(supplierDto, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(BASE_URL + "/add", request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return "redirect:/suppliers";
            } else {
                model.addAttribute("errorMessage", "Something went wrong while creating supplier.");
                model.addAttribute("supplier", supplierDto);
                return "harman/supplier-create";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Exception: " + e.getMessage());
            model.addAttribute("supplier", supplierDto);
            return "harman/supplier-create";
        }
    }

    //  Show edit form
    @GetMapping("/suppliers/edit/{id}")
    public String editSupplier(@PathVariable Integer id, Model model) {
        SupplierDto supplier = restTemplate.getForObject(BASE_URL + "/" + id, SupplierDto.class);
        model.addAttribute("supplier", supplier);
        return "harman/supplier-edit";
    }

    //  Update supplier
    @PostMapping("/suppliers/update/{id}")
    public String updateSupplier(@PathVariable Integer id, @ModelAttribute SupplierDto supplierDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SupplierDto> request = new HttpEntity<>(supplierDto, headers);
        restTemplate.exchange(BASE_URL + "/update/" + id, HttpMethod.PUT, request, Void.class);
        return "redirect:/suppliers";
    }
}
