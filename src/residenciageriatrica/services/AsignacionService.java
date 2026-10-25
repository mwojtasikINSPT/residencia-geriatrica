package residenciageriatrica.services;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import residenciageriatrica.daos.AsignacionDAO;
import residenciageriatrica.daos.HabitacionDAO;
import residenciageriatrica.daos.PersonaDAO;
import residenciageriatrica.dtos.AsignacionDTO;
import residenciageriatrica.exceptions.EntidadNoEncontradaException;
import residenciageriatrica.exceptions.OperacionNoPermitidaException;
import residenciageriatrica.models.Asignacion;
import residenciageriatrica.utils.GeneradorCodigo;
import residenciageriatrica.utils.Validaciones;

public class AsignacionService {

    private final AsignacionDAO asignacionDAO;
    private final HabitacionDAO habitacionDAO;
    private final PersonaDAO enfermeroDAO;

    public AsignacionService() {
        this.asignacionDAO = new AsignacionDAO();
        this.habitacionDAO = new HabitacionDAO();
        this.enfermeroDAO = new PersonaDAO("enfermeros.txt");
    }

    public void crearAsignacion(String idEnfermero, String codHabitacion) throws IOException {
        idEnfermero = Validaciones.validarCodigo(idEnfermero, "ENF");
        codHabitacion = Validaciones.validarCodigo(codHabitacion, "HAB");

        if (enfermeroDAO.buscarPorId(idEnfermero) == null) {
            throw new EntidadNoEncontradaException(
                    "El enfermero no existe o está inactivo"
            );
        }

        if (habitacionDAO.buscarPorId(codHabitacion) == null) {
            throw new EntidadNoEncontradaException(
                    "La habitación no existe o está inactiva"
            );
        }

        if (asignacionDAO.buscarPorHabitacion(codHabitacion) != null) {
            throw new OperacionNoPermitidaException(
                    "La habitación ya tiene un enfermero asignado"
            );
        }

        List<String> ids = asignacionDAO.listar().stream()
                .map(AsignacionDTO::getId)
                .collect(Collectors.toList());

        String id = GeneradorCodigo.generarSiguienteId(ids, "ASI");

        Asignacion asignacion = new Asignacion(id, idEnfermero, codHabitacion);

        AsignacionDTO nueva = new AsignacionDTO(
                asignacion.getId(),
                asignacion.getIdEnfermero(),
                asignacion.getCodHabitacion(),
                asignacion.isActivo()
        );

        asignacionDAO.agregar(nueva);
    }

    public void eliminarAsignacion(String codigo) throws IOException {
        codigo = Validaciones.normalizarTexto(codigo);

        if (!Validaciones.esCodigoValido(codigo, "ASI")
                && !Validaciones.esCodigoValido(codigo, "ENF")
                && !Validaciones.esCodigoValido(codigo, "HAB")) {
            throw new IllegalArgumentException(
                    "El código debe comenzar con ASI, ENF o HAB"
            );
        }

        asignacionDAO.eliminar(codigo);
    }

    public List<AsignacionDTO> listarAsignaciones() {
        return asignacionDAO.listar().stream()
                .filter(AsignacionDTO::isActivo)
                .collect(Collectors.toList());
    }

    public AsignacionDTO buscarAsignacionPorId(String id) {
        return asignacionDAO.buscarPorId(
                Validaciones.validarCodigo(id, "ASI")
        );
    }

    public AsignacionDTO buscarAsignacionPorHabitacion(String codHabitacion) {
        return asignacionDAO.buscarPorHabitacion(
                Validaciones.validarCodigo(codHabitacion, "HAB")
        );
    }

    public List<AsignacionDTO> buscarAsignacionesPorEnfermero(String idEnfermero) {
        return asignacionDAO.buscarPorEnfermero(
                Validaciones.validarCodigo(idEnfermero, "ENF")
        );
    }
}
