package residenciageriatrica.views;

import java.util.List;
import residenciageriatrica.controllers.ConsultaController;
import residenciageriatrica.dtos.HabitacionDTO;
import residenciageriatrica.dtos.PersonaDTO;
import residenciageriatrica.utils.ConsolaUtils;

public class ConsultaView {

    private final ConsultaController controller;

    public ConsultaView() {
        this.controller = new ConsultaController();
    }

    public void mostrarMenu() {
        int opcion;

        do {
            ConsolaUtils.imprimirMenu(
                    "CONSULTAS ESPECIALES",
                    "1. Consultar habitación de un residente",
                    "2. Consultar residentes atendidos por un enfermero",
                    "3. Consultar enfermero asignado a un residente",
                    "0. Volver"
            );

            opcion = ConsolaUtils.leerEntero("Seleccione una opción");

            try {
                switch (opcion) {
                    case 1:
                        consultarHabitacion();
                        break;
                    case 2:
                        consultarResidentes();
                        break;
                    case 3:
                        consultarEnfermero();
                        break;
                    case 0:
                        break;
                    default:
                        ConsolaUtils.imprimirError("Opción inválida.");
                }
            } catch (RuntimeException e) {
                ConsolaUtils.imprimirError(e.getMessage());
            }
            if (opcion != 0) {
                ConsolaUtils.pausar();
            }

        } while (opcion != 0);
    }

    private void consultarHabitacion() {
        String idResidente = ConsolaUtils.leerTexto("ID del residente");

        HabitacionDTO habitacion
                = controller.obtenerHabitacionDeResidente(idResidente);

        if (habitacion == null) {
            ConsolaUtils.imprimirMensaje("No se encontró la habitación.");
            return;
        }

        ConsolaUtils.imprimirMensaje(
                "Habitación: " + habitacion.getCodHabitacion()
                + " | Estado: " + habitacion.getEstado()
        );
    }

    private void consultarResidentes() {
        String idEnfermero = ConsolaUtils.leerTexto("ID del enfermero");

        List<PersonaDTO> residentes
                = controller.obtenerResidentesPorEnfermero(idEnfermero);

        if (residentes.isEmpty()) {
            ConsolaUtils.imprimirMensaje(
                    "El enfermero no tiene residentes a cargo."
            );
            return;
        }

        ConsolaUtils.imprimirTitulo("RESIDENTES ATENDIDOS");

        for (PersonaDTO residente : residentes) {
            mostrarPersona(residente);
        }
    }

    private void consultarEnfermero() {
        String idResidente = ConsolaUtils.leerTexto("ID del residente");

        PersonaDTO enfermero
                = controller.obtenerEnfermeroDeResidente(idResidente);

        if (enfermero == null) {
            ConsolaUtils.imprimirMensaje(
                    "No se encontró un enfermero asignado."
            );
            return;
        }

        ConsolaUtils.imprimirTitulo("ENFERMERO ASIGNADO");
        mostrarPersona(enfermero);
    }

    private void mostrarPersona(PersonaDTO persona) {
        ConsolaUtils.imprimirMensaje(
                "ID: " + persona.getId()
                + " | DNI: " + persona.getDni()
                + " | Nombre: " + persona.getNombre()
                + " " + persona.getApellido()
        );
    }
}
