package residenciageriatrica.views;

import residenciageriatrica.utils.ConsolaUtils;

public class MenuPrincipalView {

    private final PersonaView residentes = new PersonaView("residentes.txt", "Residentes");
    private final PersonaView enfermeros = new PersonaView("enfermeros.txt", "Enfermeros");
    private final HabitacionView habitaciones = new HabitacionView();
    private final ReservaView reservas = new ReservaView();
    private final AsignacionView asignaciones = new AsignacionView();
    private final ConsultaView consultas = new ConsultaView();

    public void mostrarMenu() {
        int opcion;

        do {
            ConsolaUtils.imprimirMenu(
                    "GESTIÓN DE RESIDENCIA GERIÁTRICA",
                    "1. Gestionar residentes",
                    "2. Gestionar enfermeros",
                    "3. Gestionar habitaciones",
                    "4. Gestionar reservas",
                    "5. Gestionar asignaciones",
                    "6. Consultas especiales",
                    "0. Salir"
            );

            opcion = ConsolaUtils.leerEntero("Seleccione una opción");

            switch (opcion) {
                case 1:
                    residentes.mostrarMenu();
                    break;
                case 2:
                    enfermeros.mostrarMenu();
                    break;
                case 3:
                    habitaciones.mostrarMenu();
                    break;
                case 4:
                    reservas.mostrarMenu();
                    break;
                case 5:
                    asignaciones.mostrarMenu();
                    break;
                case 6:
                    consultas.mostrarMenu();
                    break;
                case 0:
                    ConsolaUtils.imprimirMensaje("Saliendo del sistema...");
                    break;
                default:
                    ConsolaUtils.imprimirError("Opción inválida.");
            }

        } while (opcion != 0);
    }
}
