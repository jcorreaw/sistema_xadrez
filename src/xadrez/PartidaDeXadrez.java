package xadrez;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PartidaDeXadrez {
    private int turno;
    private Cor jogadorAtual;
    private Tabuleiro tabuleiro;
    private boolean check;
    private boolean checkMate;

    private List<Peca> pecasNoTabuleiro = new ArrayList<>();
    private List<Peca> pecasCapturadas = new ArrayList<>();

    public int getTurno() {
        return turno;
    }

    public Cor getJogadorAtual() {
        return jogadorAtual;
    }

    public boolean getCheck(){
        return check;
    }

    public boolean getCheckMate(){
        return checkMate;
    }

    public PartidaDeXadrez(){
        tabuleiro = new Tabuleiro(8,8);
        turno = 1;
        jogadorAtual = Cor.BRANCO;
        setupInicial();
    }
    public PecasDeXadrez[][] getPecas(){
        PecasDeXadrez[][] mat = new PecasDeXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
        for (int i= 0; i<tabuleiro.getLinhas(); i++){
            for (int j=0; j<tabuleiro.getColunas(); j++){
                mat[i][j] = (PecasDeXadrez) tabuleiro.peca(i, j);
            }
        }
        return mat;
    }

    public boolean[][] movimentoPossivel(PosicaoXadrez posicaoOrigem){
        Posicao posicao = posicaoOrigem.toPosicao();
        validaPosicaoOrigem(posicao);
        return tabuleiro.peca(posicao).movimentoPossivel();
    }

    public PecasDeXadrez executarJogadaDeXadrez(PosicaoXadrez posicaoOrigem,PosicaoXadrez posicaoDestino){
        Posicao origem = posicaoOrigem.toPosicao();
        Posicao destino = posicaoDestino.toPosicao();
        validaPosicaoOrigem(origem);
        validaPosicaoDestino(origem, destino);
        Peca pecaCapturada = fazerMover(origem, destino);

        if (testaCheck(jogadorAtual)){
            desfazerMovimento(origem, destino, pecaCapturada);
            throw new ExceptionXadrez("Voce nao pode se colocar em check!");
        }

        check = (testaCheck(oponente(jogadorAtual))) ? true : false;

        if (testaCheckMate(oponente(jogadorAtual))){
            checkMate = true;
        }
        else {
            proximoTurno();
        }
        return (PecasDeXadrez)pecaCapturada;
    }

    private Peca fazerMover(Posicao origem, Posicao destino){
        PecasDeXadrez p = (PecasDeXadrez) tabuleiro.removePeca(origem);
        p.aumentoContadorDeMovimento();
        Peca pecaCapturada = tabuleiro.removePeca(destino);
        tabuleiro.posicaoDaPeca(p, destino);

        if (pecaCapturada != null){
            pecasNoTabuleiro.remove(pecaCapturada);
            pecasCapturadas.add(pecaCapturada);
        }

        //movimentacao especial lado menor
        if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2){
            Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
            Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
            PecasDeXadrez torre = (PecasDeXadrez) tabuleiro.removePeca(origemT);
            tabuleiro.posicaoDaPeca(torre, destinoT);
            torre.aumentoContadorDeMovimento();
        }
        //movimentacao especial lado maior
        if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2){
            Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
            Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
            PecasDeXadrez torre = (PecasDeXadrez) tabuleiro.removePeca(origemT);
            tabuleiro.posicaoDaPeca(torre, destinoT);
            torre.aumentoContadorDeMovimento();
        }

        return pecaCapturada;
    }

    private void desfazerMovimento(Posicao origem, Posicao destino, Peca pecaCapturada){
        PecasDeXadrez p = (PecasDeXadrez) tabuleiro.removePeca(destino);
        p.diminuiContadorDeMovimento();
        tabuleiro.posicaoDaPeca(p, origem);

        if (pecaCapturada != null){
            tabuleiro.posicaoDaPeca(pecaCapturada, destino);
            pecasCapturadas.remove(pecaCapturada);
            pecasNoTabuleiro.add(pecaCapturada);
        }
        //movimentacao especial lado menor
        if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2){
            Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
            Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
            PecasDeXadrez torre = (PecasDeXadrez) tabuleiro.removePeca(destinoT);
            tabuleiro.posicaoDaPeca(torre, origemT);
            torre.diminuiContadorDeMovimento();
        }
        //movimentacao especial lado maior
        if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2){
            Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
            Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
            PecasDeXadrez torre = (PecasDeXadrez) tabuleiro.removePeca(destinoT);
            tabuleiro.posicaoDaPeca(torre, origemT);
            torre.diminuiContadorDeMovimento();
        }

    }

    private void validaPosicaoOrigem(Posicao posicao){
        if (!tabuleiro.haUmaPeca(posicao)){
            throw new ExceptionXadrez("Não há peça na posicao de origem!");
        }
        if (jogadorAtual != ((PecasDeXadrez)tabuleiro.peca(posicao)).getCor()){
            throw new ExceptionXadrez("A peça escolhida não é sua!");
        }


        if (!tabuleiro.peca(posicao).temMovimentoPOssivel()){
            throw new ExceptionXadrez("Nao existe movimentos possiveis para a peça escolhida!");
        }
    }

    private void validaPosicaoDestino(Posicao origem, Posicao destino){
        if (!tabuleiro.peca(origem).movimentoPossivel(destino)){
            throw new ExceptionXadrez("A peça nao pode mover para a posição de destino!");
        }
    }

    private void proximoTurno(){
        turno++;
        jogadorAtual = (jogadorAtual == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
    }

    private Cor oponente(Cor cor){
        return (cor == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
    }

    private PecasDeXadrez rei(Cor cor){
        List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecasDeXadrez)x).getCor() == cor).collect(Collectors.toList());
        for (Peca p : list){
            if (p instanceof Rei){
                return (PecasDeXadrez) p;
            }
        }
        throw new IllegalStateException("Nao existe o rei da cor " + cor + " no tabuleiro");
    }

    private boolean testaCheck(Cor cor){
        Posicao posicaoRei = rei(cor).getPosicaoXadrez().toPosicao();
        List<Peca> pecasOponentes = pecasNoTabuleiro.stream().filter(x -> ((PecasDeXadrez)x).getCor() == oponente(cor)).collect(Collectors.toList());
        for (Peca p : pecasOponentes){
            boolean[][] mat = p.movimentoPossivel();
            if (mat[posicaoRei.getLinha()][posicaoRei.getColuna()]){
                return true;
            }
        }
        return false;
    }

    private boolean testaCheckMate(Cor cor){
        if (!testaCheck(cor)){
            return false;
        }
        List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecasDeXadrez)x).getCor() == cor).collect(Collectors.toList());
        for (Peca p : list){
            boolean[][] mat = p.movimentoPossivel();
            for (int i=0; i<tabuleiro.getLinhas(); i++){
                for (int j=0; j< tabuleiro.getColunas(); j++){
                    if (mat[i][j]){
                        Posicao origem = ((PecasDeXadrez)p).getPosicaoXadrez().toPosicao();
                        Posicao destino = new Posicao(i,j);
                        Peca pecaCapturada = fazerMover(origem, destino);
                        boolean testaCheck = testaCheck(cor);
                        desfazerMovimento(origem, destino, pecaCapturada);
                        if (!testaCheck){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }


    private void lugarNovaPeca(char coluna, int linha, PecasDeXadrez peca){
        tabuleiro.posicaoDaPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());
        pecasNoTabuleiro.add(peca);
    }

    private void setupInicial(){
        lugarNovaPeca('a', 1, new Torre(tabuleiro, Cor.BRANCO));
        lugarNovaPeca('b', 1, new Cavalo(tabuleiro, Cor.BRANCO));
        lugarNovaPeca('c', 1, new Bispo(tabuleiro, Cor.BRANCO));
        lugarNovaPeca('d', 1, new Rainha(tabuleiro, Cor.BRANCO));
        lugarNovaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCO, this));
        lugarNovaPeca('f', 1, new Bispo(tabuleiro, Cor.BRANCO));
        lugarNovaPeca('g', 1, new Cavalo(tabuleiro, Cor.BRANCO));
        lugarNovaPeca('h', 1, new Torre(tabuleiro, Cor.BRANCO));
        lugarNovaPeca('a', 2, new Peao(tabuleiro, Cor.BRANCO));
        lugarNovaPeca('b', 2, new Peao(tabuleiro, Cor.BRANCO));
        lugarNovaPeca('c', 2, new Peao(tabuleiro, Cor.BRANCO));
        lugarNovaPeca('d', 2, new Peao(tabuleiro, Cor.BRANCO));
        lugarNovaPeca('e', 2, new Peao(tabuleiro, Cor.BRANCO));
        lugarNovaPeca('f', 2, new Peao(tabuleiro, Cor.BRANCO));
        lugarNovaPeca('g', 2, new Peao(tabuleiro, Cor.BRANCO));
        lugarNovaPeca('h', 2, new Peao(tabuleiro, Cor.BRANCO));

        lugarNovaPeca('a', 8, new Torre(tabuleiro, Cor.PRETO));
        lugarNovaPeca('b', 8, new Cavalo(tabuleiro, Cor.PRETO));
        lugarNovaPeca('c', 8, new Bispo(tabuleiro, Cor.PRETO));
        lugarNovaPeca('d', 8, new Rainha(tabuleiro, Cor.PRETO));
        lugarNovaPeca('e', 8, new Rei(tabuleiro, Cor.PRETO, this));
        lugarNovaPeca('f', 8, new Bispo(tabuleiro, Cor.PRETO));
        lugarNovaPeca('g', 8, new Cavalo(tabuleiro, Cor.PRETO));
        lugarNovaPeca('h', 8, new Torre(tabuleiro, Cor.PRETO));
        lugarNovaPeca('a', 7, new Peao(tabuleiro, Cor.PRETO));
        lugarNovaPeca('b', 7, new Peao(tabuleiro, Cor.PRETO));
        lugarNovaPeca('c', 7, new Peao(tabuleiro, Cor.PRETO));
        lugarNovaPeca('d', 7, new Peao(tabuleiro, Cor.PRETO));
        lugarNovaPeca('e', 7, new Peao(tabuleiro, Cor.PRETO));
        lugarNovaPeca('f', 7, new Peao(tabuleiro, Cor.PRETO));
        lugarNovaPeca('g', 7, new Peao(tabuleiro, Cor.PRETO));
        lugarNovaPeca('h', 7, new Peao(tabuleiro, Cor.PRETO));




    }

}
