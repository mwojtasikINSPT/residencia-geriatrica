package residenciageriatrica.models;

public class Asignacion {

    private final String id;
    private final String idEnfermero;
    private final String codHabitacion;
    private boolean activo;

    public Asignacion(String id, String idEnfermero, String codHabitacion) {
        this.id = id;
        this.idEnfermero = idEnfermero;
        this.codHabitacion = codHabitacion;
        this.activo = true;
    }

    public String getId() {
        return id;
    }

    public String getIdEnfermero() {
        return idEnfermero;
    }

    public String getCodHabitacion() {
        return codHabitacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Asignacion{"
                + "id='" + id + '\''
                + ", idEnfermero='" + idEnfermero + '\''
                + ", codHabitacion='" + codHabitacion + '\''
                + ", activo=" + activo
                + '}';
    }
}
