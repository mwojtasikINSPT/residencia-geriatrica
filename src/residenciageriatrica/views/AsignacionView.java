package residenciageriatrica.views;

import java.io.IOException;
import java.util.List;
import residenciageriatrica.controllers.AsignacionController;
import residenciageriatrica.dtos.AsignacionDTO;
import residenciageriatrica.utils.ConsolaUtils;

public class AsignacionView {

    private final AsignacionController controller;

    public AsignacionView() {
        this.controller = new AsignacionController();
    }

    public void mostrarMenu() {
        int opcion;

        do {
            ConsolaUtils.imprimirMenu(
                    "GESTIÓN DE ASIGNACIONES",
                    "1. Asignar enfermero a habitación",
                    "2. Listar asignaciones",
                    "3. Buscar por ID de asignación",
                    "4. Buscar por código de habitación",
                    "5. Buscar por ID de enfermero",
                    "6. Cancelar asignación",
                    "7. Reasignar habitación",
                    "0. Volver"
            );

            opcion = ConsolaUtils.leerEntero("Seleccione una opción");

            try {
                switch (opcion) {
                    case 1:
                        crear();
                        break;
                    case 2:
                        listar();
                        break;
                    case 3:
                        buscarPorId();
                        break;
                    case 4:
                        buscarPorHabitacion();
                        break;
                    case 5:
                        buscarPorEnfermero();
                        break;
                    case 6:
                        cancelar();
                        break;
                    case 7:
                        reasignar();
                        break;
                    case 0:
                        break;
                    default:
                        ConsolaUtils.imprimirError("Opción inválida.");
                }
            } catch (IOException | RuntimeException e) {
                ConsolaUtils.imprimirError(e.getMessage());
            }
            if (opcion != 0) {
                ConsolaUtils.pausar();
            }
        } while (opcion != 0);
    }

    private void crear() throws IOException {
        String idEnfermero = ConsolaUtils.leerTexto("ID del enfermero");
        String codHabitacion = ConsolaUtils.leerTexto("Código de habitación");

        String id = controller.crearAsignacion(idEnfermero, codHabitacion);
        ConsolaUtils.imprimirRegistroCreado("Asignación", id);
    }

    private void listar() {
        List<AsignacionDTO> asignaciones = controller.listarAsignaciones();

        if (asignaciones.isEmpty()) {
            ConsolaUtils.imprimirMensaje("No hay asignaciones activas.");
            return;
        }

        for (AsignacionDTO asignacion : asignaciones) {
            mostrarAsignacion(asignacion);
        }
    }

    private void buscarPorId() {
        String id = ConsolaUtils.leerTexto("ID de asignación");
        mostrarAsignacion(controller.buscarAsignacionPorId(id));
    }

    private void buscarPorHabitacion() {
        String codigo = ConsolaUtils.leerTexto("Código de habitación");
        mostrarAsignacion(controller.buscarAsignacionPorHabitacion(codigo));
    }

    private void buscarPorEnfermero() {
        String id = ConsolaUtils.leerTexto("ID del enfermero");

        List<AsignacionDTO> asignaciones
                = controller.buscarAsignacionesPorEnfermero(id);

        if (asignaciones.isEmpty()) {
            ConsolaUtils.imprimirMensaje(
                    "El enfermero no tiene asignaciones activas."
            );
            return;
        }

        for (AsignacionDTO asignacion : asignaciones) {
            mostrarAsignacion(asignacion);
        }
    }

    private void cancelar() throws IOException {
        String codigo = ConsolaUtils.leerTexto(
                "Ingrese ID de asignación, enfermero o habitación"
        );

        String valor = codigo.trim().toUpperCase();

        if (valor.startsWith("ASI")) {
            AsignacionDTO asignacion = controller.buscarAsignacionPorId(valor);

            if (asignacion == null) {
                ConsolaUtils.imprimirError("Asignación no encontrada.");
                return;
            }

            mostrarAsignacion(asignacion);

        } else if (valor.startsWith("HAB")) {
            AsignacionDTO asignacion
                    = controller.buscarAsignacionPorHabitacion(valor);

            if (asignacion == null) {
                ConsolaUtils.imprimirError("Asignación no encontrada.");
                return;
            }

            mostrarAsignacion(asignacion);

        } else if (valor.startsWith("ENF")) {
            List<AsignacionDTO> asignaciones
                    = controller.buscarAsignacionesPorEnfermero(valor);

            if (asignaciones.isEmpty()) {
                ConsolaUtils.imprimirError(
                        "El enfermero no tiene asignaciones activas."
                );
                return;
            }

            for (AsignacionDTO asignacion : asignaciones) {
                mostrarAsignacion(asignacion);
            }

            ConsolaUtils.imprimirMensaje(
                    "Se cancelarán todas las asignaciones de este enfermero."
            );

        } else {
            ConsolaUtils.imprimirError("Código inválido.");
            return;
        }

        if (ConsolaUtils.confirmar("¿Confirmar cancelación?")) {
            controller.eliminarAsignacion(valor);
            ConsolaUtils.imprimirMensaje(
                    "Asignación o asignaciones canceladas correctamente."
            );
        }
    }

    private void mostrarAsignacion(AsignacionDTO asignacion) {
        if (asignacion == null) {
            ConsolaUtils.imprimirMensaje("Asignación no encontrada.");
            return;
        }

        ConsolaUtils.imprimirMensaje(
                "ID: " + asignacion.getId()
                + " | Enfermero: " + asignacion.getIdEnfermero()
                + " | Habitación: " + asignacion.getCodHabitacion()
        );
    }

    private void reasignar() throws IOException {
        String codHabitacion = ConsolaUtils.leerTexto("Código de habitación");
        String idEnfermero = ConsolaUtils.leerTexto("ID del nuevo enfermero");

        String id = controller.reasignarHabitacion(codHabitacion, idEnfermero);

        ConsolaUtils.imprimirMensaje(
                "Habitación reasignada correctamente. Nueva asignación: " + id
        );
    }
}
