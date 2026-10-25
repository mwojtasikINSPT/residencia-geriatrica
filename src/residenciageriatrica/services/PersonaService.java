package residenciageriatrica.services;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import residenciageriatrica.daos.PersonaDAO;
import residenciageriatrica.daos.ReservaDAO;
import residenciageriatrica.daos.AsignacionDAO;
import residenciageriatrica.dtos.PersonaDTO;
import residenciageriatrica.models.Persona;
import residenciageriatrica.models.Residente;
import residenciageriatrica.models.Enfermero;
import residenciageriatrica.utils.GeneradorCodigo;
import residenciageriatrica.utils.Validaciones;
import residenciageriatrica.exceptions.EntidadNoEncontradaException;
import residenciageriatrica.exceptions.OperacionNoPermitidaException;

public class PersonaService {

    private final PersonaDAO personaDAO;
    private final String prefijo;

    public PersonaService(String ruta) {
        String archivo = ruta.replace("\\", "/");
        archivo = archivo.substring(archivo.lastIndexOf("/") + 1);

        if (archivo.equalsIgnoreCase("residentes.txt")) {
            this.prefijo = "RES";
        } else if (archivo.equalsIgnoreCase("enfermeros.txt")) {
            this.prefijo = "ENF";
        } else {
            throw new IllegalArgumentException(
                    "Archivo de personas no válido"
            );
        }

        this.personaDAO = new PersonaDAO(ruta);
    }

    private Persona crearModelo(String id, String dni, String nombre,
            String apellido, boolean activo) {

        Persona persona;

        if (prefijo.equals("RES")) {
            persona = new Residente(id, dni, nombre, apellido);
        } else {
            persona = new Enfermero(id, dni, nombre, apellido);
        }

        persona.setActivo(activo);
        return persona;
    }

    private PersonaDTO convertirADTO(Persona persona) {
        return new PersonaDTO(
                persona.getId(),
                persona.getDni(),
                persona.getNombre(),
                persona.getApellido(),
                persona.isActivo()
        );
    }

    public List<PersonaDTO> listarPersonas() {
        return personaDAO.listar().stream()
                .filter(PersonaDTO::isActivo)
                .collect(Collectors.toList());
    }

    public String agregarPersona(String dni, String nombre, String apellido)
            throws IOException {

        dni = Validaciones.validarDni(dni);
        nombre = Validaciones.validarTextoObligatorio(nombre, "nombre");
        apellido = Validaciones.validarTextoObligatorio(apellido, "apellido");

        List<String> ids = personaDAO.listar().stream()
                .map(PersonaDTO::getId)
                .collect(Collectors.toList());

        String id = GeneradorCodigo.generarSiguienteId(ids, prefijo);

        Persona persona = crearModelo(
                id,
                dni,
                nombre,
                apellido,
                true
        );

        personaDAO.agregar(convertirADTO(persona));
        return id;
    }

    public PersonaDTO buscarPorId(String id) {
        return personaDAO.buscarPorId(
                Validaciones.validarCodigo(id, prefijo)
        );
    }

    public PersonaDTO buscarPorDni(String dni) {
        return personaDAO.buscarPorDni(
                Validaciones.validarDni(dni)
        );
    }

    public void eliminarPersona(String id) throws IOException {
        id = Validaciones.validarCodigo(id, prefijo);

        PersonaDTO datos = personaDAO.buscarPorId(id);

        if (datos == null) {
            throw new EntidadNoEncontradaException(
                    "La persona no existe o está inactiva"
            );
        }

        Persona persona = crearModelo(
                datos.getId(),
                datos.getDni(),
                datos.getNombre(),
                datos.getApellido(),
                datos.isActivo()
        );

        switch (persona.getTipoPersona()) {
            case "RESIDENTE" -> {
                ReservaDAO reservaDAO = new ReservaDAO();

                if (reservaDAO.buscarPorResidente(id) != null) {
                    ReservaService reservaService = new ReservaService();
                    reservaService.eliminarReserva(id);
                }
            }

            case "ENFERMERO" -> {
                AsignacionDAO asignacionDAO = new AsignacionDAO();

                if (!asignacionDAO.buscarPorEnfermero(id).isEmpty()) {
                    throw new OperacionNoPermitidaException(
                            "No se puede eliminar el enfermero porque tiene asignaciones activas"
                    );
                }
            }

            default ->
                throw new OperacionNoPermitidaException(
                        "Tipo de persona no reconocido"
                );
        }

        personaDAO.eliminar(id);
    }

    public void actualizarPersona(PersonaDTO actualizada) throws IOException {
        String id = Validaciones.validarCodigo(
                actualizada.getId(), prefijo
        );

        String dni = Validaciones.validarDni(actualizada.getDni());

        String nombre = Validaciones.validarTextoObligatorio(
                actualizada.getNombre(), "nombre"
        );

        String apellido = Validaciones.validarTextoObligatorio(
                actualizada.getApellido(), "apellido"
        );

        Persona persona = crearModelo(
                id,
                dni,
                nombre,
                apellido,
                true
        );

        personaDAO.actualizar(convertirADTO(persona));
    }
    
    public void reactivarPersona(String id) throws IOException {
    id = Validaciones.validarCodigo(id, prefijo);
    personaDAO.reactivar(id);
}
}
