package residenciageriatrica.views;

import java.io.IOException;
import java.util.List;
import residenciageriatrica.controllers.ReservaController;
import residenciageriatrica.dtos.ReservaDTO;
import residenciageriatrica.utils.ConsolaUtils;

public class ReservaView {

    private final ReservaController controller;

    public ReservaView() {
        this.controller = new ReservaController();
    }

    public void mostrarMenu() {
        int opcion;

        do {
            ConsolaUtils.imprimirMenu(
                    "GESTIÓN DE RESERVAS",
                    "1. Crear reserva",
                    "2. Listar reservas",
                    "3. Buscar por ID de reserva",
                    "4. Buscar por ID de residente",
                    "5. Buscar por código de habitación",
                    "6. Cancelar reserva",
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
                        buscarPorResidente();
                        break;
                    case 5:
                        buscarPorHabitacion();
                        break;
                    case 6:
                        cancelar();
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
        String idResidente = ConsolaUtils.leerTexto("ID del residente");
        String codHabitacion = ConsolaUtils.leerTexto("Código de habitación");

        controller.crearReserva(idResidente, codHabitacion);
        ConsolaUtils.imprimirMensaje("Reserva creada correctamente.");
    }

    private void listar() {
        List<ReservaDTO> reservas = controller.listarReservas();

        if (reservas.isEmpty()) {
            ConsolaUtils.imprimirMensaje("No hay reservas activas.");
            return;
        }

        for (ReservaDTO reserva : reservas) {
            mostrarReserva(reserva);
        }
    }

    private void buscarPorId() {
        String id = ConsolaUtils.leerTexto("ID de reserva");
        mostrarReserva(controller.buscarReservaPorId(id));
    }

    private void buscarPorResidente() {
        String id = ConsolaUtils.leerTexto("ID del residente");
        mostrarReserva(controller.buscarReservaPorResidente(id));
    }

    private void buscarPorHabitacion() {
        String codigo = ConsolaUtils.leerTexto("Código de habitación");
        mostrarReserva(controller.buscarReservaPorHabitacion(codigo));
    }

    private void cancelar() throws IOException {
        String codigo = ConsolaUtils.leerTexto(
                "Ingrese ID de reserva, residente o habitación"
        );

        ReservaDTO reserva;

        String valor = codigo.trim().toUpperCase();

        if (valor.startsWith("RSV")) {
            reserva = controller.buscarReservaPorId(valor);
        } else if (valor.startsWith("RES")) {
            reserva = controller.buscarReservaPorResidente(valor);
        } else if (valor.startsWith("HAB")) {
            reserva = controller.buscarReservaPorHabitacion(valor);
        } else {
            ConsolaUtils.imprimirError("Código inválido.");
            return;
        }

        if (reserva == null) {
            ConsolaUtils.imprimirError("Reserva no encontrada.");
            return;
        }

        mostrarReserva(reserva);

        if (ConsolaUtils.confirmar("¿Confirmar cancelación?")) {
            controller.eliminarReserva(reserva.getId());
            ConsolaUtils.imprimirMensaje("Reserva cancelada correctamente.");
        }
    }

    private void mostrarReserva(ReservaDTO reserva) {
        if (reserva == null) {
            ConsolaUtils.imprimirMensaje("Reserva no encontrada.");
            return;
        }

        ConsolaUtils.imprimirMensaje(
                "ID: " + reserva.getId()
                + " | Residente: " + reserva.getIdResidente()
                + " | Habitación: " + reserva.getCodHabitacion()
        );
    }
}
