package com.codegym.fashionshop.service.impl;

import com.codegym.fashionshop.dto.CartDto;
import com.codegym.fashionshop.dto.CartItemDto;
import com.codegym.fashionshop.model.ProductVariant;
import org.springframework.stereotype.Service;

import com.codegym.fashionshop.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Service
public class CartService {

    @Autowired
    private ProductVariantRepository variantRepository;

    private static final String CART_SESSION_KEY = "shoppingCart";

    public CartDto getCart(HttpSession session) {
        CartDto cart = (CartDto) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new CartDto();
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }

    public void addToCart(Long variantId, ProductVariant variant, int quantity, HttpSession session) {
        CartDto cart = getCart(session);
        Map<Long, CartItemDto> items = cart.getItems();

        int stockQuantity = variant.getQuantity();

        int currentQuantityInCart = 0;
        if (items.containsKey(variantId)) {
            currentQuantityInCart = items.get(variantId).getQuantity();
        }

        // Kiểm tra tổng số lượng (mới + cũ) có vượt quá tồn kho không
        if (currentQuantityInCart + quantity > stockQuantity) {
            // THAY ĐỔI THÔNG BÁO LỖI Ở ĐÂY
            String errorMessage = String.format(
                    "Số lượng tồn kho cho sản phẩm '%s' không đủ. Bạn đã có %d sản phẩm này trong giỏ hàng.",
                    variant.getProduct().getName(),
                    currentQuantityInCart
            );
            throw new RuntimeException(errorMessage);
        }

        if (items.containsKey(variantId)) {
            CartItemDto item = items.get(variantId);
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            items.put(variantId, new CartItemDto(variant, quantity));
        }
    }

    public void updateCart(Long variantId, int quantity, HttpSession session) {
        CartDto cart = getCart(session);
        if (cart.getItems().containsKey(variantId)) {
            if (quantity <= 0) {
                removeFromCart(variantId, session);
                return;
            }

            ProductVariant variant = variantRepository.findByIdWithProduct(variantId)
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại."));

            int stockQuantity = variant.getQuantity() - variant.getSoldQuantity();

            if (quantity > stockQuantity) {
                // Bây giờ câu lệnh này sẽ hoạt động vì product đã được tải
                throw new RuntimeException("Số lượng tồn kho cho sản phẩm '" + variant.getProduct().getName() + "' không đủ.");
            }

            cart.getItems().get(variantId).setQuantity(quantity);
        }
    }

    public void removeFromCart(Long variantId, HttpSession session) {
        CartDto cart = getCart(session);
        cart.getItems().remove(variantId);
    }
}