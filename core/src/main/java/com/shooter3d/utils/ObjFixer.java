package com.shooter3d.utils;

public class ObjFixer {
    
    public static String fixCommas(String objContent) {
        // Заменяем запятые на точки в координатах
        // Ищем паттерны вида "число,число" и заменяем на "число.число"
        StringBuilder result = new StringBuilder();
        String[] lines = objContent.split("\n");
        
        for (String line : lines) {
            String fixedLine = line;
            if (line.startsWith("v ") || line.startsWith("vn ") || line.startsWith("vt ")) {
                // Заменяем запятые на точки в координатах
                fixedLine = line.replace(',', '.');
            }
            result.append(fixedLine).append("\n");
        }
        
        return result.toString();
    }
}
