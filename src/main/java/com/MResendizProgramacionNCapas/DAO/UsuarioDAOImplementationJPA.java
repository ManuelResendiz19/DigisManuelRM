
package com.MResendizProgramacionNCapas.DAO;

import com.MResendizProgramacionNCapas.JPA.UsuarioJPA;
import com.MResendizProgramacionNCapas.ML.Result;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioDAOImplementationJPA implements IUsuarioDAOJPA{

    @Autowired
    private EntityManager entityManager;
    
    
    @Override
    public Result GetAll() {
        Result result =  new Result();
        
        try{
                TypedQuery<UsuarioJPA> queryUsuario = entityManager.createQuery("FROM UsuarioJPA", UsuarioJPA.class);
                List<UsuarioJPA> usuarios =  queryUsuario.getResultList();
        
                result.correct = true;
        }catch(Exception ex){
            result.correct =  false;
            result.errorMessage =  ex.getLocalizedMessage();
            result.ex =  ex;
            result.objects = null;
        }
        
        return result;
    }

}
