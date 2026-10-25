package residenciageriatrica.dtos;

public class AsignacionDTO {

    private final String id;
    private final String codHabitacion;
    private final String idEnfermero;
    private final boolean activo;

    public AsignacionDTO(String id, String idEnfermero, String codHabitacion, boolean activo) {
        this.id = id;
        this.idEnfermero = idEnfermero;
        this.codHabitacion = codHabitacion;
        this.activo = activo;
    }

    public String getId() {
        return id;
    }

    public String getCodHabitacion() {
        return codHabitacion;
    }

    public String getIdEnfermero() {
        return idEnfermero;
    }

    public boolean isActivo() {
        return activo;
    }
}
