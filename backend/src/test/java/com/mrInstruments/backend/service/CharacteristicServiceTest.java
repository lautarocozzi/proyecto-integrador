package com.mrInstruments.backend.service;

import com.mrInstruments.backend.dto.CharacteristicDto;
import com.mrInstruments.backend.entities.Characteristic;
import com.mrInstruments.backend.exception.BadRequestException;
import com.mrInstruments.backend.repository.CharacteristicRepository;
import com.mrInstruments.backend.utils.UtilsDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static com.mrInstruments.backend.utils.UtilsDtoMapper.characteristicDtoToCharacteristic;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharacteristicServiceTest {

    @Mock
    CharacteristicRepository characteristicRepository;
    @InjectMocks
    CharacteristicService characteristicService;

    private CharacteristicDto char1Dto;
    private CharacteristicDto char2Dto;

    @BeforeEach
    @DisplayName("Cargando Data..")
    void setup(){
        char1Dto = new CharacteristicDto(1l,"Portatil","url-img");
        char2Dto = new CharacteristicDto(2l,"Liviano","url-img");

    }


    @Test
    @DisplayName("Deberia guardar caracteristica - Service")
    void saveCharacteristic() throws Exception {
        //given
        lenient().when(characteristicRepository.save(any(Characteristic.class))).thenReturn(characteristicDtoToCharacteristic(char1Dto,new Characteristic()));
        //when
        CharacteristicDto charGuardada = characteristicService.saveCharacteristic(char1Dto);
        //then
        assertTrue(Optional.of(charGuardada).isPresent());

    }

    @Test
    @DisplayName("Deberia buscar caracteristicas x ID - Service")
    void findCharacteristicById() throws Exception {
        //given
        lenient().when(characteristicRepository.findById(char1Dto.getId())).thenReturn(Optional.of(characteristicDtoToCharacteristic(char1Dto,new Characteristic())));
        //when
        CharacteristicDto charEncontrada = characteristicService.findCharacteristicById(char1Dto.getId());
        //then
        assertTrue(Optional.of(charEncontrada).isPresent());
    }

    @Test
    @DisplayName("Deberia eliminar caracteristica x ID - Service")
    void deleteCharacteristic() throws BadRequestException {
        //given
        willDoNothing().given(characteristicRepository).deleteById(char1Dto.getId());

        //when
        characteristicService.deleteCharacteristic(char1Dto.getId());
        //then
        verify(characteristicRepository,timeout(1)).deleteById(char1Dto.getId());

    }

    @Test
    @DisplayName("Deberia listar caracteristicas - Service")
    void listCharacteristic() {
        //given
        given(characteristicRepository.findAll()).willReturn(List.of(characteristicDtoToCharacteristic(char1Dto,new Characteristic()),characteristicDtoToCharacteristic(char2Dto,new Characteristic())));
        //when
        List<CharacteristicDto> listaCaracteristicas = characteristicService.listCharacteristic();
        //then
        assertTrue(listaCaracteristicas.size()==2);
        assertTrue(Optional.of(listaCaracteristicas).isPresent());
    }

    @Test
    @DisplayName("Deberia actualizar caracteristica - Service")
    void updateCharacteristic() throws Exception {
        //given
        lenient().when(characteristicRepository.save(any(Characteristic.class))).thenReturn(characteristicDtoToCharacteristic(char1Dto,new Characteristic()));
        lenient().when(characteristicRepository.findById(char1Dto.getId())).thenReturn(Optional.of(characteristicDtoToCharacteristic(char1Dto,new Characteristic())));
        //when
        CharacteristicDto charActualizada = characteristicService.updateCharacteristic(char1Dto.getId(), char1Dto);
        //then
        assertTrue(Optional.of(charActualizada).isPresent());
    }
}