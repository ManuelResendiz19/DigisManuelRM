
package com.MResendizProgramacionNCapas.DAO;

import com.MResendizProgramacionNCapas.JPA.UsuarioJPA;
import com.MResendizProgramacionNCapas.ML.Result;
import com.MResendizProgramacionNCapas.ML.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioDAOImplementationJPA implements IUsuarioJPA{

    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Override
    public Result GetAll() {
        Result result = new Result();
        try{
            
            TypedQuery<UsuarioJPA> queryUsuario = entityManager.createQuery("FROM UsuarioJPA", UsuarioJPA.class);
            List<UsuarioJPA> usuarios = queryUsuario.getResultList();
            
            
            List<Usuario> usuariosDTO = usuarios.stream().map(usuario -> modelMapper.map(usuario, Usuario.class)).collect(Collectors.toList());
            
            result.objects = (List<Object>) (List<?>) usuariosDTO;
            result.correct = true;
            
            
        }catch(Exception ex){
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }

}
