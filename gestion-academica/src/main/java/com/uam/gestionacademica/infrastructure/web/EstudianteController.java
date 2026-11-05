package com.uam.gestionacademica.infrastructure.web;

import com.uam.gestionacademica.application.service.EstudianteService;
import com.uam.gestionacademica.domain.model.Estudiante;
import com.uam.gestionacademica.infrastructure.web.dto.EstudianteResponse;
import com.uam.gestionacademica.infrastructure.web.dto.EstudianteUpdateRequest;
import com.uam.gestionacademica.infrastructure.web.dto.EstudianteRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
* Controlador REST
* Responsabilidades
* 1. Recibir HTTP
* 2. Validar el request (@Valid)
* 3. Convertir DTO de entrada a objeto de dominio
* 4. Llamar al caso de uso
* 5. Transformar el resultado a DTO de respuesta
* 6. Devolver código HTTP correcto
* 7. Devolver código HTTP correcto
*/
@RestController
@RequestMapping("/api/v1/estudiantes")
public class EstudianteController {
    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstudianteResponse> create(@Valid @RequestBody EstudianteRequest request){
        Estudiante estudiante = new Estudiante(
                null,
                request.nombre(),
                request.correo(),
                request.edad(),
                request.carrera()

        );

        Estudiante createdEstudiante = estudianteService.createEstudiante(estudiante);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(createdEstudiante));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE')")
    public ResponseEntity<EstudianteResponse> getById(@PathVariable Long id){
        Estudiante estudiante = estudianteService.getEstudianteById(id);
        return ResponseEntity.ok(toResponse(estudiante));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE')")
    public ResponseEntity<List<EstudianteResponse>> getAll(){
        List<EstudianteResponse> estudiantes = estudianteService.getAllEstudiantes()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(estudiantes);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstudianteResponse> update(@PathVariable Long id, @Valid @RequestBody EstudianteUpdateRequest request) {
        Estudiante estudiante = new Estudiante(
                id,
                request.nombre(),
                request.correo(),
                request.edad(),
                request.carrera()
        );

        Estudiante updatedEstudiante = estudianteService.updateEstudiante(id, estudiante);

        return ResponseEntity.ok(toResponse(updatedEstudiante));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        estudianteService.deleteEstudiante(id);
        return ResponseEntity.noContent().build();
    }

    /*
    * Conversión manual de Dominio a DTO de respuesta.
    *
    * */
    private EstudianteResponse toResponse(Estudiante estudiante){
        return new EstudianteResponse(
                estudiante.getId(),
                estudiante.getNombre(),
                estudiante.getCorreo(),
                estudiante.getEdad(),
                estudiante.getCarreraId()
        );
    }
}
