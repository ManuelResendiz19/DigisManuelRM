
package com.MResendizProgramacionNCapas.JPA;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ROLS")
public class RolJPA {
   
    @Id
    @Column(name = "idrols")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int IdRols;
    
    @Column(name = "nombrerol")
    private String NombreRol;
    

    public int getIdRols() {
        return IdRols;
    }

    public void setIdRols(int IdRols) {
        this.IdRols = IdRols;
    }

    public String getNombreRol() {
        return NombreRol;
    }

    public void setNombreRol(String NombreRol) {
        this.NombreRol = NombreRol;
    }
    
    
}
