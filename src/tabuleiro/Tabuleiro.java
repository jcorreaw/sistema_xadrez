package tabuleiro;

public class Tabuleiro {
    private int linhas;
    private int colunas;
    private Peca[][] pecas;

    public Tabuleiro(int linhas, int colunas) {
        if (linhas < 1 || colunas < 1){
            throw new ExceptionTabuleiro("Erro ao criar o tabuleiro: o numero de linhas e colunas deve ser maior que 1.");
        }
        this.linhas = linhas;
        this.colunas = colunas;
        pecas = new Peca[linhas][colunas];
    }

    public int getLinhas() {
        return linhas;
    }

    public int getColunas() {
        return colunas;
    }

    public  Peca peca(int linha, int coluna){
        if (!posicaoExiste(linha, coluna)){
            throw new ExceptionTabuleiro("Essa posição nao existe");
        }
        return pecas[linha][coluna];
    }

    public  Peca peca(Posicao posicao){
        if (!posicaoExiste(posicao)){
            throw new ExceptionTabuleiro("Essa posição nao existe");
        }
        return pecas[posicao.getLinha()][posicao.getColuna()];
    }

    public void posicaoDaPeca(Peca peca, Posicao posicao){
        if (haUmaPeca(posicao)){
            throw new ExceptionTabuleiro("Ja existe uma peça nessa posição "+ posicao);
        }
        pecas[posicao.getLinha()][posicao.getColuna()] = peca;
        peca.posicao = posicao;
    }

    public Peca removePeca(Posicao posicao){
        if(!posicaoExiste(posicao)){
            throw new ExceptionTabuleiro("Essa posição nao existe");
        }
        if (peca(posicao) == null) {
            return null;
        }
        Peca aux = peca(posicao);
        aux.posicao = null;
        pecas[posicao.getLinha()][posicao.getColuna()] = null;
        return aux;
    }

    private boolean posicaoExiste(int linha, int coluna){
        return linha >= 0 && linha < linhas && coluna >= 0 && coluna < colunas;
    }

    public boolean posicaoExiste(Posicao posicao) {
        return posicaoExiste(posicao.getLinha(), posicao.getColuna());
    }

    public boolean haUmaPeca(Posicao posicao){
        if (!posicaoExiste(posicao)){
            throw new ExceptionTabuleiro("Essa posição nao existe");
        }
        return peca(posicao) != null;
    }

}
