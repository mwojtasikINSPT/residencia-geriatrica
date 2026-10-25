package residenciageriatrica.services;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import residenciageriatrica.daos.HabitacionDAO;
import residenciageriatrica.daos.ReservaDAO;
import residenciageriatrica.daos.AsignacionDAO;
import residenciageriatrica.dtos.HabitacionDTO;
import residenciageriatrica.exceptions.OperacionNoPermitidaException;
import residenciageriatrica.models.Estado;
import residenciageriatrica.models.Habitacion;
import residenciageriatrica.utils.GeneradorCodigo;
import residenciageriatrica.utils.Validaciones;

public class HabitacionService {

    private final HabitacionDAO habitacionDAO;
    private final ReservaDAO reservaDAO;
    private final AsignacionDAO asignacionDAO;

    public HabitacionService() {
        this.habitacionDAO = new HabitacionDAO();
        this.reservaDAO = new ReservaDAO();
        this.asignacionDAO = new AsignacionDAO();
    }

    public List<HabitacionDTO> listarHabitaciones() {
        return habitacionDAO.listar().stream()
                .filter(HabitacionDTO::isActivo)
                .collect(Collectors.toList());
    }

    public String agregarHabitacion() throws IOException {
        List<String> ids = habitacionDAO.listar().stream()
                .map(HabitacionDTO::getCodHabitacion)
                .collect(Collectors.toList());

        String codigo = GeneradorCodigo.generarSiguienteId(ids, "HAB");

        Habitacion habitacion = new Habitacion(codigo, Estado.LIBRE);

        HabitacionDTO nueva = new HabitacionDTO(
                habitacion.getCodHabitacion(),
                habitacion.getEstado().name(),
                habitacion.isActivo()
        );

        habitacionDAO.agregar(nueva);
        return codigo;
    }

    public HabitacionDTO buscarPorCodigo(String codigo) {
        codigo = Validaciones.validarCodigo(codigo, "HAB");
        return habitacionDAO.buscarPorId(codigo);
    }

    public void eliminarHabitacion(String codigo) throws IOException {
        codigo = Validaciones.validarCodigo(codigo, "HAB");

        if (habitacionDAO.buscarPorId(codigo) == null) {
            throw new OperacionNoPermitidaException(
                    "La habitación no existe o está inactiva"
            );
        }

        if (reservaDAO.buscarPorHabitacion(codigo) != null) {
            throw new OperacionNoPermitidaException(
                    "No se puede eliminar una habitación con una reserva activa"
            );
        }

        if (asignacionDAO.buscarPorHabitacion(codigo) != null) {
            throw new OperacionNoPermitidaException(
                    "No se puede eliminar una habitación con una asignación activa"
            );
        }

        habitacionDAO.eliminar(codigo);
    }
}
