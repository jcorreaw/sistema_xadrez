package xadrez.pecas;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecasDeXadrez;

public class Peao extends PecasDeXadrez {

    public Peao(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro, cor);
    }

    @Override
    public boolean[][] movimentoPossivel() {
        boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

        Posicao p = new Posicao(0, 0);
        if (getCor() == Cor.BRANCO){
            p.setValues(posicao.getLinha() - 1, posicao.getColuna());
            if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().haUmaPeca(p)){
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.setValues(posicao.getLinha() - 2, posicao.getColuna());
            Posicao p2 = new Posicao(posicao.getLinha() - 1, posicao.getColuna());
            if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().haUmaPeca(p) && getTabuleiro().posicaoExiste(p2) && !getTabuleiro().haUmaPeca(p2) && getContadorDeMovimentos() == 0){
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.setValues(posicao.getLinha() - 1, posicao.getColuna() - 1);
            if (getTabuleiro().posicaoExiste(p) && issoEUmaPecaOponente(p)){
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.setValues(posicao.getLinha() - 1, posicao.getColuna() + 1);
            if (getTabuleiro().posicaoExiste(p) && issoEUmaPecaOponente(p)){
                mat[p.getLinha()][p.getColuna()] = true;
            }
        } else {
            p.setValues(posicao.getLinha() + 1, posicao.getColuna());
            if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().haUmaPeca(p)){
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.setValues(posicao.getLinha() + 2, posicao.getColuna());
            Posicao p2 = new Posicao(posicao.getLinha() + 1, posicao.getColuna());
            if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().haUmaPeca(p) && getTabuleiro().posicaoExiste(p2) && !getTabuleiro().haUmaPeca(p2) && getContadorDeMovimentos() == 0){
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.setValues(posicao.getLinha() + 1, posicao.getColuna() -1);
            if (getTabuleiro().posicaoExiste(p) && issoEUmaPecaOponente(p)){
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.setValues(posicao.getLinha() + 1, posicao.getColuna() +1);
            if (getTabuleiro().posicaoExiste(p) && issoEUmaPecaOponente(p)){
                mat[p.getLinha()][p.getColuna()] = true;
            }
        }
        return mat;
    }

    @Override
    public String toString(){
        return "P";
    }

}
