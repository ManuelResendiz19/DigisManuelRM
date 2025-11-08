package com.MResendizProgramacionNCapas.DAO;

import com.MResendizProgramacionNCapas.ML.Direccion;
import com.MResendizProgramacionNCapas.ML.Result;
import com.MResendizProgramacionNCapas.ML.Rol;

import com.MResendizProgramacionNCapas.ML.Usuario;
import java.sql.Types;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.springframework.jdbc.core.CallableStatementCallback;

@Repository
public class UsuarioDAOImplementation implements IUsuarioDAO {

    //Inyeccion de dependencias
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Result GetAll() {
        Result result = jdbcTemplate.execute("{CALL UsuarioDireccionesGetAll(?)}", (CallableStatementCallback<Result>) callableStatement -> {
            Result resultSP = new Result();

            try {
                callableStatement.registerOutParameter(1, Types.REF_CURSOR);
                callableStatement.execute();

                ResultSet resultSet = (ResultSet) callableStatement.getObject(1);

                int idUsuario = 0;

                resultSP.objects = new ArrayList<>();

                while (resultSet.next()) {

                    idUsuario = resultSet.getInt("IdUsuario");

                    if (!resultSP.objects.isEmpty() && idUsuario == ((Usuario) (resultSP.objects.get(resultSP.objects.size() - 1))).getIdUsuario()) {
                        Direccion direccion = new Direccion();

                        direccion.setCalle(resultSet.getString("Calle"));
                        direccion.setNumeroExterior(resultSet.getString("NumeroExterior"));
                        direccion.setNumeroInterior(resultSet.getString("NumeroInterior"));

                        Usuario usuario = ((Usuario) (resultSP.objects.get(resultSP.objects.size() - 1)));
                        usuario.Direcciones.add(direccion);
                    } else {
                        Usuario usuario = new Usuario();

                        usuario.setIdUsuario(resultSet.getInt("IdUsuario"));
                        usuario.setNombre(resultSet.getString("NombreUsuario"));
                        usuario.setApellidoPaterno(resultSet.getString("ApellidoPaterno"));
                        usuario.setApellidoMaterno(resultSet.getString("ApellidoMaterno"));
                        usuario.setFechaNacimiento(resultSet.getDate("FechaNacimiento"));
                        usuario.setTelefono(resultSet.getString("Telefono"));
                        usuario.setUserName(resultSet.getString("UserName"));
                        usuario.setEmail(resultSet.getString("Email"));
                        usuario.setPassword(resultSet.getString("Password"));
                        usuario.setSexo(resultSet.getString("Sexo"));
                        usuario.setCelular(resultSet.getString("Celular"));
                        usuario.setCURP(resultSet.getString("CURP"));
                        
                        usuario.Direcciones = new ArrayList<>();

                        Direccion direccion = new Direccion();

                        direccion.setCalle(resultSet.getString("Calle"));
                        direccion.setNumeroExterior(resultSet.getString("NumeroExterior"));
                        direccion.setNumeroInterior(resultSet.getString("NumeroInterior"));

                        usuario.Direcciones.add(direccion);
                        resultSP.objects.add(usuario);
                    }
                }
                resultSP.correct = true;
            } catch (Exception ex) {
                resultSP.correct = false;
                resultSP.errorMessage = ex.getLocalizedMessage();
                resultSP.ex = ex;
            }
            return resultSP;
        });

        return result;
    }

    @Override
    public Result Add(Usuario usuario) {

        Result result = new Result();

        result.correct = jdbcTemplate.execute("{CALL UsuarioDireccionADD(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}", (CallableStatementCallback<Boolean>) callableStatement -> {

            callableStatement.setString(1, usuario.getNombre());
            callableStatement.setString(2, usuario.getApellidoPaterno());
            callableStatement.setString(3, usuario.getApellidoMaterno());
            callableStatement.setDate(4, new java.sql.Date(usuario.getFechaNacimiento().getTime()));
            callableStatement.setString(5, usuario.getUserName());
            callableStatement.setString(6, usuario.getEmail());
            callableStatement.setString(7, usuario.getPassword());
            callableStatement.setString(8, usuario.getSexo());
            callableStatement.setString(9, usuario.getTelefono());
            callableStatement.setString(10, usuario.getCelular());
            callableStatement.setString(11, usuario.getCURP());
            callableStatement.setString(12, usuario.Direcciones.get(0).getCalle());
            callableStatement.setString(13, usuario.Direcciones.get(0).getNumeroExterior());
            callableStatement.setString(14, usuario.Direcciones.get(0).getNumeroInterior());
            callableStatement.setInt(15, usuario.Direcciones.get(0).Colonia.getIdColonia());

            callableStatement.execute();

            return true;
        });

        return result;
    }

