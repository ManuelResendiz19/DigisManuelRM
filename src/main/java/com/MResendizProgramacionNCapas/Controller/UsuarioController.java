package com.MResendizProgramacionNCapas.Controller;

import com.MResendizProgramacionNCapas.DAO.ColoniaDAOImplementation;
import com.MResendizProgramacionNCapas.DAO.EstadoDAOImplementation;
import com.MResendizProgramacionNCapas.DAO.MunicipioDAOImplementation;
import com.MResendizProgramacionNCapas.DAO.PaisDAOImplementation;
import com.MResendizProgramacionNCapas.DAO.RolDAOImplementation;
import com.MResendizProgramacionNCapas.ML.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.MResendizProgramacionNCapas.DAO.UsuarioDAOImplementation;
import com.MResendizProgramacionNCapas.DAO.UsuarioDAOImplementationJPA;
import com.MResendizProgramacionNCapas.ML.Direccion;
import com.MResendizProgramacionNCapas.ML.ErrorLoad;
import com.MResendizProgramacionNCapas.ML.Rol;
import com.MResendizProgramacionNCapas.ML.Usuario;
import com.MResendizProgramacionNCapas.Service.ValidateService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("usuario")// define la ruta base del controlador 
public class UsuarioController {

    @Autowired
    private ValidateService validateService;
    
    @Autowired
    private UsuarioDAOImplementation usuarioDAOImplementation;
    
    @Autowired
    private UsuarioDAOImplementationJPA usuarioDAOImplementationJPA;

    @Autowired
    private RolDAOImplementation rolDAOImplementation;

    @Autowired
    private PaisDAOImplementation paisDAOImplementation;

    @Autowired
    private EstadoDAOImplementation estadoDAOImplementation;

    @Autowired
    private MunicipioDAOImplementation municipioDAOImplementation;

    @Autowired
    private ColoniaDAOImplementation coloniaDAOImplementation;

    @GetMapping
    public String Index(Model model) {
        Result result = usuarioDAOImplementation.GetAll();
        Result resultJPA = usuarioDAOImplementationJPA.GetAll();
        Result resultRol = rolDAOImplementation.GetAll();
        
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("Usuarios", result.objects);
        model.addAttribute("Rols", resultRol.objects);

        return "UsuarioIndex";
    }
    
    @PostMapping("/usuarioSearch")
    public String Index(@ModelAttribute("usuario") Usuario usuario, Model model){
        Result result = usuarioDAOImplementation.UsuarioSearch(usuario);
        Result resultRol = rolDAOImplementation.GetAll();
        
        
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("usuarios", result.objects);
        model.addAttribute("Rols", resultRol.objects);
        
        return "UsuarioIndex"; 
                
    }
    
    
    @GetMapping("/bulkUpload")
    public String BulkUpload(){
        return "BulkUpload";
    }
    
    
//    @GetMapping("/bulkUpload/process")
//    public String BulkUpload(HttpSession httpSession){
//        String Path = httpSession.getAttribute("bulckUploadsFiles").toString();
//        httpSession.removeAttribute("bulckUploadsFiles");
//      
//        
//        return "BulkUpload";
//    }
//    
    
    @GetMapping("/bulkUpload/process")
    public String BulkUpload(HttpSession httpSession, List<Usuario> usuarios, Model model){
        String Path = httpSession.getAttribute("bulckUploadsFiles").toString();
        httpSession.removeAttribute("bulckUploadsFiles");
                
        String patArchivo= "src/main/resources/filesLoads";
        Result result = usuarioDAOImplementation.AddAll(usuarios);
        model.addAttribute("usuarios", result.objects);
        
        return "BulkUpload";
    }
    
