package com.mrInstruments.backend.service;

import com.mrInstruments.backend.dto.CharacteristicDto;
import com.mrInstruments.backend.entities.Characteristic;
import com.mrInstruments.backend.exception.BadRequestException;
import com.mrInstruments.backend.exception.ResourceNotFoundException;
import com.mrInstruments.backend.repository.CharacteristicRepository;
import com.mrInstruments.backend.utils.UtilsDtoMapper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mrInstruments.backend.utils.UtilsDtoMapper.characteristicDtoToCharacteristic;
import static com.mrInstruments.backend.utils.UtilsDtoMapper.characteristicToCharacteristicDto;

@Service
public class CharacteristicService {

    private final CharacteristicRepository characteristicRepository;
    Logger LOGGER = LogManager.getLogger(CategoryService.class);

    public CharacteristicService(CharacteristicRepository characteristicRepository) {
        this.characteristicRepository = characteristicRepository;
    }

    public CharacteristicDto saveCharacteristic(CharacteristicDto characteristicDto) throws Exception{
        Characteristic characteristic = new Characteristic();
        characteristic = characteristicDtoToCharacteristic(characteristicDto, characteristic);
        return characteristicToCharacteristicDto(characteristicRepository.save(characteristic));
    }

    public CharacteristicDto findCharacteristicById(Long id) throws Exception {
        Characteristic characteristic = characteristicRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Error al buscar id: " + id + " no se encontró."));
        LOGGER.log(Level.INFO,"Caracteristica con id= " + id + " fue encontrada");
        return characteristicToCharacteristicDto(characteristic);
    }

    public void deleteCharacteristic(Long id) throws BadRequestException{
        try {
            characteristicRepository.deleteById(id);
            LOGGER.log(Level.INFO,"Caracteristica con id= "+id+" fue eliminada");
        }catch (Exception ex){
            throw new BadRequestException("Error - no se pudo eliminar la categoria");
        }
    }

    public List<CharacteristicDto> listCharacteristic() {
        List<CharacteristicDto> characteristicListDto = characteristicRepository.findAll()
                .stream()
                .map(UtilsDtoMapper::characteristicToCharacteristicDto)
                .collect(Collectors.toList());
        LOGGER.log(Level.INFO,"Listando Caracteristicas...");

        return characteristicListDto;
    }

    public CharacteristicDto updateCharacteristic(Long id, CharacteristicDto characteristicDto) throws Exception{
        Optional<Long> optionalId = Optional.ofNullable(id);
        Optional<Characteristic> characteristicOptional = characteristicRepository.findById(id);

        if (optionalId.isPresent() && optionalId.get() > 0 && characteristicOptional.isPresent()) {
            try {
                Characteristic caracteristicaEncontrada = characteristicDtoToCharacteristic(characteristicDto,characteristicOptional.get());
                return characteristicToCharacteristicDto(characteristicRepository.save(caracteristicaEncontrada));
            } catch (Exception e) {
                throw new ResourceNotFoundException("Error al actualizar caracteristica - " + e.getMessage());
            }
        }else throw new ResourceNotFoundException("Id invalido o caracteristica no encontrada");
    }
}
