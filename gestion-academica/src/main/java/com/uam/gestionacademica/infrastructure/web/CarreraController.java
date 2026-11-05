package com.uam.gestionacademica.infrastructure.web;

import com.uam.gestionacademica.application.service.CarreraService;
import com.uam.gestionacademica.domain.model.Carrera;
import com.uam.gestionacademica.infrastructure.web.dto.CarreraRequest;
import com.uam.gestionacademica.infrastructure.web.dto.CarreraResponse;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carreras")
public class CarreraController {
    private final CarreraService carreraService;

    public CarreraController(CarreraService carreraService) {
        this.carreraService = carreraService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CarreraResponse> create(@Valid @RequestBody CarreraRequest request){
        Carrera carrera = new Carrera(null, request.codigo(), request.nombre());
        Carrera createdCarrera = carreraService.createCarrera(carrera);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(createdCarrera));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCENTE', 'ESTUDIANTE')")
    public ResponseEntity<List<CarreraResponse>> getAll(){
        List<CarreraResponse> carreras = carreraService.getAllCarreras()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(carreras);

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE', 'ESTUDIANTE')")
    public ResponseEntity<CarreraResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(toResponse(carreraService.getCarreraById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CarreraResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody CarreraRequest request){
        Carrera carrera = new Carrera(id, request.codigo(), request.nombre());
        Carrera updatedCarrera = carreraService.updateCarrera(id, carrera);

        return ResponseEntity.ok(toResponse(updatedCarrera));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        carreraService.deleteCarrera(id);
        return ResponseEntity.noContent().build();
    }

    private CarreraResponse toResponse(Carrera carrera){
        return new CarreraResponse(
                carrera.getId(),
                carrera.getCodigo(),
                carrera.getNombre()
        );
    }
}
