package residenciageriatrica.controllers;

import java.io.IOException;
import java.util.List;
import residenciageriatrica.dtos.HabitacionDTO;
import residenciageriatrica.services.HabitacionService;

public class HabitacionController {

    private final HabitacionService habitacionService;

    public HabitacionController() {
        this.habitacionService = new HabitacionService();
    }

    public List<HabitacionDTO> listarHabitaciones() {
        return habitacionService.listarHabitaciones();
    }

    public void agregarHabitacion() throws IOException {
        habitacionService.agregarHabitacion();
    }

    public HabitacionDTO buscarPorCodigo(String codigo) {
        return habitacionService.buscarPorCodigo(codigo);
    }

    public void eliminarHabitacion(String codigo) throws IOException {
        habitacionService.eliminarHabitacion(codigo);
    }
}