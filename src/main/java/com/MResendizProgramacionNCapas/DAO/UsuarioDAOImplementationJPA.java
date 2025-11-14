
package com.MResendizProgramacionNCapas.DAO;

import com.MResendizProgramacionNCapas.JPA.DireccionJPA;
import com.MResendizProgramacionNCapas.JPA.RolJPA;
import com.MResendizProgramacionNCapas.JPA.UsuarioJPA;
import com.MResendizProgramacionNCapas.ML.Direccion;
import com.MResendizProgramacionNCapas.ML.Result;
import com.MResendizProgramacionNCapas.ML.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.hibernate.sql.exec.ExecutionException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioDAOImplementationJPA implements IUsuarioJPA{

    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private IUsuarioJRepository usuarioRepo;
    
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result Add(Usuario usuario) {

        Result result = new Result();     
        try{
            
            UsuarioJPA usuarioJPA = modelMapper.map(usuario, UsuarioJPA.class);
            
            RolJPA rolJPA =  entityManager.find(RolJPA.class,usuario.getRol().getIdRols());
            usuarioJPA.setRolJPA(rolJPA);
             
          

            if (usuario.getDirecciones() != null && !usuario.getDirecciones().isEmpty()) {
            List<DireccionJPA> direccionesJPA = usuario.getDirecciones()
                .stream()
                .map(d -> {
                    DireccionJPA direccionJPA = modelMapper.map(d, DireccionJPA.class);
                    direccionJPA.setUsuarioJPA(usuarioJPA);
                    return direccionJPA;
                })
                .collect(Collectors.toList());

            usuarioJPA.setDireccionesJPA(direccionesJPA);
        }               
            entityManager.persist(usuarioJPA);
       
        }catch(Exception ex){
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

    
    @Override
     public Result GetById(Integer IdUsuario){
         Result result = new Result();
         
         try {
                UsuarioJPA usuarioJPA = usuarioRepo.findById(IdUsuario).orElse(null);
                
                if(usuarioJPA !=null){
                Usuario usuariogetId = modelMapper.map(usuarioJPA, Usuario.class);
                result.correct = true;
                 result.object = usuariogetId;
                }
                 
        } catch (Exception ex) {
            result.correct =  false;
            result.errorMessage =  ex.getLocalizedMessage();
            result.ex =  ex;
            
        }
    
         return result;
     }
    
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result Update(Usuario usuario) {
        Result result = new Result();
        try {
            
           Optional<UsuarioJPA> usuarioOptional = usuarioRepo.findById(usuario.getIdUsuario());
           if(usuarioOptional.isPresent()){
               UsuarioJPA usuarioJPA =  usuarioOptional.get();
               UsuarioJPA usuarioUpdate = modelMapper.map(usuario, UsuarioJPA.class);
               usuarioUpdate.setPassword(usuarioJPA.getPassword());
               usuarioUpdate.setImagen(usuarioJPA.getImagen());
               usuarioRepo.save(usuarioUpdate);
           }
         
            result.correct =  true;
            
        } catch (Exception ex) {
            result.correct =  false;
            result.errorMessage =  ex.getLocalizedMessage();
            result.ex =  ex;
        }
        
        return result;
    }
    
    


    
    
    
}
