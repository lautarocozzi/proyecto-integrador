package com.mrInstruments.backend.controller;

import com.mrInstruments.backend.dto.CharacteristicDto;
import com.mrInstruments.backend.entities.Characteristic;
import com.mrInstruments.backend.exception.BadRequestException;
import com.mrInstruments.backend.exception.ResourceNotFoundException;
import com.mrInstruments.backend.service.CharacteristicService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/caracteristicas")
public class  CharacteristicController {

    @Autowired
    private CharacteristicService characteristicService;

    Logger LOGGER = LogManager.getLogger(CharacteristicController.class);

    @PostMapping
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<CharacteristicDto> createCharacteristic(@RequestBody CharacteristicDto characteristicDto) throws Exception{
        CharacteristicDto nuevoCaracteristicaDto = characteristicService.saveCharacteristic(characteristicDto);
        return ResponseEntity.ok(nuevoCaracteristicaDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> searchCharacteristic(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.ok(characteristicService.findCharacteristicById(id));
    }

    @DeleteMapping("/{id}")
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<?> deleteCharacteristic(@PathVariable Long id) throws Exception {
        characteristicService.findCharacteristicById(id);
        characteristicService.deleteCharacteristic(id);
        return ResponseEntity.ok("Se elimino correctamente la caracteristica con id: " + id);
    }

    @GetMapping
    public ResponseEntity<?> listCharacteristic() throws Exception {
        return ResponseEntity.ok(characteristicService.listCharacteristic());
    }

    @PutMapping("/{id}")
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<CharacteristicDto> updateCharacteristic(@PathVariable Long id, @RequestBody CharacteristicDto characteristicDto) throws Exception {
        return ResponseEntity.ok(characteristicService.updateCharacteristic(id, characteristicDto));
    }
}