package br.com.sistemavendas.exception;

public class SenhaInvalidaException extends RuntimeException {
    public SenhaInvalidaException(){
        super("Senha invalida!");
    }
}
