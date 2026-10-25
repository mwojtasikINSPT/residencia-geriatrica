package residenciageriatrica.services;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import residenciageriatrica.daos.ReservaDAO;
import residenciageriatrica.daos.HabitacionDAO;
import residenciageriatrica.daos.PersonaDAO;
import residenciageriatrica.dtos.ReservaDTO;
import residenciageriatrica.dtos.HabitacionDTO;
import residenciageriatrica.models.Estado;
import residenciageriatrica.exceptions.EntidadNoEncontradaException;
import residenciageriatrica.exceptions.OperacionNoPermitidaException;
import residenciageriatrica.models.Reserva;
import residenciageriatrica.utils.GeneradorCodigo;
import residenciageriatrica.utils.Validaciones;

public class ReservaService {

    private final ReservaDAO reservaDAO;
    private final HabitacionDAO habitacionDAO;
    private final PersonaDAO residenteDAO;

    public ReservaService() {
        this.reservaDAO = new ReservaDAO();
        this.habitacionDAO = new HabitacionDAO();
        this.residenteDAO = new PersonaDAO("residentes.txt");
    }

    public void crearReserva(String idResidente, String codHabitacion) throws IOException {
        idResidente = Validaciones.validarCodigo(idResidente, "RES");
        codHabitacion = Validaciones.validarCodigo(codHabitacion, "HAB");

        if (residenteDAO.buscarPorId(idResidente) == null) {
            throw new EntidadNoEncontradaException(
                    "El residente no existe o está inactivo"
            );
        }

        HabitacionDTO habitacion = habitacionDAO.buscarPorId(codHabitacion);

        if (habitacion == null) {
            throw new EntidadNoEncontradaException(
                    "La habitación no existe o está inactiva"
            );
        }

        if (!habitacion.getEstado().equals(Estado.LIBRE.name())) {
            throw new OperacionNoPermitidaException(
                    "La habitación no está disponible"
            );
        }

        if (reservaDAO.buscarPorResidente(idResidente) != null) {
            throw new OperacionNoPermitidaException(
                    "El residente ya tiene una reserva activa"
            );
        }

        if (reservaDAO.buscarPorHabitacion(codHabitacion) != null) {
            throw new OperacionNoPermitidaException(
                    "La habitación ya tiene una reserva activa"
            );
        }

        List<String> ids = reservaDAO.listar().stream()
                .map(ReservaDTO::getId)
                .collect(Collectors.toList());

        String id = GeneradorCodigo.generarSiguienteId(ids, "RSV");

        Reserva reserva = new Reserva(id, idResidente, codHabitacion);

        ReservaDTO nueva = new ReservaDTO(
                reserva.getId(),
                reserva.getIdResidente(),
                reserva.getCodHabitacion(),
                reserva.isActivo()
        );

        reservaDAO.agregar(nueva);

        try {
            habitacionDAO.actualizarEstado(codHabitacion, Estado.OCUPADA);
        } catch (IOException | RuntimeException e) {
            try {
                reservaDAO.eliminar(id);
            } catch (IOException | RuntimeException rollbackError) {
                e.addSuppressed(rollbackError);
            }

            throw e;
        }
    }

    public void eliminarReserva(String codigo) throws IOException {
        codigo = Validaciones.normalizarTexto(codigo);

        ReservaDTO reserva;

        if (Validaciones.esCodigoValido(codigo, "RSV")) {
            reserva = reservaDAO.buscarPorId(codigo);
        } else if (Validaciones.esCodigoValido(codigo, "RES")) {
            reserva = reservaDAO.buscarPorResidente(codigo);
        } else if (Validaciones.esCodigoValido(codigo, "HAB")) {
            reserva = reservaDAO.buscarPorHabitacion(codigo);
        } else {
            throw new IllegalArgumentException(
                    "El código debe comenzar con RSV, RES o HAB"
            );
        }

        if (reserva == null) {
            throw new EntidadNoEncontradaException(
                    "No existe una reserva activa asociada al código ingresado"
            );
        }

        reservaDAO.eliminar(reserva.getId());

        try {
            habitacionDAO.actualizarEstado(
                    reserva.getCodHabitacion(),
                    Estado.LIBRE
            );
        } catch (IOException | RuntimeException e) {
            try {
                reservaDAO.reactivar(reserva.getId());
            } catch (IOException | RuntimeException rollbackError) {
                e.addSuppressed(rollbackError);
            }

            throw e;
        }
    }

    public List<ReservaDTO> listarReservas() {
        return reservaDAO.listar().stream()
                .filter(ReservaDTO::isActivo)
                .collect(Collectors.toList());
    }

    public ReservaDTO buscarReservaPorId(String id) {
        return reservaDAO.buscarPorId(
                Validaciones.validarCodigo(id, "RSV")
        );
    }

    public ReservaDTO buscarReservaPorResidente(String idResidente) {
        return reservaDAO.buscarPorResidente(
                Validaciones.validarCodigo(idResidente, "RES")
        );
    }

    public ReservaDTO buscarReservaPorHabitacion(String codHabitacion) {
        return reservaDAO.buscarPorHabitacion(
                Validaciones.validarCodigo(codHabitacion, "HAB")
        );
    }
}
