package tabuleiro;

public abstract class Peca {
    protected Posicao posicao;
    private Tabuleiro tabuleiro;

    public Peca(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
        posicao = null;
    }

    protected Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public abstract boolean[][] movimentoPossivel();

    public boolean movimentoPossivel(Posicao posicao){
        return movimentoPossivel()[posicao.getLinha()][posicao.getColuna()];
    }

    public boolean temMovimentoPOssivel(){
        boolean[][] mat = movimentoPossivel();
        for (int i =0; i<mat.length; i++){
            for (int j =0; j< mat.length; j++){
                if (mat[i][j]){
                    return true;
                }
            }
        }
        return false;
    }


}
