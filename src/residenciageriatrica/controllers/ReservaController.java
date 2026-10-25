package residenciageriatrica.controllers;

import java.io.IOException;
import java.util.List;
import residenciageriatrica.dtos.ReservaDTO;
import residenciageriatrica.services.ReservaService;

public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController() {
        this.reservaService = new ReservaService();
    }

    public void crearReserva(String idResidente, String codHabitacion)
            throws IOException {
        reservaService.crearReserva(idResidente, codHabitacion);
    }

    public void eliminarReserva(String codigo) throws IOException {
        reservaService.eliminarReserva(codigo);
    }

    public List<ReservaDTO> listarReservas() {
        return reservaService.listarReservas();
    }

    public ReservaDTO buscarReservaPorId(String id) {
        return reservaService.buscarReservaPorId(id);
    }

    public ReservaDTO buscarReservaPorResidente(String idResidente) {
        return reservaService.buscarReservaPorResidente(idResidente);
    }

    public ReservaDTO buscarReservaPorHabitacion(String codHabitacion) {
        return reservaService.buscarReservaPorHabitacion(codHabitacion);
    }
}