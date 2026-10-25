package residenciageriatrica.models;

public class Residente extends Persona {

    public Residente(String id, String dni, String nombre, String apellido) {
        super(id, dni, nombre, apellido);
    }

    @Override
    public String getTipoPersona() {
        return "RESIDENTE";
    }
}