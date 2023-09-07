package com.mrInstruments.backend.utils;

import com.mrInstruments.backend.exception.BadRequestException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public final class UtilsFileManager {

    public static String fileTextToString(String pathFile) throws BadRequestException {

        BufferedReader br = null;
        StringBuilder stringBuilder;
        stringBuilder = new StringBuilder();
        try {
            br = new BufferedReader(new FileReader(pathFile));
            String texto = br.readLine();
            // Repetir mientras no se llegue al final del fichero
            while (texto != null) {
                stringBuilder.append(texto);
                texto = br.readLine();
            }
        } catch (FileNotFoundException ex) {
            throw new BadRequestException("Error: Fichero no encontrado --- " + ex.getMessage());
        } catch (Exception ex) {
            throw new BadRequestException("Error de lectura del fichero --- " + ex.getMessage());
        } finally {
            try {
                // Cerrar el fichero si se ha podido abrir
                if (br != null) {
                    br.close();
                }
                return stringBuilder.toString();
            } catch (Exception ex) {
                throw new BadRequestException("Error al cerrar el fichero --- " + ex.getMessage());
            }
        }
    }
}
