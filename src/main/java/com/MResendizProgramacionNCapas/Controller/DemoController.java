
package com.MResendizProgramacionNCapas.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DemoController {

    @GetMapping("/saludo/{nombre}")
    public String Saludo(@PathVariable("nombre") String nombre, Model model){
        model.addAttribute("nombre", nombre);
        return "HolaMundo";
    }
    
}
