package com.spring_restfull.restfull.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.springframework.security.core.GrantedAuthority;

@Entity
@SequenceGenerator(name = "Seq_Role", sequenceName = "Seq_Role", allocationSize = 1, initialValue = 1)
public class Role implements GrantedAuthority{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Seq_Role")
    private Long id;
    private String nameRole; //papel, exemplo Role_Gerente

    @Override
    public String getAuthority() {
        return this.nameRole;
    }




    
}