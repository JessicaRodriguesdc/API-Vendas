package br.com.sistemavendas.util.exception;

public class SenhaInvalidaException extends RuntimeException {
    public SenhaInvalidaException(){
        super("Senha invalida!");
    }
}
