package residenciageriatrica.dtos;

public class ReservaDTO {

    private final String id;
    private final String idResidente;
    private final String codHabitacion;
    private final boolean activo;

    public ReservaDTO(String id, String idResidente, String codHabitacion, boolean activo) {
        this.id = id;
        this.idResidente = idResidente;
        this.codHabitacion = codHabitacion;
        this.activo = activo;
    }

    public String getId() {
        return id;
    }

    public String getIdResidente() {
        return idResidente;
    }

    public String getCodHabitacion() {
        return codHabitacion;
    }

    public boolean isActivo() {
        return activo;
    }
}