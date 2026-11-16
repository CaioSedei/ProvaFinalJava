
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        ArrayList<Robo> robos = new ArrayList<>();
        robos.add(new RoboOfensivo(1, "Pearl Jam", 30, 18));
        robos.add(new RoboDefensivo(2, "The doors", 30, 20));
        robos.add(new RoboEquilibrado(3, "Led Zepellin", 28, 15));
        robos.add(new RoboOfensivo(4, "Pink Floyd", 25, 10));
        robos.add(new RoboDefensivo(5, "Alice in chain", 20, 20));

        int opt = -1;
        do {
            System.out.println("==== Rinha de robos ====");
            System.out.println("0. Sair");
            System.out.println("1. Cadastrar robô");
            System.out.println("2. Consultar robô");
            System.out.println("3. Realizar um combate");
            System.out.println("4. Recuperar energia");
            System.out.println("5. Executar uma rodada geral");
            System.out.println("6. Exibir a classificação");
            System.out.println("7. Emitir estatísticas");
            System.out.println("8. Excluir participante");
            System.out.println("9. Listar robôs");
            System.out.println("Digite a operação: ");
            opt = buscarOperacao(s);
            switch (opt) {
                case 0:
                    System.out.println("Adeus!");
                    break;
                case 1:
                    Campeonato.cadastrar(s, robos);
                    break;
                case 2:
                    Campeonato.consultar(s, robos);
                    break;
                case 3:
                    Campeonato.iniciarCombate(s, robos);
                    break;
                case 4:
                    Campeonato.recuperarEnergia(s, robos);
                    break;
                case 5:
                    Campeonato.rodadaGeral(robos);
                    break;
                case 6:
                    Campeonato.classificacaoGeral(robos);
                    break;
                case 7:
                    Campeonato.estatisticas(robos);
                    break;
                case 8:
                    Campeonato.excluirRobo(s, robos);
                    break;
                case 9:
                    Campeonato.listar(robos);
                    break;
            }

        } while (opt != 0);

        s.close();
    }

    public static int buscarOperacao(Scanner s) {
        int opt = -1;
        do {
            try {
                opt = s.nextInt();
            } catch (InputMismatchException e) {
                s.next();
                System.out.println("Operação inválida");
                opt = -1;
            }
        } while (opt < 0);
        s.nextLine();

        return opt;
    }
}