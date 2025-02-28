package com.local.clientes.router.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientePlusDto {

    private String nombre;
    private String apellido;
    private int edad;
    private LocalDate fechaNacimiento;
    private int esperanzaDeVida;

}
