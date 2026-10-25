package residenciageriatrica.dtos;

public class HabitacionDTO {

    private final String codHabitacion;
    private final String estado;
    private final boolean activo;

    public HabitacionDTO(String codHabitacion, String estado, boolean activo) {
        this.codHabitacion = codHabitacion;
        this.estado = estado;
        this.activo = activo;
    }

    public String getCodHabitacion() {
        return codHabitacion;
    }

    public String getEstado() {
        return estado;
    }

    public boolean isActivo() {
        return activo;
    }
}
