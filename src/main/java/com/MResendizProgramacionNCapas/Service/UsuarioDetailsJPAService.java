
package com.MResendizProgramacionNCapas.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.MResendizProgramacionNCapas.DAO.IUsuarioJPARepository;
import com.MResendizProgramacionNCapas.JPA.UsuarioJPA;
import org.springframework.security.core.userdetails.User;

@Service
public class UsuarioDetailsJPAService  implements UserDetailsService{

    private final IUsuarioJPARepository iUsuarioJPARepository;
    
    public UsuarioDetailsJPAService(IUsuarioJPARepository iUsuarioJPARepository){
        this.iUsuarioJPARepository = iUsuarioJPARepository;
    }
   
    
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        
        UsuarioJPA usuario =  iUsuarioJPARepository.findByUserName(userName);
        
        return User.withUsername(usuario.getUserName())
                .password(usuario.getPassword())
                .build();
    }

    
    
}
