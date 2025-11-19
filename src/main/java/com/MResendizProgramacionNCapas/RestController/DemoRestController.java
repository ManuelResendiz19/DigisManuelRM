
package com.MResendizProgramacionNCapas.RestController;


import com.MResendizProgramacionNCapas.JPA.Result;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/demo")
public class DemoRestController {
    
    @GetMapping("/saludo/{nombre}")
    public String Saludo(@PathVariable("nombre") String nombre){
        
        return "Hola " + nombre + " " + "(つ▀¯▀)つ✧" ;
    }
    
    @GetMapping("division")
    public ResponseEntity Division(@RequestParam ("numeroUno") int numeroUno, @RequestParam("numeroDos") int numeroDos){
        Result result = new Result();
        try {
            if(numeroDos == 0 ){
                result.correct =  false;
                result.errorMessage =  "Error no se puede dividir entre cero intente con otro numero";
               result.status = 400; 
            }else{
                int division = numeroUno / numeroDos;
                result.correct = true;
                result.status = 200;
            }
            
            
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage =  ex.getLocalizedMessage();
            result.ex = ex;
            result.status =  500;
        }
        
        return ResponseEntity.status(result.status).body(result);
    }
    
    @GetMapping("multiplicacion")
    public ResponseEntity Multiplicacion(@RequestParam("numero1" )int numero1, @RequestParam("numero2") int numero2){
     Result result = new Result();
    
        try {
            
            
            int multiplicacion = numero1 * numero2;
            result.correct = true;
            result.status = 200;
            
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
     
     return ResponseEntity.status(result.status).body(result);
    }
    
    
    @PostMapping("suma")
    public ResponseEntity Suma(@RequestBody int[] Numeros){
        Result result = new Result();
        
        try {
            
            int suma = 0;
            
            for(int numero : Numeros){
                suma += numero;
            }
            
            result.correct = true;
            result.status = 200;
            result.object = suma;
            
        } catch (Exception ex) {
            result.correct =  false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        
        return ResponseEntity.status(result.status).body(result);
    }
}
