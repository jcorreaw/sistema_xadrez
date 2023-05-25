package aplicacao;

import xadrez.Cor;
import xadrez.PartidaDeXadrez;
import xadrez.PecasDeXadrez;
import xadrez.PosicaoXadrez;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class UI {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    public static void limpartTela(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static PosicaoXadrez lerPosicaoXadrez(Scanner sc){
        try{
            String s = sc.nextLine();
            char coluna = s.charAt(0);
            int linha = Integer.parseInt(s.substring(1));
            return new PosicaoXadrez(coluna, linha);
        }
        catch (RuntimeException e) {
            throw new InputMismatchException("Erro ao ler posiçao do xadrez. Valores validos sao de a1 ate h8. ");
        }

    }

    public static void mostraTabuleiro(PecasDeXadrez[][] pecas){
        for (int i=0; i<pecas.length; i++){
            System.out.print((8-i) + " ");
            for (int j=0; j<pecas.length; j++){
                mostraPeca(pecas[i][j], false);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }


    private static void  mostraPeca(PecasDeXadrez peca, boolean fundo) {
        if (fundo){
            System.out.print(ANSI_BLUE_BACKGROUND);
        }
        if (peca == null) {
            System.out.print("-" + ANSI_RESET);
        }
        else {
            if (peca.getCor() == Cor.BRANCO) {
                System.out.print(ANSI_WHITE + peca + ANSI_RESET);
            }
            else {
                System.out.print(ANSI_YELLOW + peca + ANSI_RESET);
            }
        }
        System.out.print(" ");
    }

    public static void mostraPartida(PartidaDeXadrez partidaDeXadrez, List<PecasDeXadrez> capturada){
        mostraTabuleiro(partidaDeXadrez.getPecas());
        System.out.println();
        mostraPecasCapturadas(capturada);
        System.out.println();
        System.out.println("Turno: " + partidaDeXadrez.getTurno());
        if (!partidaDeXadrez.getCheckMate()){
            System.out.println("Esperando jogador: " + partidaDeXadrez.getJogadorAtual());
            if (partidaDeXadrez.getCheck()){
                System.out.println("CHECK!");
            }
        }
        else {
            System.out.println("CHECKMATE!");
            System.out.println("Vencedor: " + partidaDeXadrez.getJogadorAtual());
        }

    }

    public static void mostraTabuleiro(PecasDeXadrez[][] pecas, boolean[][] movimentoPossivel){
        for (int i=0; i<pecas.length; i++){
            System.out.print((8-i) + " ");
            for (int j=0; j<pecas.length; j++){
                mostraPeca(pecas[i][j], movimentoPossivel[i][j]);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    private static void mostraPecasCapturadas(List <PecasDeXadrez> capturada){
        List <PecasDeXadrez> branco = capturada.stream().filter(x -> x.getCor() == Cor.BRANCO).collect(Collectors.toList());
        List <PecasDeXadrez> preto = capturada.stream().filter(x -> x.getCor() == Cor.PRETO).collect(Collectors.toList());
        System.out.println("Peças capturadas:");
        System.out.println("Branco:");
        System.out.print(ANSI_WHITE);
        System.out.println(Arrays.toString(branco.toArray()));
        System.out.print(ANSI_RESET);
        System.out.println("Preto:");
        System.out.print(ANSI_YELLOW);
        System.out.println(Arrays.toString(preto.toArray()));
        System.out.print(ANSI_RESET);
    }


}