    @PostMapping("/bulkUpload")
    public String BulkUpload(@RequestParam("archivo") MultipartFile archivo, Model model, HttpSession httpSession){
        String extensions = archivo.getOriginalFilename().split("\\.")[1];
        
        String path = System.getProperty("user.dir");
        String patArchivo = "src/main/resources/filesLoads";
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmSS"));
        String pathDefinty = path + "/" + patArchivo + "/" + fecha + archivo.getOriginalFilename();
        
        try{
        archivo.transferTo(new File(pathDefinty));
        }catch(Exception ex){
        
        }
        
        List<Usuario> usuarios;
        if(extensions.equals("txt")){
            usuarios = LecturaArchivoTXT(new File(pathDefinty));
            
            
        }else if(extensions.equals( "xlsx")){
            usuarios = LecturaArchivoXLSX(new File(pathDefinty));

        } else{
            model.addAttribute("errorMessage", "Manda un archivo que sea correcto");
           return "BulkUpload";
        }
        
        List<ErrorLoad> errores = ValidarDatosArchivo(usuarios);
            
            if(errores.isEmpty()){
                model.addAttribute("errores", false);
                httpSession.setAttribute("bulckUploadsFiles", pathDefinty);
            }else{
                model.addAttribute("errores", true);
                model.addAttribute("errores", errores);
            }
        return "BulkUpload";
    }
    
    
    public List<ErrorLoad> ValidarDatosArchivo(List<Usuario> usuarios) {

        List<ErrorLoad> errorLoads = new ArrayList<>();
        int lineaError = 0;

        for (Usuario usuario : usuarios) {
            lineaError++;
            BindingResult bindingResult = validateService.validateObject(usuario);
            List<ObjectError> errors = bindingResult.getAllErrors();
            for(ObjectError error : errors){
                FieldError fieldError = (FieldError) error;
                ErrorLoad errorLoad = new ErrorLoad();
                errorLoad.campo = fieldError.getField();
                errorLoad.descripcion = fieldError.getDefaultMessage();
                errorLoad.linea = lineaError;
                errorLoads.add(errorLoad);        
            }
        }
        return errorLoads;
    }

    
    
    
    public List<Usuario> LecturaArchivoTXT(File archivo){
        
        List<Usuario> usuarios = new ArrayList<>();
        
        try (InputStream fileInputStream = new FileInputStream(archivo);
                BufferedReader bufferReader = new BufferedReader(new InputStreamReader(fileInputStream));){
            String linea = "";
            
            while((linea = bufferReader.readLine()) !=null){
                String[] campos = linea.split("\\|");
                Usuario usuario = new Usuario();
                usuario.setNombre(campos[0]);
                usuario.setApellidoPaterno(campos[1]);
                usuario.setApellidoMaterno(campos[2]);
//                usuario.setFechaNacimiento(campos[3]);
                usuario.setUserName(campos[4]);
                usuario.setEmail(campos[5]);
                usuario.setSexo(campos[6]);
                usuario.setTelefono(campos[7]);
                usuario.setCelular(campos[8]);
                usuario.setCURP(campos[9]);
                
                usuarios.add(usuario);
            }
            
        } catch (Exception ex) {
            
            return null;
        }       
        return usuarios;
    }
    
