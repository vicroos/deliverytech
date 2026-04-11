package com.deliverytech.delivery.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.api.model.Restaurante;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    
    boolean existsByNome(String nome);
    
    Page<Restaurante> findByAtivoTrue(Pageable pageable);
    
    Page<Restaurante> findByCategoriaAndAtivoTrue(String categoria, Pageable pageable);
}