    @Override
    public Result GetById(int IdUsuario) {
        return jdbcTemplate.execute("{CALL UsuarioDireccionesGetByIdUsuario(?,?)}", (CallableStatementCallback<Result>) callableStatent -> {

            Result result = new Result();

            try {
                callableStatent.setInt(1, IdUsuario);
                callableStatent.registerOutParameter(2, Types.REF_CURSOR);

                callableStatent.execute();

                ResultSet resultSet = (ResultSet) callableStatent.getObject(2);

                int idUsuario = 0;

                if (resultSet.next()) {

                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(resultSet.getInt("IdUsuario"));
                    usuario.setNombre(resultSet.getString("Nombre"));
                    usuario.setApellidoPaterno(resultSet.getString("ApellidoPaterno"));
                    usuario.setApellidoMaterno(resultSet.getString("ApellidoMaterno"));
                    usuario.setFechaNacimiento(resultSet.getDate("FechaNacimiento"));
                    usuario.setUserName(resultSet.getString("UserName"));
                    usuario.setEmail(resultSet.getString("Email"));
                    usuario.setSexo(resultSet.getString("Sexo"));
                    usuario.setTelefono(resultSet.getString("telefono"));
                    usuario.setCelular(resultSet.getString("Celular"));
                    usuario.setCURP(resultSet.getString("CURP"));

                    usuario.Direcciones = new ArrayList<>();

                    do {
                        Direccion direccion = new Direccion();
                        direccion.setCalle(resultSet.getString("Calle"));
                        direccion.setNumeroExterior(resultSet.getString("NumeroExterior"));
                        direccion.setNumeroInterior(resultSet.getString("NumeroInterior"));
                        usuario.Direcciones.add(direccion);

                    } while (resultSet.next());
                    result.object = usuario;
                    result.correct = true;
                }
            } catch (Exception ex) {
                result.correct = false;
                result.errorMessage = ex.getLocalizedMessage();
                result.ex = ex;
            }
            return result;
        });

    }

    public Result UpdateUsuario(Usuario usuario) {
        return jdbcTemplate.execute("{CALL UsuarioUpdate(?,?,?,?,?,?,?,?,?,?,?)}", (CallableStatementCallback<Result>) callableStatement ->{
            
            Result result = new Result();
            try{
                
                callableStatement.setInt(1, usuario.getIdUsuario());
                callableStatement.setString(2, usuario.getNombre());
                callableStatement.setString(3, usuario.getApellidoPaterno());
                callableStatement.setString(4, usuario.getApellidoMaterno());
                callableStatement.setDate(5, new java.sql.Date(usuario.getFechaNacimiento().getTime()));
                callableStatement.setString(6, usuario.getUserName());
                callableStatement.setString(7, usuario.getEmail());
                callableStatement.setString(8, usuario.getSexo());
                callableStatement.setString(9, usuario.getTelefono());
                callableStatement.setString(10, usuario.getCelular());
                callableStatement.setString(11, usuario.getCURP());
                
                int rowAffected = callableStatement.executeUpdate();
                if(rowAffected > 0){
                    result.correct = true;
                }else{
                    result.correct = false;
                }
                
            }catch(Exception ex){
                result.correct = false;
                result.errorMessage = ex.getLocalizedMessage();
                result.ex = ex;
            }
            return result;
        });
    }

    public Result AddDireccion(Direccion direccion, int IdUsuario) {
       return jdbcTemplate.execute("{CALL AddDireccion(?,?,?)}", (CallableStatementCallback<Result>) callableStatement ->{
           Result result =new Result();
           
           try{
               callableStatement.setString(1, direccion.getCalle());
               callableStatement.setString(2, direccion.getNumeroExterior());
               callableStatement.setString(3, direccion.getNumeroInterior());
               
               callableStatement.execute();
               
               result.correct= true;
           
           }catch(Exception ex){
               result.correct = false;
               result.errorMessage = ex.getLocalizedMessage();
               result.ex = ex;
           }
       
       return result;
       });
    }
    
    @Override
    public Result UpdateDireccion(Direccion direccion, int IdUsuario) {
        return jdbcTemplate.execute("{CALL UpdateDirecciones(?,?,?,?)}", (CallableStatementCallback<Result>) callableStatement ->{
        
            Result result = new Result();
            
            try{
                callableStatement.setString(1, direccion.getCalle());
                callableStatement.setString(2, direccion.getNumeroExterior());
                callableStatement.setString(3, direccion.getNumeroInterior());
                
                
                int rowAffected = callableStatement.executeUpdate();
                if(rowAffected > 0){
                    result.correct = true;
                    
                }else{
                    result.correct = false;
                }
                
            }catch(Exception ex){
                result.correct = false;
                result.errorMessage = ex.getLocalizedMessage();
                result.ex = ex;
            
            }
            return result;
        });
    }

    public Result UsuarioSearch(Usuario usuario) {
         return jdbcTemplate.execute("{CALL UsuarioGetAllDinamic(?,?,?,?,?)}", (CallableStatementCallback<Result>) callableStatement->{
         
             Result result = new Result();
             
             try{
                 
                 callableStatement.setString(1, usuario.getNombre() );
                 callableStatement.setString(2, usuario.getApellidoPaterno());
                 callableStatement.setString(3, usuario.getApellidoMaterno());
                 callableStatement.setInt(4, usuario.Rol.getIdRols());
                 callableStatement.registerOutParameter(5, Types.REF_CURSOR);
                 
                 ResultSet resultSet = (ResultSet) callableStatement.getObject(5);
                 
                 callableStatement.execute();
                 
                 result.objects = new ArrayList<>();
                 
                 while(resultSet.next()){
                           Usuario u = new Usuario();
                           u.setNombre(resultSet.getString("Nombre"));
                           u.setApellidoPaterno(resultSet.getString("ApellidoPaterno"));
                           u.setApellidoMaterno(resultSet.getString("ApellidoMaterno"));
                           u.Rol.setIdRols(resultSet.getInt("IdRols"));
                           result.objects.add(u);
                 }
                 
                 result.correct = true;
             
             }catch(Exception ex){
             
                 result.correct = false;
                 result.errorMessage = ex.getLocalizedMessage();
                 result.ex = ex;
             }            
             return result;
         });
    }
    
    
}
