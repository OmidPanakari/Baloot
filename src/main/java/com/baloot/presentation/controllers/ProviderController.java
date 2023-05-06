package com.baloot.presentation.controllers;

import com.baloot.presentation.utils.Container;
import com.baloot.responses.Response;
import com.baloot.service.ProviderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = {"Authorization"})
@RestController
@RequestMapping("/providers")
public class ProviderController {
    @GetMapping("/{id}")
    public Response GetProviderById(@PathVariable int id, HttpServletRequest request) {
        var service = Container.resolve(ProviderService.class);
        var username = (String) request.getServletContext().getAttribute("username");
        return service.getProviderById(id, username);
    }
}
