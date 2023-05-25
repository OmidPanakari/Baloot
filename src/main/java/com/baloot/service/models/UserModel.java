package com.baloot.service.models;

import java.util.List;

public record UserModel(String username, int credit, String email, String address, String birthDate,
                        List<CommodityItemModel> buyList, List<CommodityItemModel> purchased) {
}
