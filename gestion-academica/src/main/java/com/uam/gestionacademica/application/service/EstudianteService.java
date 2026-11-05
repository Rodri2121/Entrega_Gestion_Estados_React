package com.uam.gestionacademica.application.service;

import com.uam.gestionacademica.application.exception.CarreraNoEncontradaException;
import com.uam.gestionacademica.application.exception.CorreoYaRegistradoException;
import com.uam.gestionacademica.application.exception.EstudianteNoEncontradoException;
import com.uam.gestionacademica.application.port.out.CarreraRepositoryPort;
import com.uam.gestionacademica.application.port.out.EstudianteRepositoryPort;
import com.uam.gestionacademica.domain.model.Estudiante;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EstudianteService {

    private final EstudianteRepositoryPort estudianteRepositoryPort;
    private final CarreraRepositoryPort carreraRepositoryPort;

    public EstudianteService(EstudianteRepositoryPort estudianteRepositoryPort,
                             CarreraRepositoryPort carreraRepositoryPort) {
        this.estudianteRepositoryPort = estudianteRepositoryPort;
        this.carreraRepositoryPort = carreraRepositoryPort;
    }

    /*
    * Caso de uso: Crear estudiante
    */
    @Transactional
    public Estudiante createEstudiante(Estudiante estudiante){
        estudiante.validateCanRegister();
        validateCorreoForCreate(estudiante.getCorreo());
        validateCarreraExists(estudiante.getCarreraId());

        return estudianteRepositoryPort.save(estudiante);
    }

    /*
    * Caso de uso: Obtener estudiante por ID
    * */
    @Transactional(readOnly = true)
    public Estudiante getEstudianteById(Long id){
        return estudianteRepositoryPort.findById(id)
                .orElseThrow(() -> new EstudianteNoEncontradoException(id));
    }

    /*
    * Caso de uso: Listar todos los estudiantes
    * */
    @Transactional(readOnly = true)
    public List<Estudiante> getAllEstudiantes(){
        return estudianteRepositoryPort.findAll();
    }

    /*
    * Caso de uso: Actualizar estudiante existente
    * */
    @Transactional
    public Estudiante updateEstudiante(Long id, Estudiante estudianteData){
        if(!estudianteRepositoryPort.existsById(id)){
            throw new EstudianteNoEncontradoException(id);
        }

        estudianteData.validateCanRegister();
        validateCorreoForUpdate(estudianteData.getCorreo(), id);
        validateCarreraExists(estudianteData.getCarreraId());

        Estudiante updatedEstudiante = new Estudiante(
                id,
                estudianteData.getNombre(),
                estudianteData.getCorreo(),
                estudianteData.getEdad(),
                estudianteData.getCarreraId()
        );

        return estudianteRepositoryPort.save(updatedEstudiante);
    }

    /*
    * Caso de uso: Eliminar estudiante
    * */
    @Transactional
    public void deleteEstudiante(Long id){
        if(!estudianteRepositoryPort.existsById(id)){
            throw new EstudianteNoEncontradoException(id);
        }

        estudianteRepositoryPort.deleteById(id);
    }

    /*
    * Valida que el correo no esté registrado al crear un estudiante
    * */
    private void validateCorreoForCreate(String correo){
        if(estudianteRepositoryPort.existsByCorreo(correo)){
            throw new CorreoYaRegistradoException(correo);
        }
    }

    /*
    * Valida que el correo no pertenezca a otro estudiante al actualizar
    * */
    private void validateCorreoForUpdate(String correo, Long id){
        if(estudianteRepositoryPort.existsByCorreoAndIdNot(correo, id)){
            throw new CorreoYaRegistradoException(correo);
        }
    }

    /*
    * Valida que la carrera asignada exista
    */
    private void validateCarreraExists(Long carreraId){
        if(carreraId == null || !carreraRepositoryPort.existsById(carreraId)){
            throw new CarreraNoEncontradaException(carreraId);
        }
    }
}
