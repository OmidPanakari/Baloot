package com.baloot.service;

import com.baloot.dataAccess.repositories.DiscountRepository;
import com.baloot.responses.DataResponse;
import com.baloot.responses.Response;

public class DiscountService {
    private final DiscountRepository discountRepository;

    public DiscountService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    public Response getDiscount(String code) {
        var discount = discountRepository.getDiscount(code);
        if (discount == null)
            return DataResponse.Failed("Discount not found!");
        return DataResponse.Successful(discount);
    }
}
