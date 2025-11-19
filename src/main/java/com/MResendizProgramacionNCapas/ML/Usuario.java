
package com.MResendizProgramacionNCapas.ML;
import com.MResendizProgramacionNCapas.JPA.UsuarioJPA;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

public class Usuario {
    
    private int IdUsuario;
    
    @NotNull(message = "Este campo no debe ser nulo")
    @NotBlank (message = "Este campo debe contener datos y no debe estar vacia")
    @Size(min = 2, max = 20, message = "El nombre esta en un rango de 2 a 20 letras")    
    private String Nombre;
    
    @NotNull(message = "Este campo no debe ser nulo")
    @NotBlank (message = "Este campo debe contener datos y no debe estar vacia")
    @Size(min = 2, max = 20, message = "El Apellido Paterno esta en un rango de 2 a 20 letras")    
    private String ApellidoPaterno;
    
    @NotNull(message = "El campo no debe ser nulo")
    @NotBlank(message = "El campo debe contener datos y no debe estar vacia")
    @Size(min=4 , max = 20, message = "El Apellido Materno esta en un rango de 4 a 20 letras")
    private String ApellidoMaterno;
    
    @Past(message = "La fecha de Nacimiento debe ser menor a la actual")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date FechaNacimiento;
    
    @NotNull(message = "El campo no puede ser nulo")
    @NotBlank(message = "El campo debe contener datos y no debe estar vacio")
    @Size(min =10 , max = 15, message = "El numero de Telefono esta en un rango de 10 a 15 numeros")
    private String Telefono;
    
    @NotNull(message = "Este campo no puede ser nulo")
    @NotBlank(message = "Este campo debe contener datos y no debe estar vacia")
    @Size(min = 3, max = 10, message = "El User Name esta en un rango de 3 a 6 letras")
    private String UserName;
    
//    @NotNull(message = "Este campo no puede ser nulo")
//    @NotBlank(message = "Este campo debe contener datos y no debe estar vacia")    
//    @Size(min = 20, max = 40, message = "El Email tiene un rango de 20 a 40 letras")
    private String Email;
    
//    @NotNull(message = "Este campo no puede ser nulo")
//    @NotBlank(message = "Este campo debe contener datos y no debe estar vacia")
//    @Size(min = 4, max = 8, message = "El Password esta en un rango de 4 a 8 caracteres")
    private String Password;
    
    @NotNull(message = "Este campo no puede ser nulo")
    @NotBlank(message = "Este campo debe contener datos y no debe estar vacia")
    private String Sexo;
    
    @NotNull(message = "Este campo no puede ser nulo")
    @NotBlank(message = "Este campo debe contener datos y no debe estar vacia")
    @Size(min =10 , max = 15, message = "El numero de Celular esta en un rango de 10 a 15 numeros")
    private String Celular; 
    
    @NotNull(message = "Este campo no puede ser nulo")
    @NotBlank(message = "Este campo debe contener datos y no debe estar vacia")
    @Size(min = 16 , max = 18, message = "El CURP esta en un rango de 16 a 18 caracteres")
    private String CURP;
    
    
    public List<Direccion> Direcciones; 
    public Rol Rol;
    private String Imagen;
    
    public Usuario (){
        
    }
    
    public Usuario (String Nombre, String ApellidoPaterno, String ApellidoMaterno, Date FechaNacimiento, String Username,String Email, String Password, String Sexo, String Telefono, String Celular, String CURP){
        this.IdUsuario = IdUsuario;
        this.Nombre = Nombre;
        this.ApellidoPaterno = ApellidoPaterno;
        this.ApellidoMaterno = ApellidoMaterno;
        this.FechaNacimiento = FechaNacimiento;
        this.Telefono = Telefono;
        this.UserName = UserName;
        this.Email = Email;
        this.Password = Password;
        this.Sexo = Sexo;
        this.Celular = Celular;
        this.CURP = CURP;
    }
    
    
    public  Usuario(UsuarioJPA usuarioJPA){
        this.IdUsuario = usuarioJPA.getIdUsuario();
        this.Nombre = usuarioJPA.getNombre();
    }
    
    public void setIdUsuario (int IdUsuario){
        this.IdUsuario = IdUsuario;
    }
    
    public int getIdUsuario(){
        return IdUsuario;
    }
    
    public void setNombre (String Nombre){
        this.Nombre = Nombre;
    }
    
    public String getNombre(){
        return Nombre;
    }
    
    public void setApellidoPaterno(String ApellidoPaterno){
        this.ApellidoPaterno = ApellidoPaterno;
    }
    
    public String getApellidoPaterno (){
        return ApellidoPaterno;
    }
    
    public void setApellidoMaterno(String ApellidoMaterno){
        this.ApellidoMaterno = ApellidoMaterno;
    }
    
    public String getApellidoMaterno(){
        return ApellidoMaterno;
    }
    
    
    public void setFechaNacimiento(Date FechaNacimiento){
        this.FechaNacimiento = FechaNacimiento;
    }
    
    public Date getFechaNacimiento(){
        return FechaNacimiento;
    }   
    
    public void setTelefono(String Telefono){
        this.Telefono = Telefono;
    }
    
    public String getTelefono(){
        return Telefono;
    }
    
    public void setUserName(String UserName){
        this.UserName = UserName;
    }
    
    public String getUserName (){
        return UserName;
    }
    
    public void setEmail(String Email){
        this.Email = Email;
    }
    
    public String getEmail(){
        return Email;
    }
    
    public void setPassword(String Password){
        this.Password = Password;
    }
    
    public String getPassword(){
        return Password;
    }
    
    public void setSexo(String Sexo){
        this.Sexo = Sexo;
    }
   
    public String getSexo(){
        return Sexo;
    }
    
    public void setCelular(String Celular){
        this.Celular = Celular;
    }
    
    public String getCelular(){
        return Celular;
    }
    
    public void setCURP(String CURP){
        this.CURP = CURP;
    }
    
    public String getCURP(){
        return CURP;
    }

    public List<Direccion> getDirecciones() {
        return Direcciones;
    }

    public void setDirecciones(List<Direccion> Direcciones) {
        this.Direcciones = Direcciones;
    }

    public Rol getRol() {
        return Rol;
    }

    public void setRol(Rol Rol) {
        this.Rol = Rol;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String Imagen) {
        this.Imagen = Imagen;
    }

    
}
