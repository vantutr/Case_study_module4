package com.codegym.fashionshop.dto;

import lombok.Data;

@Data
public class CheckoutDto {
    private String customerName;
    private String customerPhone;
    private String shippingAddress;
    private String customerEmail;
}