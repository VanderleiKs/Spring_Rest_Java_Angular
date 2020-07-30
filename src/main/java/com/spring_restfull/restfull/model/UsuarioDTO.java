package com.spring_restfull.restfull.model;

import java.io.Serializable;

public class UsuarioDTO implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private String userName;
    private String userLogin;

    public UsuarioDTO(Usuario usuario){
        this.userName = usuario.getNome();
        this.userLogin = usuario.getLogin();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

}