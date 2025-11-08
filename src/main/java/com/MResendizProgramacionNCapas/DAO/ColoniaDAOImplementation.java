
package com.MResendizProgramacionNCapas.DAO;

import com.MResendizProgramacionNCapas.ML.Colonia;
import com.MResendizProgramacionNCapas.ML.Result;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Types;

@Repository
public class ColoniaDAOImplementation implements IColoniaDAO {
 
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    
    @Override
    public Result GetByIdMunicipio(int IdMunicipio){
        return jdbcTemplate.execute("{CALL ColoniaGetByIdMunicipio(?,?)}", (CallableStatementCallback<Result>) callableStatement ->{
            Result result = new Result();
            
            try {
                callableStatement.setInt(1, IdMunicipio);
                callableStatement.registerOutParameter(2, Types.REF_CURSOR);
                callableStatement.execute();
                
                ResultSet resultSet = (ResultSet)  callableStatement.getObject(2);
                
                result.objects = new ArrayList<>();
                while(resultSet.next()){
                        Colonia colonia = new Colonia();
                        colonia.setIdColonia(resultSet.getInt("IdColonia"));
                        colonia.setNombre(resultSet.getString("Nombre"));
                        colonia.setCodigoPostal(resultSet.getString("CodigoPostal"));
                        
                        result.objects.add(colonia);     
                }
                result.correct = true; 
            } catch (Exception ex) {
                result.correct = false;
                result.errorMessage = ex.getLocalizedMessage();
                result.ex= ex;
                result.objects = null;
            }
            return result;
        });
    }
}
