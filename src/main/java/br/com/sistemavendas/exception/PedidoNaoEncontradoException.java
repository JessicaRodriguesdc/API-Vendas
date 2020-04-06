package br.com.sistemavendas.exception;

public class PedidoNaoEncontradoException extends RuntimeException {
    public PedidoNaoEncontradoException(){
        super("Pedido nao encontrado");
    }
}
