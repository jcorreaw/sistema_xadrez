package xadrez;

import tabuleiro.ExceptionTabuleiro;

import java.io.Serial;

public class ExceptionXadrez extends ExceptionTabuleiro {
    @Serial
    private static final long serialVersionUID = 1L;
    public ExceptionXadrez(String msg){
        super(msg);
    }


}
