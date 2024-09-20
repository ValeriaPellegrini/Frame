package com.telecom.pages;


import java.util.HashMap;
import java.util.Map;

public class ProcesadorDeTextos {
    public static Map<String, String> parseTexto(String texto) {
        Map<String, String> pares = new HashMap<>();
        String[] paresTexto = texto.split("\\s+\\|\\s+");
    
        for (String parTexto : paresTexto) {
            String[] par = parTexto.split("\\|");
            if (par.length == 2) {
                pares.put(par[0].trim(), par[1].trim()); // Eliminamos espacios en blanco iniciales y finales
            }
        }
    
        return pares;
    }

    String[] paresTexto = texto.split("\\s\\s"); //Split por dos espacios

}
