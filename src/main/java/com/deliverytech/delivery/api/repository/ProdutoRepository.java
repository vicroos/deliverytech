package com.deliverytech.delivery.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.api.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>{}
