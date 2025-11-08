
package com.MResendizProgramacionNCapas.DAO;

import com.MResendizProgramacionNCapas.ML.Estado;
import com.MResendizProgramacionNCapas.ML.Result;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.CallableStatementCallback;


@Repository
public class EstadoDAOImplementation implements IEstadoDAO{

    @Autowired
    private JdbcTemplate jdbcTemplate;
    

      @Override
      public Result EstadosGetByIdPais(int IdPais) {
        return jdbcTemplate.execute("{CALL EstadosGetByIdPais(?,?)}", (CallableStatementCallback<Result>) callableStatemen ->{
          Result result = new Result();
          
          try{
          
              callableStatemen.setInt(1,IdPais);
              callableStatemen.registerOutParameter(2,java.sql.Types.REF_CURSOR);
              
              callableStatemen.execute();
              
              
              ResultSet resultSet = (ResultSet) callableStatemen.getObject(2);
              
              result.objects = new ArrayList<>();
              
              while (resultSet.next()) {
                  Estado estado = new Estado();
                  estado.setIdEstado(resultSet.getInt("IdEstado"));
                  estado.setNombre(resultSet.getString("Nombre"));
                  
                  result.objects.add(estado);
                  
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
