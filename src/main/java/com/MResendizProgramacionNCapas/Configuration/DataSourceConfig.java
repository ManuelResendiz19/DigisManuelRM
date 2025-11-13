
package com.MResendizProgramacionNCapas.Configuration;

import com.MResendizProgramacionNCapas.JPA.DireccionJPA;
import com.MResendizProgramacionNCapas.JPA.UsuarioJPA;
import com.MResendizProgramacionNCapas.ML.Direccion;
import com.MResendizProgramacionNCapas.ML.Usuario;
import javax.sql.DataSource;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        
        dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:orcl");
        dataSource.setUsername("MResendizProgramacionNCapasDia3");
        dataSource.setPassword("password1");
        
        return dataSource;
    }
    
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
    
    @Bean
    public ModelMapper modelMapper(){
        
        ModelMapper modelMapper =  new ModelMapper();
        modelMapper.typeMap(Usuario.class, UsuarioJPA.class);
        modelMapper.typeMap(Direccion.class, DireccionJPA.class);

        return modelMapper;
    }
    
}
