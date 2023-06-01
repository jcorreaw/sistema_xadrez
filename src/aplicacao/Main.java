package aplicacao;

import xadrez.ExceptionXadrez;
import xadrez.PartidaDeXadrez;
import xadrez.PecasDeXadrez;
import xadrez.PosicaoXadrez;

import java.security.InvalidParameterException;
import java.util.*;

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
                System.out.print("Destino: ");
                PosicaoXadrez destino = UI.lerPosicaoXadrez(sc);

                PecasDeXadrez pecaCapturada = partidaDeXadrez.executarJogadaDeXadrez(origem, destino);

                if (pecaCapturada != null){
                    capturada.add(pecaCapturada);
                }

                if (partidaDeXadrez.getPromocaoDoPeao() != null){
                    System.out.print("Entre com a letra da nova peça para promoção(B, C, R, Q): ");
                    String tipo = sc.nextLine().toUpperCase();
                    while (!tipo.equals("B") && !tipo.equals("C") && !tipo.equals("R") && !tipo.equals("Q")){
                        System.out.print("Valor invalido! Entre com a letra da nova peça para promoção(B, C, R, Q): ");
                        tipo = sc.nextLine().toUpperCase();
                    }
                    partidaDeXadrez.trocaPecaPromocao(tipo);
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