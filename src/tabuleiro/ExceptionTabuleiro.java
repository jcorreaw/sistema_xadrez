package tabuleiro;

import java.io.Serial;

public class ExceptionTabuleiro extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ExceptionTabuleiro(String msg){
        super(msg);
    }

}
