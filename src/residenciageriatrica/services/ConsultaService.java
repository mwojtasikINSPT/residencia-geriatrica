package residenciageriatrica.services;

import java.util.ArrayList;
import java.util.List;
import residenciageriatrica.dtos.AsignacionDTO;
import residenciageriatrica.dtos.HabitacionDTO;
import residenciageriatrica.dtos.PersonaDTO;
import residenciageriatrica.dtos.ReservaDTO;
import residenciageriatrica.exceptions.EntidadNoEncontradaException;
import residenciageriatrica.utils.Validaciones;

public class ConsultaService {

    private final PersonaService residenteService;
    private final PersonaService enfermeroService;
    private final HabitacionService habitacionService;
    private final ReservaService reservaService;
    private final AsignacionService asignacionService;

    public ConsultaService() {
        this.residenteService = new PersonaService("residentes.txt");
        this.enfermeroService = new PersonaService("enfermeros.txt");
        this.habitacionService = new HabitacionService();
        this.reservaService = new ReservaService();
        this.asignacionService = new AsignacionService();
    }

    public HabitacionDTO obtenerHabitacionDeResidente(String idResidente) {
        idResidente = Validaciones.validarCodigo(idResidente, "RES");

        PersonaDTO residente = residenteService.buscarPorId(idResidente);

        if (residente == null) {
            throw new EntidadNoEncontradaException(
                    "El residente no existe o está inactivo"
            );
        }

        ReservaDTO reserva = reservaService.buscarReservaPorResidente(idResidente);

        if (reserva == null) {
            throw new EntidadNoEncontradaException(
                    "El residente no tiene una habitación reservada"
            );
        }

        return habitacionService.buscarPorCodigo(reserva.getCodHabitacion());
    }

    public List<PersonaDTO> obtenerResidentesPorEnfermero(String idEnfermero) {
        idEnfermero = Validaciones.validarCodigo(idEnfermero, "ENF");

        PersonaDTO enfermero = enfermeroService.buscarPorId(idEnfermero);

        if (enfermero == null) {
            throw new EntidadNoEncontradaException(
                    "El enfermero no existe o está inactivo"
            );
        }

        List<PersonaDTO> residentes = new ArrayList<>();

        List<AsignacionDTO> asignaciones =
                asignacionService.buscarAsignacionesPorEnfermero(idEnfermero);

        for (AsignacionDTO asignacion : asignaciones) {

            ReservaDTO reserva = reservaService.buscarReservaPorHabitacion(
                    asignacion.getCodHabitacion()
            );

            if (reserva != null) {
                PersonaDTO residente = residenteService.buscarPorId(
                        reserva.getIdResidente()
                );

                if (residente != null) {
                    residentes.add(residente);
                }
            }
        }

        return residentes;
    }

    public PersonaDTO obtenerEnfermeroDeResidente(String idResidente) {
        idResidente = Validaciones.validarCodigo(idResidente, "RES");

        PersonaDTO residente = residenteService.buscarPorId(idResidente);

        if (residente == null) {
            throw new EntidadNoEncontradaException(
                    "El residente no existe o está inactivo"
            );
        }

        ReservaDTO reserva = reservaService.buscarReservaPorResidente(idResidente);

        if (reserva == null) {
            throw new EntidadNoEncontradaException(
                    "El residente no tiene una habitación reservada"
            );
        }

        AsignacionDTO asignacion =
                asignacionService.buscarAsignacionPorHabitacion(
                        reserva.getCodHabitacion()
                );

        if (asignacion == null) {
            throw new EntidadNoEncontradaException(
                    "La habitación del residente no tiene enfermero asignado"
            );
        }

        return enfermeroService.buscarPorId(asignacion.getIdEnfermero());
    }
}