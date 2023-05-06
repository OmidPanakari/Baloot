package com.baloot.presentation.controllers;

import com.baloot.presentation.utils.Container;
import com.baloot.responses.Response;
import com.baloot.service.DiscountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/discounts")
public class DiscountController {
    @GetMapping("/{code}")
    public Response getDiscount(@PathVariable String code) {
        var service = Container.resolve(DiscountService.class);
        return service.getDiscount(code);
    }
}
