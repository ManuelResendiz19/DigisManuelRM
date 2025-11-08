
package com.MResendizProgramacionNCapas.DAO;

import com.MResendizProgramacionNCapas.ML.Pais;
import com.MResendizProgramacionNCapas.ML.Result;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PaisDAOImplementation implements IPaisDAO{
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public Result PaisGetAll(){  
        return jdbcTemplate.execute("{CALL PaisGetAll(?)}", (CallableStatementCallback<Result>) callableStatement ->{
            Result result = new Result();
          
          try{
              
              callableStatement.registerOutParameter(1, Types.REF_CURSOR);
              
              callableStatement.execute();
              
              ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
              
              
              
              result.objects = new ArrayList<>();
              
              while (resultSet.next()) {
                  Pais pais = new Pais();
                  pais.setIdPais(resultSet.getInt("IdPais"));
                  pais.setNombre(resultSet.getString("Nombre"));
                  
                  result.objects.add(pais);
                  
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
