package residenciageriatrica.views;

import java.io.IOException;
import java.util.List;
import residenciageriatrica.controllers.HabitacionController;
import residenciageriatrica.dtos.HabitacionDTO;
import residenciageriatrica.utils.ConsolaUtils;

public class HabitacionView {

    private final HabitacionController controller;

    public HabitacionView() {
        this.controller = new HabitacionController();
    }

    public void mostrarMenu() {
        int opcion;

        do {
            ConsolaUtils.imprimirMenu(
                    "GESTIÓN DE HABITACIONES",
                    "1. Agregar habitación",
                    "2. Listar habitaciones",
                    "3. Buscar habitación por código",
                    "4. Eliminar habitación",
                    "0. Volver"
            );

            opcion = ConsolaUtils.leerEntero("Seleccione una opción");

            try {
                switch (opcion) {
                    case 1:
                        agregar();
                        break;
                    case 2:
                        listar();
                        break;
                    case 3:
                        buscar();
                        break;
                    case 4:
                        eliminar();
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

    private void agregar() throws IOException {
        String codigo = controller.agregarHabitacion();
        ConsolaUtils.imprimirRegistroCreado("Habitación", codigo);
    }

    private void listar() {
        List<HabitacionDTO> habitaciones = controller.listarHabitaciones();

        if (habitaciones.isEmpty()) {
            ConsolaUtils.imprimirMensaje("No hay habitaciones activas.");
            return;
        }

        for (HabitacionDTO habitacion : habitaciones) {
            mostrarHabitacion(habitacion);
        }
    }

    private void buscar() {
        String codigo = ConsolaUtils.leerTexto("Código de habitación");
        mostrarHabitacion(controller.buscarPorCodigo(codigo));
    }

    private void eliminar() throws IOException {
        String codigo = ConsolaUtils.leerTexto("Código de habitación");

        HabitacionDTO habitacion = controller.buscarPorCodigo(codigo);

        if (habitacion == null) {
            ConsolaUtils.imprimirError("Habitación no encontrada.");
            return;
        }

        mostrarHabitacion(habitacion);

        if (ConsolaUtils.confirmar("¿Confirmar eliminación?")) {
            controller.eliminarHabitacion(codigo);
            ConsolaUtils.imprimirMensaje("Habitación eliminada correctamente.");
        }
    }

    private void mostrarHabitacion(HabitacionDTO habitacion) {
        if (habitacion == null) {
            ConsolaUtils.imprimirMensaje("Habitación no encontrada.");
            return;
        }

        ConsolaUtils.imprimirMensaje(
                "Código: " + habitacion.getCodHabitacion()
                + " | Estado: " + habitacion.getEstado()
        );
    }
}
