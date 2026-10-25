package residenciageriatrica.daos;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import residenciageriatrica.dtos.PersonaDTO;
import residenciageriatrica.exceptions.EntidadDuplicadaException;
import residenciageriatrica.exceptions.EntidadNoEncontradaException;

public class PersonaDAO {

    private final String ruta;

    public PersonaDAO(String ruta) {
        this.ruta = ruta;

        try {
            if (!Files.exists(Paths.get(ruta))) {
                Files.createFile(Paths.get(ruta));
            }
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Error al crear el archivo: " + ruta, e
            );
        }
    }

    public List<PersonaDTO> leerTodos() {
        try {
            return Files.readAllLines(Paths.get(ruta))
                    .stream()
                    .filter(linea -> !linea.trim().isEmpty())
                    .map(linea -> {
                        String[] datos = linea.split(",", -1);

                        if (datos.length != 5
                                || datos[0].isBlank()
                                || datos[1].isBlank()
                                || datos[2].isBlank()
                                || datos[3].isBlank()
                                || (!datos[4].trim().equalsIgnoreCase("true")
                                && !datos[4].trim().equalsIgnoreCase("false"))) {
                            throw new IllegalArgumentException(
                                    "Registro de persona inválido en " + ruta + ": " + linea
                            );
                        }

                        return new PersonaDTO(
                                datos[0].trim(),
                                datos[1].trim(),
                                datos[2].trim(),
                                datos[3].trim(),
                                Boolean.parseBoolean(datos[4].trim())
                        );
                    })
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Error al leer el archivo: " + ruta, e
            );
        }
    }

    public void guardarTodos(List<PersonaDTO> lista) throws IOException {
        List<String> lineas = lista.stream()
                .map(p -> String.join(",",
                p.getId(),
                p.getDni(),
                p.getNombre(),
                p.getApellido(),
                String.valueOf(p.isActivo())
        ))
                .collect(Collectors.toList());

        try {
            Files.write(Paths.get(ruta), lineas);
        } catch (IOException e) {
            throw new IOException("Error al guardar el archivo: " + ruta, e);
        }
    }

    public void agregar(PersonaDTO nueva) throws IOException {
        List<PersonaDTO> personas = leerTodos();

        boolean existe = personas.stream()
                .anyMatch(p -> p.getId().equals(nueva.getId())
                || p.getDni().equals(nueva.getDni()));

        if (existe) {
            throw new EntidadDuplicadaException(
                    "Ya existe una persona con ese ID o DNI"
            );
        }

        personas.add(nueva);
        guardarTodos(personas);
    }

    public PersonaDTO buscarPorId(String id) {
        return leerTodos()
                .stream()
                .filter(persona -> persona.getId().equals(id) && persona.isActivo())
                .findFirst()
                .orElse(null);
    }

    public PersonaDTO buscarPorDni(String dni) {
        return leerTodos()
                .stream()
                .filter(persona -> persona.getDni().equals(dni) && persona.isActivo())
                .findFirst()
                .orElse(null);
    }

    public void eliminar(String id) throws IOException {
        List<PersonaDTO> personas = leerTodos();

        for (int i = 0; i < personas.size(); i++) {
            PersonaDTO persona = personas.get(i);

            if (persona.getId().equals(id) && persona.isActivo()) {
                personas.set(i, new PersonaDTO(
                        persona.getId(),
                        persona.getDni(),
                        persona.getNombre(),
                        persona.getApellido(),
                        false
                ));

                guardarTodos(personas);
                return;
            }
        }

        throw new EntidadNoEncontradaException(
                "La persona no existe o ya está inactiva"
        );
    }

    public void actualizar(PersonaDTO actualizada) throws IOException {
        List<PersonaDTO> personas = leerTodos();

        for (int i = 0; i < personas.size(); i++) {
            PersonaDTO persona = personas.get(i);

            if (persona.getId().equals(actualizada.getId()) && persona.isActivo()) {

                boolean dniDuplicado = personas.stream()
                        .anyMatch(p -> p.getDni().equals(actualizada.getDni())
                        && !p.getId().equals(actualizada.getId()));

                if (dniDuplicado) {
                    throw new EntidadDuplicadaException(
                            "Ya existe otra persona con ese DNI"
                    );
                }

                personas.set(i, new PersonaDTO(
                        persona.getId(),
                        actualizada.getDni(),
                        actualizada.getNombre(),
                        actualizada.getApellido(),
                        persona.isActivo()
                ));

                guardarTodos(personas);
                return;
            }
        }

        throw new EntidadNoEncontradaException(
                "La persona no existe o está inactiva"
        );
    }
}
