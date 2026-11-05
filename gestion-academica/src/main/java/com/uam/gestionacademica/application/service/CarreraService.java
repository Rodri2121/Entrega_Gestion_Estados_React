package com.uam.gestionacademica.application.service;

import com.uam.gestionacademica.application.exception.CarreraConEstudiantesException;
import com.uam.gestionacademica.application.exception.CarreraNoEncontradaException;
import com.uam.gestionacademica.application.exception.CodigoCarreraYaRegistradoException;
import com.uam.gestionacademica.application.port.out.CarreraRepositoryPort;
import com.uam.gestionacademica.application.port.out.EstudianteRepositoryPort;
import com.uam.gestionacademica.domain.model.Carrera;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CarreraService {
    private final CarreraRepositoryPort carreraRepositoryPort;
    private final EstudianteRepositoryPort estudianteRepositoryPort;

    public CarreraService(CarreraRepositoryPort carreraRepositoryPort, EstudianteRepositoryPort estudianteRepositoryPort) {
        this.carreraRepositoryPort = carreraRepositoryPort;
        this.estudianteRepositoryPort = estudianteRepositoryPort;
    }

    /*
    * Caso de uso: Crear carrera
    */
    @Transactional
    public Carrera createCarrera(Carrera carrera){
        if(carreraRepositoryPort.existsByCodigo(carrera.getCodigo())){
            throw  new CodigoCarreraYaRegistradoException(carrera.getCodigo());
        }

        return carreraRepositoryPort.save(carrera);
    }

    /*
    * Caso de uso: Obtener carrera por ID
    */
    @Transactional(readOnly = true)
    public Carrera getCarreraById(Long id){
        return carreraRepositoryPort.findById(id)
                .orElseThrow(() -> new CarreraNoEncontradaException(id));
    }

    /*
    * Caso de uso: Listar Carreras
    */
    @Transactional(readOnly = true)
    public List<Carrera> getAllCarreras(){
        return carreraRepositoryPort.findAll();
    }

    /*
    * Caso de uso: Actualizar carrera
    */
    @Transactional
    public Carrera updateCarrera(Long id, Carrera carreraData){
        if(!carreraRepositoryPort.existsById(id)){
            throw new CarreraNoEncontradaException(id);
        }

        if(carreraRepositoryPort.existsByCodigoAndIdNot(carreraData.getCodigo(), id)){
            throw new CodigoCarreraYaRegistradoException(carreraData.getCodigo());
        }

        Carrera updateCarrera = new Carrera(
                id,
                carreraData.getCodigo(),
                carreraData.getNombre()
        );

        return carreraRepositoryPort.save(updateCarrera);
    }

    /*
     * Caso de uso: Eliminar carrera
     * No se permite si tiene estudiantes asociados.
     */
    @Transactional
    public void deleteCarrera(Long id) {
        if (!carreraRepositoryPort.existsById(id)) {
            throw new CarreraNoEncontradaException(id);
        }

        if (estudianteRepositoryPort.existsByCarreraId(id)) {
            throw new CarreraConEstudiantesException(id);
        }

        carreraRepositoryPort.deleteById(id);
}
}
