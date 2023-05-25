package xadrez;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;

public abstract class PecasDeXadrez extends Peca {

    private Cor cor;
    private int contadorDeMovimentos;

    public PecasDeXadrez(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro);
        this.cor = cor;
    }

    public Cor getCor() {
        return cor;
    }

    public int getContadorDeMovimentos(){
        return contadorDeMovimentos;
    }

    public void aumentoContadorDeMovimento(){
        contadorDeMovimentos++;
    }

    public void diminuiContadorDeMovimento(){
        contadorDeMovimentos--;
    }

    public PosicaoXadrez getPosicaoXadrez(){
        return PosicaoXadrez.ondePosicao(posicao);
    }

    protected boolean issoEUmaPecaOponente(Posicao posicao){
        PecasDeXadrez p =(PecasDeXadrez)getTabuleiro().peca(posicao);
        return p != null && p.getCor() != cor;
    }

}
