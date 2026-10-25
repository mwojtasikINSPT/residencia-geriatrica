package residenciageriatrica.daos;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import residenciageriatrica.dtos.AsignacionDTO;
import residenciageriatrica.exceptions.EntidadDuplicadaException;
import residenciageriatrica.exceptions.EntidadNoEncontradaException;
import residenciageriatrica.interfaces.ICrud;

public class AsignacionDAO implements ICrud<AsignacionDTO, String> {

    private static final String FILE_PATH = "asignaciones.txt";

    public AsignacionDAO() {
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
    public List<AsignacionDTO> listar() {
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
                                    "Registro de asignación inválido en "
                                    + FILE_PATH + ": " + linea
                            );
                        }

                        return new AsignacionDTO(
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
    public void guardarAll(List<AsignacionDTO> lista) throws IOException {
        List<String> lineas = lista.stream()
                .map(a -> String.join(",",
                a.getId(),
                a.getIdEnfermero(),
                a.getCodHabitacion(),
                String.valueOf(a.isActivo())
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
    public void agregar(AsignacionDTO nueva) throws IOException {
        List<AsignacionDTO> asignaciones = listar();

        boolean idDuplicado = asignaciones.stream()
                .anyMatch(a -> a.getId().equals(nueva.getId()));

        if (idDuplicado) {
            throw new EntidadDuplicadaException(
                    "Ya existe una asignación con ese ID"
            );
        }

        boolean habitacionAsignada = asignaciones.stream()
                .anyMatch(a -> a.isActivo()
                && a.getCodHabitacion().equals(nueva.getCodHabitacion()));

        if (habitacionAsignada) {
            throw new EntidadDuplicadaException(
                    "La habitación ya tiene un enfermero asignado"
            );
        }

        asignaciones.add(nueva);
        guardarAll(asignaciones);
    }

    @Override
    public AsignacionDTO buscarPorId(String id) {
        return listar().stream()
                .filter(a -> a.getId().equals(id) && a.isActivo())
                .findFirst()
                .orElse(null);
    }

    public AsignacionDTO buscarPorHabitacion(String codHabitacion) {
        return listar().stream()
                .filter(a -> a.getCodHabitacion().equals(codHabitacion)
                && a.isActivo())
                .findFirst()
                .orElse(null);
    }

    public List<AsignacionDTO> buscarPorEnfermero(String idEnfermero) {
        return listar().stream()
                .filter(a -> a.getIdEnfermero().equals(idEnfermero)
                && a.isActivo())
                .collect(Collectors.toList());
    }

    @Override
    public void eliminar(String codigo) throws IOException {
        List<AsignacionDTO> asignaciones = listar();
        boolean encontrado = false;

        for (int i = 0; i < asignaciones.size(); i++) {
            AsignacionDTO asignacion = asignaciones.get(i);

            if (asignacion.isActivo()
                    && (asignacion.getId().equals(codigo)
                    || asignacion.getIdEnfermero().equals(codigo)
                    || asignacion.getCodHabitacion().equals(codigo))) {

                asignaciones.set(i, new AsignacionDTO(
                        asignacion.getId(),
                        asignacion.getIdEnfermero(),
                        asignacion.getCodHabitacion(),
                        false
                ));

                encontrado = true;
            }
        }

        if (!encontrado) {
            throw new EntidadNoEncontradaException(
                    "No existen asignaciones activas asociadas al código ingresado"
            );
        }

        guardarAll(asignaciones);
    }
}
