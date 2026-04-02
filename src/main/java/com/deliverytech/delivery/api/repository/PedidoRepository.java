package com.deliverytech.delivery.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deliverytech.delivery.api.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {}