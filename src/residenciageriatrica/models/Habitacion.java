package residenciageriatrica.models;

public class Habitacion {

    private final String codHabitacion;
    private Estado estado;
    private boolean activo;

    public Habitacion(String codHabitacion, Estado estado) {
        this.codHabitacion = codHabitacion;
        this.estado = estado;
        this.activo = true;
    }

    public String getCodHabitacion() {
        return codHabitacion;
    }

    public Estado getEstado() {
        return estado;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Habitacion{"
                + "codHabitacion='" + codHabitacion + '\''
                + ", estado=" + estado
                + ", activo=" + activo
                + '}';
    }
}