    public List<Usuario> LecturaArchivoXLSX(File archivo){        
        List<Usuario> usuarios= new ArrayList<>();
        
        try(InputStream fileInputStream = new FileInputStream(archivo);
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream) ){
            XSSFSheet workSheet = workbook.getSheetAt(0);
            for(Row row :  workSheet){
                Usuario usuario = new Usuario();
                usuario.Rol = new Rol();
                
                usuario.setNombre(row.getCell(0).toString());
                usuario.setApellidoPaterno(row.getCell(1).toString());
                usuario.setApellidoMaterno(row.getCell(2).toString());
                usuario.setFechaNacimiento(row.getCell(3).getDateCellValue());
                usuario.setUserName(row.getCell(4).toString());
                usuario.setEmail(row.getCell(5).toString());
                usuario.setPassword(row.getCell(6).toString());
                usuario.setSexo(row.getCell(7).toString());
                usuario.setTelefono(row.getCell(8).toString());
                usuario.setCelular(row.getCell(9).toString());
                usuario.setCURP(row.getCell(10).toString());
                usuario.Rol.setIdRols((int) row.getCell(11).getNumericCellValue());
                
                usuarios.add(usuario);
            }      
        }catch(Exception ex){
            return null;
        }
        return usuarios;
    }
    
    
    @GetMapping("/detail/{IdUsuario}")
    public String Detail(@PathVariable("IdUsuario") int IdUsuario, Model model) {
        model.addAttribute("usuario", usuarioDAOImplementation.GetById(IdUsuario).object);
        model.addAttribute("direccion", new Direccion());
        
        return "UsuarioDetail";
    }

    @GetMapping("/add")
    public String Form(Model model) {

        Usuario usuario = new Usuario();
        usuario.Rol = new Rol();
//        Pais pais = new Pais();
//        Estado estado = new Estado();
//        Municipio municipio = new Municipio();
//        Colonia colonia = new Colonia();
        
        usuario.Direcciones = new ArrayList<>();
        Direccion direccion = new Direccion();
        usuario.Direcciones.add(direccion);

        Result resultRol = rolDAOImplementation.GetAll();
        Result resultPais = paisDAOImplementation.PaisGetAll();
//        Result resultEstado = estadoDAOImplementation.EstadosGetByIdPais(0);
//        Result resultMunicipio = municipioDAOImplementation.MunicipioGetByIdEstado(0);
//        Result resultColonia = coloniaDAOImplementation.GetByIdMunicipio(0);
        model.addAttribute("Rols", resultRol.objects); // Rols es el nombre del modelo que mandas a tu vista
        model.addAttribute("Usuario", usuario);
        model.addAttribute("Paises", resultPais.objects);
//        model.addAttribute("Estados", resultEstado.objects);
//        model.addAttribute("Municipios", resultMunicipio.objects);
//        model.addAttribute("Colonias", resultColonia.objects);
        
        return "UsuarioForm";
    }

    @PostMapping("/add")
    public String Form(@Valid @ModelAttribute("usuario") Usuario usuario,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model,
            @RequestParam("imagenFile") MultipartFile imagenFile) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("Usuario", usuario);
            model.addAttribute("rols", rolDAOImplementation.GetAll().objects);

            if (usuario.Direcciones.get(0).Colonia.Municipio.Estado.Pais.getIdPais() > 0) {
                model.addAttribute("estados", estadoDAOImplementation.EstadosGetByIdPais(usuario.Direcciones.get(0).Colonia.Municipio.Estado.Pais.getIdPais()).objects);

                if (usuario.Direcciones.get(0).Colonia.Municipio.Estado.getIdEstado() > 0) {
                    model.addAttribute("municipios", municipioDAOImplementation.MunicipioGetByIdEstado(usuario.Direcciones.get(0).Colonia.Municipio.Estado.getIdEstado()).objects);

                    if (usuario.Direcciones.get(0).Colonia.Municipio.getIdMunicipio() > 0) {
                        model.addAttribute("colonias", coloniaDAOImplementation.GetByIdMunicipio(usuario.Direcciones.get(0).Colonia.Municipio.getIdMunicipio()).objects);
                    }
                }
            }

            return "UsuarioForm";
        }
        
        if(imagenFile != null){
            try{
                String extension = imagenFile.getOriginalFilename().split("\\.")[1];
                if(extension.equals("jpg")||extension.equals("png")){
                    
                    byte[] byteImagen = imagenFile.getBytes();
                    String imagenBase64 = Base64.getEncoder().encodeToString(byteImagen);
                    usuario.setImagen(imagenBase64);
                }
            }catch(IOException ex){
                Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        redirectAttributes.addFlashAttribute("successMessage", "El usuario" + usuario.getUserName() + "Se creo con exito.");
        return "redirect:/usuario"; 

    }

//    @GetMapping("pais/{IdPais}")
//    @ResponseBody
//    public Result GetByIdPais(@PathVariable("IdPais") int IdPais) {
//        return paisDAOImplementation.GetByIdPais(IdPais);
//    }
    
    @PostMapping("upedidireccion/{IdUsuario}")
    public String UpEdiDireccion(@PathVariable("IdUsuario") int IdUsuario, @ModelAttribute("direccion") Direccion direccion, Model model){
    
        if(direccion.getIdDireccion() == 0){ // aqui se agrega la direccion
            usuarioDAOImplementation.AddDireccion(direccion, IdUsuario);
        }else{
            usuarioDAOImplementation.UpdateDireccion(direccion, IdUsuario);
        }
        
        return "redirect:/usuario/detail"+IdUsuario;
    }
    
    
    @PostMapping("/detail")
    public String UpdateUsuario(@ModelAttribute("Usuario") Usuario usuario) {
        Result result = usuarioDAOImplementation.UpdateUsuario(usuario);
        return "redirect:/usuario/detail/" + usuario.getIdUsuario();
    }
    
   
            
    
    @GetMapping("estado/{IdPais}")
    @ResponseBody
    public Result EstadosGetByIdPais(@PathVariable("IdPais") int IdPais) {
        return estadoDAOImplementation.EstadosGetByIdPais(IdPais);
    }

    @GetMapping("municipio/{IdEstado}")
    @ResponseBody
    public Result MunicipioGetByIdEstado(@PathVariable("IdEstado") int IdEstado) {
        return municipioDAOImplementation.MunicipioGetByIdEstado(IdEstado);
    }

    @GetMapping("colonia/{IdMunicipio}")
    @ResponseBody
    public Result GetByIdMunicipio(@PathVariable("IdMunicipio") int IdMunicipio) {
        return coloniaDAOImplementation.GetByIdMunicipio(IdMunicipio);
    }
}
