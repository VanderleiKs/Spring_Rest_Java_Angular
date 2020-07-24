package com.spring_restfull.restfull.repositories;

import com.spring_restfull.restfull.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsusarioRepository extends JpaRepository<Usuario, Long>{
    
}