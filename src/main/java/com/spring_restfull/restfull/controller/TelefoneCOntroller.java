package com.spring_restfull.restfull.controller;

import com.spring_restfull.restfull.model.Telefone;
import com.spring_restfull.restfull.repositories.TelefoneRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;


public class TelefoneCOntroller {
    
    
    private TelefoneRepository telefoneRepository;

    
    public ResponseEntity<Telefone> saveTelefone(@RequestBody Telefone telefone){
        Telefone tel = telefoneRepository.save(telefone);
        return ResponseEntity.ok().body(tel);
    }

}