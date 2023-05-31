package xadrez.pecas;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PartidaDeXadrez;
import xadrez.PecasDeXadrez;

public class Peao extends PecasDeXadrez {
    private PartidaDeXadrez partidaDeXadrez;

    public Peao(Tabuleiro tabuleiro, Cor cor, PartidaDeXadrez partidaDeXadrez) {
        super(tabuleiro, cor);
        this.partidaDeXadrez = partidaDeXadrez;
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

            if (posicao.getLinha() == 3){
                Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
                if (getTabuleiro().posicaoExiste(esquerda) && issoEUmaPecaOponente(esquerda) && getTabuleiro().peca(esquerda) == partidaDeXadrez.getvulnerabilidadeEnPassant()){
                    mat[esquerda.getLinha() - 1][esquerda.getColuna()] = true;
                }

                Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
                if (getTabuleiro().posicaoExiste(direita) && issoEUmaPecaOponente(direita) && getTabuleiro().peca(direita) == partidaDeXadrez.getvulnerabilidadeEnPassant()){
                    mat[direita.getLinha() - 1][direita.getColuna()] = true;
                }
            }
        }
        else {
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

            if (posicao.getLinha() == 4){
                Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
                if (getTabuleiro().posicaoExiste(esquerda) && issoEUmaPecaOponente(esquerda) && getTabuleiro().peca(esquerda) == partidaDeXadrez.getvulnerabilidadeEnPassant()){
                    mat[esquerda.getLinha() + 1][esquerda.getColuna()] = true;
                }

                Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
                if (getTabuleiro().posicaoExiste(direita) && issoEUmaPecaOponente(direita) && getTabuleiro().peca(direita) == partidaDeXadrez.getvulnerabilidadeEnPassant()){
                    mat[direita.getLinha() + 1][direita.getColuna()] = true;
                }
            }

        }
        return mat;
    }

    @Override
    public String toString(){
        return "P";
    }

}
