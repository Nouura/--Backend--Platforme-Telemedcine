package com.mycompany.platforme_telemedcine.dto;

public class SpecialtyPointDTO {
    public String name;
    public double value;   // %
    public String color;

    public SpecialtyPointDTO(String name, double value, String color) {
        this.name = name;
        this.value = value;
        this.color = color;
    }
}