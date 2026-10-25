package residenciageriatrica.models;

public class Reserva {

    private final String id;
    private final String idResidente;
    private final String codHabitacion;
    private boolean activo;

    public Reserva(String id, String idResidente, String codHabitacion) {
        this.id = id;
        this.idResidente = idResidente;
        this.codHabitacion = codHabitacion;
        this.activo = true;
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

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Reserva{"
                + "id='" + id + '\''
                + ", idResidente='" + idResidente + '\''
                + ", codHabitacion='" + codHabitacion + '\''
                + ", activo=" + activo
                + '}';
    }
}