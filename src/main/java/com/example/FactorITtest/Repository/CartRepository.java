package com.example.FactorITtest.Repository;

import com.example.FactorITtest.Entities.CartEntity;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Xavier Pocchettino
 */
@Repository
public interface CartRepository extends CrudRepository<CartEntity, Long> {
    
    @Query(value = "SELECT COUNT(*) FROM cart", nativeQuery = true)
    Long countCarts();
    
    @Query(value = "SELECT SUM(p.price) FROM cart_product cp INNER JOIN product p "
        + "ON cp.product_id = p.id WHERE cp.cart_entity_id = :id", nativeQuery = true)
    BigDecimal sumProductPrice(@Param("id") Long id);
    
    @Query(value = "SELECT MIN(p.price) FROM cart c "
        + "INNER JOIN cart_product cp ON c.id = cp.cart_entity_id "
        + "INNER JOIN product p ON p.id = cp.product_id WHERE c.user_id = :userId", nativeQuery = true)
    BigDecimal getMinPriceFromCart(@Param("userId") Long userId);
}
