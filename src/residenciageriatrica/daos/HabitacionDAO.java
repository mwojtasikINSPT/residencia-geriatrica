package residenciageriatrica.daos;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import residenciageriatrica.dtos.HabitacionDTO;
import residenciageriatrica.exceptions.EntidadDuplicadaException;
import residenciageriatrica.exceptions.EntidadNoEncontradaException;
import residenciageriatrica.interfaces.ICrud;
import residenciageriatrica.models.Estado;

public class HabitacionDAO implements ICrud<HabitacionDTO, String> {

    private static final String FILE_PATH = "habitaciones.txt";

    public HabitacionDAO() {
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
    public List<HabitacionDTO> listar() {
        try {
            return Files.readAllLines(Paths.get(FILE_PATH))
                    .stream()
                    .filter(linea -> !linea.trim().isEmpty())
                    .map(linea -> {
                        String[] datos = linea.split(",", -1);

                        if (datos.length != 3
                                || datos[0].isBlank()
                                || datos[1].isBlank()
                                || (!datos[2].trim().equalsIgnoreCase("true")
                                && !datos[2].trim().equalsIgnoreCase("false"))) {
                            throw new IllegalArgumentException(
                                    "Registro de habitación inválido en "
                                    + FILE_PATH + ": " + linea
                            );
                        }

                        return new HabitacionDTO(
                                datos[0].trim(),
                                datos[1].trim(),
                                Boolean.parseBoolean(datos[2].trim())
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
    public void guardarAll(List<HabitacionDTO> lista) throws IOException {
        List<String> lineas = lista.stream()
                .map(h -> String.join(",",
                        h.getCodHabitacion(),
                        h.getEstado(),
                        String.valueOf(h.isActivo())
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

    @Override
    public void agregar(HabitacionDTO nueva) throws IOException {
        List<HabitacionDTO> habitaciones = listar();

        boolean existe = habitaciones.stream()
                .anyMatch(h -> h.getCodHabitacion()
                        .equals(nueva.getCodHabitacion()));

        if (existe) {
            throw new EntidadDuplicadaException(
                    "La habitación ya existe"
            );
        }

        habitaciones.add(nueva);
        guardarAll(habitaciones);
    }

    @Override
    public HabitacionDTO buscarPorId(String codigo) {
        return listar().stream()
                .filter(h -> h.getCodHabitacion().equals(codigo)
                        && h.isActivo())
                .findFirst()
                .orElse(null);
    }

    public void actualizarEstado(String codigo, Estado nuevoEstado) throws IOException {
        List<HabitacionDTO> habitaciones = listar();

        for (int i = 0; i < habitaciones.size(); i++) {
            HabitacionDTO h = habitaciones.get(i);

            if (h.getCodHabitacion().equals(codigo) && h.isActivo()) {
                habitaciones.set(i,
                        new HabitacionDTO(
                                h.getCodHabitacion(),
                                nuevoEstado.name(),
                                h.isActivo()
                        )
                );

                guardarAll(habitaciones);
                return;
            }
        }

        throw new EntidadNoEncontradaException(
                "La habitación no existe o está inactiva"
        );
    }
    
    @Override
    public void eliminar(String codigo) throws IOException {
        List<HabitacionDTO> habitaciones = listar();

        for (int i = 0; i < habitaciones.size(); i++) {
            HabitacionDTO h = habitaciones.get(i);

            if (h.getCodHabitacion().equals(codigo) && h.isActivo()) {
                habitaciones.set(i,
                        new HabitacionDTO(
                                h.getCodHabitacion(),
                                h.getEstado(),
                                false
                        )
                );

                guardarAll(habitaciones);
                return;
            }
        }

        throw new EntidadNoEncontradaException(
                "La habitación no existe o ya está inactiva"
        );
    }
}