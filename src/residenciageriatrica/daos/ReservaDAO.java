package residenciageriatrica.daos;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import residenciageriatrica.dtos.ReservaDTO;
import residenciageriatrica.exceptions.EntidadDuplicadaException;
import residenciageriatrica.exceptions.EntidadNoEncontradaException;
import residenciageriatrica.exceptions.OperacionNoPermitidaException;
import residenciageriatrica.interfaces.ICrud;

public class ReservaDAO implements ICrud<ReservaDTO, String> {

    private static final String FILE_PATH = "reservas.txt";

    public ReservaDAO() {
        try {
            if (!Files.exists(Paths.get(FILE_PATH))) {
                Files.createFile(Paths.get(FILE_PATH));
            }
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Error al crear el archivo: " + FILE_PATH, e
            );
        }
    }

    @Override
    public List<ReservaDTO> listar() {
        try {
            return Files.readAllLines(Paths.get(FILE_PATH))
                    .stream()
                    .filter(linea -> !linea.trim().isEmpty())
                    .map(linea -> {
                        String[] datos = linea.split(",", -1);

                        if (datos.length != 4
                                || datos[0].isBlank()
                                || datos[1].isBlank()
                                || datos[2].isBlank()
                                || (!datos[3].trim().equalsIgnoreCase("true")
                                && !datos[3].trim().equalsIgnoreCase("false"))) {
                            throw new IllegalArgumentException(
                                    "Registro de reserva inválido en "
                                    + FILE_PATH + ": " + linea
                            );
                        }

                        return new ReservaDTO(
                                datos[0].trim(),
                                datos[1].trim(),
                                datos[2].trim(),
                                Boolean.parseBoolean(datos[3].trim())
                        );
                    })
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Error al leer el archivo: " + FILE_PATH, e
            );
        }
    }

    @Override
    public void guardarAll(List<ReservaDTO> lista) throws IOException {
        List<String> lineas = lista.stream()
                .map(reserva -> String.join(",",
                reserva.getId(),
                reserva.getIdResidente(),
                reserva.getCodHabitacion(),
                String.valueOf(reserva.isActivo())
        ))
                .collect(Collectors.toList());

        try {
            Files.write(Paths.get(FILE_PATH), lineas);
        } catch (IOException e) {
            throw new IOException(
                    "Error al guardar el archivo: " + FILE_PATH, e
            );
        }
    }

    public void agregar(ReservaDTO nuevaReserva) throws IOException {
        List<ReservaDTO> reservas = listar();

        boolean idDuplicado = reservas.stream()
                .anyMatch(r -> r.getId().equals(nuevaReserva.getId()));

        if (idDuplicado) {
            throw new EntidadDuplicadaException(
                    "Ya existe una reserva con ese ID"
            );
        }

        boolean residenteYaTieneHabitacion = reservas.stream()
                .anyMatch(r -> r.isActivo()
                && r.getIdResidente().equals(nuevaReserva.getIdResidente()));

        if (residenteYaTieneHabitacion) {
            throw new EntidadDuplicadaException(
                    "El residente ya tiene una habitación reservada"
            );
        }

        boolean habitacionYaReservada = reservas.stream()
                .anyMatch(r -> r.isActivo()
                && r.getCodHabitacion().equals(nuevaReserva.getCodHabitacion()));

        if (habitacionYaReservada) {
            throw new EntidadDuplicadaException(
                    "La habitación ya está reservada"
            );
        }

        reservas.add(nuevaReserva);
        guardarAll(reservas);
    }

    public ReservaDTO buscarPorId(String id) {
        return listar().stream()
                .filter(r -> r.getId().equals(id) && r.isActivo())
                .findFirst()
                .orElse(null);
    }

    public ReservaDTO buscarPorResidente(String idResidente) {
        return listar().stream()
                .filter(r -> r.getIdResidente().equals(idResidente)
                && r.isActivo())
                .findFirst()
                .orElse(null);
    }

    public ReservaDTO buscarPorHabitacion(String codHabitacion) {
        return listar().stream()
                .filter(r -> r.getCodHabitacion().equals(codHabitacion)
                && r.isActivo())
                .findFirst()
                .orElse(null);
    }

    public void eliminar(String codigo) throws IOException {
        List<ReservaDTO> reservas = listar();

        for (int i = 0; i < reservas.size(); i++) {
            ReservaDTO reserva = reservas.get(i);

            if (reserva.isActivo()
                    && (reserva.getId().equals(codigo)
                    || reserva.getIdResidente().equals(codigo)
                    || reserva.getCodHabitacion().equals(codigo))) {

                reservas.set(i, new ReservaDTO(
                        reserva.getId(),
                        reserva.getIdResidente(),
                        reserva.getCodHabitacion(),
                        false
                ));

                guardarAll(reservas);
                return;
            }
        }

        throw new EntidadNoEncontradaException(
                "No existe una reserva activa asociada al código ingresado"
        );
    }

    public void reactivar(String id) throws IOException {
        List<ReservaDTO> reservas = listar();

        for (int i = 0; i < reservas.size(); i++) {
            ReservaDTO reserva = reservas.get(i);

            if (reserva.getId().equals(id) && !reserva.isActivo()) {

                boolean existeConflicto = reservas.stream()
                        .anyMatch(r -> r.isActivo()
                        && (r.getIdResidente().equals(reserva.getIdResidente())
                        || r.getCodHabitacion().equals(reserva.getCodHabitacion())));

                if (existeConflicto) {
                    throw new OperacionNoPermitidaException(
                            "No se puede reactivar la reserva porque existe un conflicto"
                    );
                }

                reservas.set(i, new ReservaDTO(
                        reserva.getId(),
                        reserva.getIdResidente(),
                        reserva.getCodHabitacion(),
                        true
                ));

                guardarAll(reservas);
                return;
            }
        }

        throw new EntidadNoEncontradaException(
                "No existe una reserva inactiva con ese ID"
        );
    }

}
