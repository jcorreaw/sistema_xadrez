package xadrez;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.*;

import java.security.InvalidParameterException;
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
    private PecasDeXadrez vulnerabilidadeEnPassant;
    private PecasDeXadrez promocaoDoPeao;


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

    public PecasDeXadrez getvulnerabilidadeEnPassant() {
        return vulnerabilidadeEnPassant;
    }

    public PecasDeXadrez getPromocaoDoPeao() {
        return promocaoDoPeao;
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

        PecasDeXadrez pecaMovida = (PecasDeXadrez)tabuleiro.peca(destino);

        //movimento especial promocao Do Peao
        promocaoDoPeao = null;
        if (pecaMovida instanceof Peao){
            if (pecaMovida.getCor() == Cor.BRANCO && destino.getLinha() == 0 || pecaMovida.getCor() == Cor.PRETO && destino.getLinha() == 7){
                promocaoDoPeao = (PecasDeXadrez)tabuleiro.peca(destino);
                promocaoDoPeao = trocaPecaPromocao("Q");
            }
        }


        check = (testaCheck(oponente(jogadorAtual))) ? true : false;

        if (testaCheckMate(oponente(jogadorAtual))){
            checkMate = true;
        }
        else {
            proximoTurno();
        }

        //testa se foi um peao para En Passant e executa
        if (pecaMovida instanceof Peao && (destino.getLinha() == origem.getLinha() - 2 || destino.getLinha() == origem.getLinha() + 2)){
            vulnerabilidadeEnPassant = pecaMovida;
        }
        else {
            vulnerabilidadeEnPassant = null;
        }

        return (PecasDeXadrez)pecaCapturada;
    }

    public PecasDeXadrez trocaPecaPromocao(String tipo){
        if (promocaoDoPeao == null){
            throw new IllegalStateException("Essa peça não pode ser promovida!");
        }
        if (!tipo.equals("B") && !tipo.equals("C") && !tipo.equals("R") && !tipo.equals("Q")){
            throw new InvalidParameterException("Tipo invalido para promoção!");
        }

        Posicao pos = promocaoDoPeao.getPosicaoXadrez().toPosicao();
        Peca p = tabuleiro.removePeca(pos);
        pecasNoTabuleiro.remove(p);

        PecasDeXadrez novaPeca = novaPeca(tipo, promocaoDoPeao.getCor());
        tabuleiro.posicaoDaPeca(novaPeca, pos);
        pecasNoTabuleiro.add(novaPeca);

        return novaPeca;
    }

    private PecasDeXadrez novaPeca(String tipo, Cor cor){
        if (tipo.equals("B")) return new Bispo(tabuleiro, cor);
        if (tipo.equals("C")) return new Cavalo(tabuleiro, cor);
        if (tipo.equals("Q")) return new Rainha(tabuleiro, cor);
        return new Torre(tabuleiro, cor);
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

        //movimewntacao en passant
        if (p instanceof Peao){
            if (origem.getColuna() != destino.getColuna() && pecaCapturada == null){
                Posicao posicaoPeao;
                if (p.getCor() == Cor.BRANCO){
                    posicaoPeao = new Posicao(destino.getLinha() + 1, destino.getColuna());
                }
                else {
                    posicaoPeao = new Posicao(destino.getLinha() - 1, destino.getColuna());
                }
                pecaCapturada = tabuleiro.removePeca(posicaoPeao);
                pecasCapturadas.add(pecaCapturada);
                pecasNoTabuleiro.remove(pecaCapturada);
            }
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

        //movimentacao en passant
        if (p instanceof Peao){
            if (origem.getColuna() != destino.getColuna() && pecaCapturada == vulnerabilidadeEnPassant){
                PecasDeXadrez peao = (PecasDeXadrez)tabuleiro.removePeca(destino);
                Posicao posicaoPeao;
                if (p.getCor() == Cor.BRANCO){
                    posicaoPeao = new Posicao(3, destino.getColuna());
                }
                else {
                    posicaoPeao = new Posicao(4, destino.getColuna());
                }
                tabuleiro.posicaoDaPeca(peao, posicaoPeao);
            }
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
        lugarNovaPeca('a', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        lugarNovaPeca('b', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        lugarNovaPeca('c', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        lugarNovaPeca('d', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        lugarNovaPeca('e', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        lugarNovaPeca('f', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        lugarNovaPeca('g', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        lugarNovaPeca('h', 2, new Peao(tabuleiro, Cor.BRANCO, this));

        lugarNovaPeca('a', 8, new Torre(tabuleiro, Cor.PRETO));
        lugarNovaPeca('b', 8, new Cavalo(tabuleiro, Cor.PRETO));
        lugarNovaPeca('c', 8, new Bispo(tabuleiro, Cor.PRETO));
        lugarNovaPeca('d', 8, new Rainha(tabuleiro, Cor.PRETO));
        lugarNovaPeca('e', 8, new Rei(tabuleiro, Cor.PRETO, this));
        lugarNovaPeca('f', 8, new Bispo(tabuleiro, Cor.PRETO));
        lugarNovaPeca('g', 8, new Cavalo(tabuleiro, Cor.PRETO));
        lugarNovaPeca('h', 8, new Torre(tabuleiro, Cor.PRETO));
        lugarNovaPeca('a', 7, new Peao(tabuleiro, Cor.PRETO, this));
        lugarNovaPeca('b', 7, new Peao(tabuleiro, Cor.PRETO, this));
        lugarNovaPeca('c', 7, new Peao(tabuleiro, Cor.PRETO, this));
        lugarNovaPeca('d', 7, new Peao(tabuleiro, Cor.PRETO, this));
        lugarNovaPeca('e', 7, new Peao(tabuleiro, Cor.PRETO, this));
        lugarNovaPeca('f', 7, new Peao(tabuleiro, Cor.PRETO, this));
        lugarNovaPeca('g', 7, new Peao(tabuleiro, Cor.PRETO, this));
        lugarNovaPeca('h', 7, new Peao(tabuleiro, Cor.PRETO, this));




    }


}
