package residenciageriatrica.controllers;

import java.util.List;
import residenciageriatrica.dtos.HabitacionDTO;
import residenciageriatrica.dtos.PersonaDTO;
import residenciageriatrica.services.ConsultaService;

public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController() {
        this.consultaService = new ConsultaService();
    }

    public HabitacionDTO obtenerHabitacionDeResidente(String idResidente) {
        return consultaService.obtenerHabitacionDeResidente(idResidente);
    }

    public List<PersonaDTO> obtenerResidentesPorEnfermero(String idEnfermero) {
        return consultaService.obtenerResidentesPorEnfermero(idEnfermero);
    }

    public PersonaDTO obtenerEnfermeroDeResidente(String idResidente) {
        return consultaService.obtenerEnfermeroDeResidente(idResidente);
    }
}