package residenciageriatrica.controllers;

import java.io.IOException;
import java.util.List;
import residenciageriatrica.dtos.AsignacionDTO;
import residenciageriatrica.services.AsignacionService;

public class AsignacionController {

    private final AsignacionService asignacionService;

    public AsignacionController() {
        this.asignacionService = new AsignacionService();
    }

    public String crearAsignacion(String idEnfermero, String codHabitacion) throws IOException {
        return asignacionService.crearAsignacion(idEnfermero, codHabitacion);
    }

    public void eliminarAsignacion(String codigo) throws IOException {
        asignacionService.eliminarAsignacion(codigo);
    }

    public List<AsignacionDTO> listarAsignaciones() {
        return asignacionService.listarAsignaciones();
    }

    public AsignacionDTO buscarAsignacionPorId(String id) {
        return asignacionService.buscarAsignacionPorId(id);
    }

    public AsignacionDTO buscarAsignacionPorHabitacion(String codHabitacion) {
        return asignacionService.buscarAsignacionPorHabitacion(codHabitacion);
    }

    public List<AsignacionDTO> buscarAsignacionesPorEnfermero(String idEnfermero) {
        return asignacionService.buscarAsignacionesPorEnfermero(idEnfermero);
    }

    public String reasignarHabitacion(String codHabitacion, String idNuevoEnfermero)
            throws IOException {
        return asignacionService.reasignarHabitacion(codHabitacion, idNuevoEnfermero);
    }
}
