package com.laptopshop.library.repository;

import com.laptopshop.library.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query(value = "update cart_items set shopping_cart_id = null where shopping_cart_id = ?1", nativeQuery = true)
    void deleteCartItemById(Long cartId);

    @Modifying
    @Query(value = "delete from CartItem c where c.product.id = :productId")
    void deleteByProductId(@Param("productId") Long productId);
}
