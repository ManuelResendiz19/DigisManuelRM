

package com.MResendizProgramacionNCapas.DAO;

import com.MResendizProgramacionNCapas.ML.Result;
import com.MResendizProgramacionNCapas.ML.Usuario;


public interface IUsuarioJPA {

        Result GetAll();
        Result Add(Usuario usuario);
//        Result Update(Usuario usuario);
        
}
