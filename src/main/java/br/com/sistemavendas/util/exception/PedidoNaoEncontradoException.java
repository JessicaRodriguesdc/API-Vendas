package br.com.sistemavendas.util.exception;

public class PedidoNaoEncontradoException extends RuntimeException {
    public PedidoNaoEncontradoException(){
        super("Pedido nao encontrado");
    }
}
