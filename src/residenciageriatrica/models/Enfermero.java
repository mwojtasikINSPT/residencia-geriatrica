package residenciageriatrica.models;

public class Enfermero extends Persona {

    public Enfermero(String id, String dni, String nombre, String apellido) {
        super(id, dni, nombre, apellido);
    }

    @Override
    public String getTipoPersona() {
        return "ENFERMERO";
    }
}