package net.notfab.dimensionchanger.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class NMSException extends Exception {

    @Getter
    private String message;

}