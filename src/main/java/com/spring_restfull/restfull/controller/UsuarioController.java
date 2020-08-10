package com.spring_restfull.restfull.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.spring_restfull.restfull.model.Usuario;
import com.spring_restfull.restfull.model.UsuarioDTO;
import com.spring_restfull.restfull.repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    UsuarioRepository ususarioRepository;
    Usuario u = new Usuario(null, "vanderlei", "e2345", "vanderlei k silva");

    @GetMapping
    @Cacheable(value = "cacheUsuarios")
    public ResponseEntity<List<UsuarioDTO>> findAll() {
        final List<Usuario> list = (List<Usuario>) ususarioRepository.findAll();
        List<UsuarioDTO> listResponse = new ArrayList<>();
        for (Usuario usuario : list) {
            listResponse.add(new UsuarioDTO(usuario));
        }
        return ResponseEntity.ok().body(listResponse);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UsuarioDTO> findById(@PathVariable final Long id) {
        final Optional<Usuario> usuario = ususarioRepository.findById(id);
        return ResponseEntity.ok().body(new UsuarioDTO(usuario.get()));
    }

    @PostMapping(value = "/cadastrar")
    @CacheEvict(value = "cacheUsuarios", allEntries = true)
    public ResponseEntity<Usuario> saveById(@RequestBody Usuario usuario) {
        for (int i = 0; i < usuario.getTelefones().size(); i++) {
            usuario.getTelefones().get(i).setUsuario(usuario);
        }
        final String senhaCript = new BCryptPasswordEncoder().encode(usuario.getSenha());
        usuario.setSenha(senhaCript);
        final Usuario user = ususarioRepository.save(usuario);
        final URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(user);
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    @CachePut("cacheUsuarios")
    public ResponseEntity<Usuario> updateById(@RequestBody final Usuario usuario) {
        for (int i = 0; i < usuario.getTelefones().size(); i++) {
            usuario.getTelefones().get(i).setUsuario(usuario);
        }
        final Usuario userTestSenha = ususarioRepository.findUserById(usuario.getId());
        if(!userTestSenha.getSenha().equals(usuario.getSenha())){
            final String senhaCript = new BCryptPasswordEncoder().encode(usuario.getSenha());
            usuario.setSenha(senhaCript);
        }
        final Usuario user = ususarioRepository.save(usuario);
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping(value = "/{id}")
    public String deleteById(@PathVariable final Long id) {
        ususarioRepository.deleteById(id);
        return "Excluido com sucesso!";
    }
}