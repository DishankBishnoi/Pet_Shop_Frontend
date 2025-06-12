package com.lms.petshopfrontend.controller;

import com.lms.petshopfrontend.dto.AddressDto;
import com.lms.petshopfrontend.dto.CustomerFullDto;
import com.lms.petshopfrontend.dto.customerdto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class customercontrollers {

    @Autowired
    private RestTemplate restTemplate;

    // View paginated customer summary
    @GetMapping("/customersummary")
    public String viewCustomerSummaries(@RequestParam(defaultValue = "1") int page, Model model) {
        String url = "http://localhost:9090/api/customers/summary/all";
        ResponseEntity<customerdto[]> response = restTemplate.getForEntity(url, customerdto[].class);
        customerdto[] responseBody = response.getBody();

        List<customerdto> allCustomers = responseBody != null ? Arrays.asList(responseBody) : List.of();

        int pageSize = 5;
        int totalPages = (int) Math.ceil((double) allCustomers.size() / pageSize);
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, allCustomers.size());

        List<customerdto> customers = allCustomers.subList(start, end);

        model.addAttribute("customers", customers);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("prevPage", Math.max(1, page - 1));
        model.addAttribute("nextPage", Math.min(totalPages, page + 1));
        model.addAttribute("pages", IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList()));

        return "rahul/RahulmainPage";
    }

    // Show edit form for a specific customer
    @GetMapping("/customer/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        String apiUrl = "http://localhost:9090/api/customers/" + id;
        CustomerFullDto customer = restTemplate.getForObject(apiUrl, CustomerFullDto.class);

        if (customer == null) {
            // Optional: Handle null case (maybe redirect or throw)
            return "redirect:/error-page"; // or another fallback
        }

        if (customer.getAddress() == null) {
            customer.setAddress(new AddressDto());
        }


        model.addAttribute("customerFullDto", customer);
        return "rahul/edit-customer";
    }

    // Handle PUT request to update customer
    @PostMapping("/customer/update/{id}")
    public String updateCustomer(@PathVariable Integer id,
                                 @ModelAttribute("customerFullDto") CustomerFullDto customerDto) {
        String apiUrl = "http://localhost:9090/api/customers/" + id + "/full";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CustomerFullDto> requestEntity = new HttpEntity<>(customerDto, headers);
        restTemplate.exchange(apiUrl, HttpMethod.PUT, requestEntity, Void.class);

        return "redirect:/customersummary";

    }

    // Show add-customer form
    @GetMapping("/add-customer")
    public String showCustomerForm(Model model) {
        CustomerFullDto customer = new CustomerFullDto();
        customer.setAddress(new AddressDto()); // prevent null reference
        model.addAttribute("customerFullDto", customer);
        return "rahul/add-customer";
    }

    // Submit new customer
    @PostMapping("/add-customer")
    public String submitCustomer(@ModelAttribute CustomerFullDto customerFullDto) {
        String apiUrl = "http://localhost:9090/api/customers/frontend";
        restTemplate.postForEntity(apiUrl, customerFullDto, String.class);
        return "redirect:/customersummary";
    }

//    @GetMapping("/RahulPatel")
//    public String showRahulPatelPage() {
//        return "rahul/RahulmainPage";
//    }
}
