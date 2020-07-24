package com.spring_restfull.restfull.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import com.spring_restfull.restfull.model.Usuario;
import com.spring_restfull.restfull.repositories.UsusarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@CrossOrigin
@RestController
@RequestMapping(value = "/usuario")
public class UsuarioController {

    @Autowired
    UsusarioRepository ususarioRepository;
    Usuario u = new Usuario(null, "vanderlei", "e2345", "vanderlei k silva");

    @GetMapping
    public ResponseEntity<List<Usuario>> findAll(){
        final List<Usuario> list = (List<Usuario>) ususarioRepository.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Usuario> findById(@PathVariable Long id){
        Optional<Usuario> usuario = ususarioRepository.findById(id);
        return ResponseEntity.ok().body(usuario.get());
    }

    @PostMapping(value = "/cadastrar", produces = "application/json")
    public ResponseEntity<Usuario> saveById(@RequestBody final Usuario usuario){
      
        for(int i = 0; i < usuario.getTelefones().size(); i++){
            usuario.getTelefones().get(i).setUsuario(usuario);
        }
        final Usuario user = ususarioRepository.save(usuario);
        final URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(user);
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Usuario> updateById(@RequestBody final Usuario usuario){
        for(int i=0; i< usuario.getTelefones().size(); i++){
            usuario.getTelefones().get(i).setUsuario(usuario);
        }
        final Usuario user = ususarioRepository.save(usuario);
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping(value = "/{id}")
    public String deleteById(@PathVariable final Long id){
        ususarioRepository.deleteById(id);
        return "Excluido com sucesso!";
    }    
}