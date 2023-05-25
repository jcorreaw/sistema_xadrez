package aplicacao;

import xadrez.ExceptionXadrez;
import xadrez.PartidaDeXadrez;
import xadrez.PecasDeXadrez;
import xadrez.PosicaoXadrez;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        PartidaDeXadrez partidaDeXadrez = new PartidaDeXadrez();
        List<PecasDeXadrez> capturada = new ArrayList<>();

        while (!partidaDeXadrez.getCheckMate()) {
            try {
                UI.limpartTela();
                UI.mostraPartida(partidaDeXadrez, capturada);
                System.out.println();
                System.out.print("Origem: ");
                PosicaoXadrez origem = UI.lerPosicaoXadrez(sc);

                boolean[][] movimentoPossivel = partidaDeXadrez.movimentoPossivel(origem);
                UI.limpartTela();
                UI.mostraTabuleiro(partidaDeXadrez.getPecas(), movimentoPossivel);

                System.out.println();
                System.out.println();
                System.out.println();
                System.out.print("Destino: ");
                PosicaoXadrez destino = UI.lerPosicaoXadrez(sc);

                PecasDeXadrez pecaCapturada = partidaDeXadrez.executarJogadaDeXadrez(origem, destino);

                if (pecaCapturada != null){
                    capturada.add(pecaCapturada);
                }

            }
            catch (ExceptionXadrez e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }
            catch (InputMismatchException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }

        }
        UI.limpartTela();
        UI.mostraPartida(partidaDeXadrez, capturada);
    }
}