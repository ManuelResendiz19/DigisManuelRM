

package com.MResendizProgramacionNCapas.DAO;

import com.MResendizProgramacionNCapas.JPA.UsuarioJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IUsuarioJRepository extends JpaRepository<UsuarioJPA, Integer>{

    UsuarioJPA usuarioJPA =  new UsuarioJPA();
    
   
}
