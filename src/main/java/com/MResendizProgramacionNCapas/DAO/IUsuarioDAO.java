
package com.MResendizProgramacionNCapas.DAO;

import com.MResendizProgramacionNCapas.ML.Result;
import com.MResendizProgramacionNCapas.DAO.UsuarioDAOImplementation;
import com.MResendizProgramacionNCapas.ML.Direccion;
import com.MResendizProgramacionNCapas.ML.Usuario;
import java.util.List;

public interface IUsuarioDAO {
        Result GetAll();
        Result Add(Usuario usuario);
        Result GetById(int IdUsuario);
        Result UpdateUsuario(Usuario usuario);
        Result UpdateDireccion(Direccion direccion, int IdUsuario);
        Result AddDireccion(Direccion direccion, int IdUsuario);
        
        Result AddAll(List<Usuario> usuarios);
}
