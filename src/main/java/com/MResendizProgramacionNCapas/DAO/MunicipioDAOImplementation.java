
package com.MResendizProgramacionNCapas.DAO;

import com.MResendizProgramacionNCapas.ML.Municipio;
import com.MResendizProgramacionNCapas.ML.Result;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MunicipioDAOImplementation implements IMunicipioDAO {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public Result MunicipioGetByIdEstado(int IdEstado){
        return jdbcTemplate.execute("{CALL MunicipioGetByIdEstado(?,?)}", (CallableStatementCallback<Result>) callableStatemen ->{
          Result result = new Result();
          
          try{
          
              callableStatemen.setInt(1,IdEstado);
              callableStatemen.registerOutParameter(2,java.sql.Types.REF_CURSOR);
              
              callableStatemen.execute();
              
              
              ResultSet resultSet = (ResultSet) callableStatemen.getObject(2);
              
              result.objects = new ArrayList<>();
              
              while (resultSet.next()) {
                  Municipio municipio = new Municipio();
                  municipio.setIdMunicipio(resultSet.getInt("IdMunicipio"));
                  municipio.setNombre(resultSet.getString("Nombre"));
                  
                  result.objects.add(municipio);
                  
              }
              result.correct = true;
              
          }catch(Exception ex){
              result.correct = false;
              result.errorMessage = ex.getLocalizedMessage();
              result.ex = ex;
              result.objects = null;
          }
          return result;
      });
    }
}
