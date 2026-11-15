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
                    cadastrar(s, robos);
                    break;
                case 2:
                    consultar(s, robos);
                    break;
                case 3:
                    iniciarCombate(s, robos);
                    break;
                case 4:
                    recuperarEnergia(s, robos);
                    break;
                case 5:
                    rodadaGeral(robos);
                    break;
                case 6:
                    classificacaoGeral(robos);
                    break;
                case 7:
                    estatisticas(robos);
                    break;
                case 8:
                    excluirRobo(s, robos);
                    break;
                case 9:
                    listar(robos);
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
                System.out.println("Digite novamente a informação!");
                opt = -1;
            }
        } while (opt < 0);
        s.nextLine();

        return opt;
    }

    private static int lerInt(Scanner s, String mensagem) {
        int n = 0;
        boolean valido = false;
        do {
            System.out.print(mensagem);
            try {
                n = s.nextInt();
                s.nextLine();
                valido = true;
            } catch (InputMismatchException e) {
                s.next();
                System.out.println("Entrada Inválida. Por favor digite um número inteiro.");
            }
        } while (!valido);
        return n;
    }

    private static Robo buscar(ArrayList<Robo> robos, int codigo) {
        for (Robo r : robos) {
            if (r.getCodigo() == codigo)
                return r;
        }
        return null;
    }

    private static void cadastrar(Scanner s, ArrayList<Robo> robos) {
        int codigo = lerInt(s, "Código: ");
        if (buscar(robos, codigo) != null) {
            System.out.println("Já existe um Robô com esse código!");
            return;
        }
        System.out.print("Nome: ");
        String nome = s.nextLine();
        int tipo = lerTipo(s);
        int ataque = lerInt(s, "Ataque (10 a 30): ");
        int defesa = lerInt(s, "Defesa (0 a 20): ");
        try {
            Robo novo;
            if (tipo == 1)
                novo = new RoboOfensivo(codigo, nome, ataque, defesa);
            else if (tipo == 2)
                novo = new RoboDefensivo(codigo, nome, ataque, defesa);
            else
                novo = new RoboEquilibrado(codigo, nome, ataque, defesa);
            robos.add(novo);
            System.out.println("Robô cadastrado com sucesso!!");
        } catch (IllegalArgumentException e) {
            System.out.println("Cadastro recusado: " + e.getMessage());
        }
    }

    private static void consultar(Scanner s, ArrayList<Robo> robos) {
        Robo r = buscar(robos, lerInt(s, "Código do Robô: "));
        if (r == null)
            System.out.println("Robô não Encontrado.");
        else
            r.exibirDados();
    }

    private static void listar(ArrayList<Robo> robos) {
        if (robos.isEmpty())
            System.out.println("Nenhum robô cadastrado.");
        for (Robo r : robos)
            r.exibirDados();
    }

    private static void iniciarCombate(Scanner s, ArrayList<Robo> robos) {
        int c1 = lerInt(s, "Código do primeiro Robô: ");
        int c2 = lerInt(s, "Código do segundo Robô: ");
        Robo r1 = buscar(robos, c1);
        Robo r2 = buscar(robos, c2);
        if (c1 == c2)
            System.out.println("Os Robôs devem ser diferentes.");
        else if (r1 == null || r2 == null)
            System.out.println("Pelo menos um dos códigos não existe.");
        else if (!r1.podeLutar() || !r2.podeLutar())
            System.out.println("Ambos precisam ter pelo menos 30 de energia.");
        else
            new Combate(r1, r2).executar();
    }

    private static void recuperarEnergia(Scanner s, ArrayList<Robo> robos) {
        Robo r = buscar(robos, lerInt(s, "Código do Robô: "));
        if (r == null) {
            System.out.println("Robô não Encontrado.");
            return;
        }
        int qtd = lerInt(s, "Energia desejada (múltiplo de 10): ");
        if (r.recuperarEnergia(qtd)) {
            System.out.println("Recuperação aceita! " + r.getNome() + " agora tem energia "
                    + r.getEnergiaAtual() + " e " + r.getPontos() + " ponto(s).");
        } else {
            System.out.println("Recusado: a quantidade deve ser positiva e múltipla de 10, "
                    + "custa 1 ponto a cada 10 (você tem " + r.getPontos() + ") "
                    + "e a energia final não pode passar de 100.");
        }
    }

    private static void rodadaGeral(ArrayList<Robo> robos) {
        ArrayList<Robo> disp = new ArrayList<>();
        for (Robo r : classificar(robos)) {
            if (r.podeLutar())
                disp.add(r);
        }
        if (disp.size() < 2) {
            System.out.println("Recusado: menos de 2 robôs disponíveis.");
            return;
        }
        for (int i = 0; i + 1 < disp.size(); i += 2) {
            new Combate(disp.get(i), disp.get(i + 1)).executar();
        }
        if (disp.size() % 2 == 1) {
            Robo folga = disp.get(disp.size() - 1);
            folga.registrarFolga();
            System.out.println("\n" + folga.getNome() + " ficou de folga e ganhou 1 ponto.");
        }
    }

    private static boolean vemAntes(Robo a, Robo b) {
        if (a.getPontos() != b.getPontos())
            return a.getPontos() > b.getPontos();
        if (a.getVitorias() != b.getVitorias())
            return a.getVitorias() > b.getVitorias();
        if (a.getEnergiaAtual() != b.getEnergiaAtual())
            return a.getEnergiaAtual() > b.getEnergiaAtual();
        return a.getCodigo() < b.getCodigo();
    }

    private static ArrayList<Robo> classificar(ArrayList<Robo> robos) {
        ArrayList<Robo> c = new ArrayList<>(robos);
        for (int i = 0; i < c.size() - 1; i++) {
            int melhor = i;
            for (int j = i + 1; j < c.size(); j++) {
                if (vemAntes(c.get(j), c.get(melhor)))
                    melhor = j;
            }
            Robo temp = c.get(i);
            c.set(i, c.get(melhor));
            c.set(melhor, temp);
        }
        return c;
    }

    private static void classificacaoGeral(ArrayList<Robo> robos) {
        if (robos.isEmpty()) {
            System.out.println("Nenhum robô cadastrado.");
            return;
        }
        ArrayList<Robo> t = classificar(robos);
        System.out.println("Classificação Geral");
        for (int i = 0; i < t.size(); i++) {
            Robo r = t.get(i);
            System.out.println((i + 1) + "º " + r.getNome() + " (cód " + r.getCodigo() + ", " + r.getTipo()
                    + ") | Pts: " + r.getPontos()
                    + " | Vit: " + r.getVitorias() + " | Energia: " + r.getEnergiaAtual() + " | " + r.getSituacao());
        }
    }

    private static void estatisticas(ArrayList<Robo> robos) {
        if (robos.isEmpty()) {
            System.out.println("Nenhum robô cadastrado.");
            return;
        }
        int soma = 0;
        Robo ref = null;
        System.out.println("Robôs cadastrados: " + robos.size());
        System.out.println("Em recuperação:");
        boolean algum = false;
        for (Robo r : robos) {
            soma += r.getEnergiaAtual();
            if (!r.podeLutar()) {
                System.out.println("  - " + r.getNome() + " (energia " + r.getEnergiaAtual() + ")");
                algum = true;
            }
            if (r.getTotalCombates() > 0 && (ref == null
                    || r.getVitorias() * ref.getTotalCombates() > ref.getVitorias() * r.getTotalCombates())) {
                ref = r;
            }
        }
        if (!algum)
            System.out.println("  Nenhum.");
        System.out.printf("Média de energia: %.2f%n", (double) soma / robos.size());

        if (ref == null) {
            System.out.println("Maior aproveitamento: nenhum robô lutou ainda.");
            return;
        }
        System.out.printf("Maior aproveitamento (%.1f%%):%n", ref.getAproveitamento());
        for (Robo r : robos) {
            if (r.getTotalCombates() > 0
                    && r.getVitorias() * ref.getTotalCombates() == ref.getVitorias() * r.getTotalCombates()) {
                System.out.println("  - " + r.getNome());
            }
        }
    }

    private static void excluirRobo(Scanner s, ArrayList<Robo> robos) {
        Robo r = buscar(robos, lerInt(s, "Código do Robô: "));
        if (r == null)
            System.out.println("Robô não Encontrado.");
        else if (r.getTotalCombates() > 0)
            System.out.println("Recusado: robôs que já lutaram não podem ser excluídos.");
        else {
            robos.remove(r);
            System.out.println("Robô excluído com sucesso.");
        }
    }

    private static int lerTipo(Scanner s) {
        int t;
        do {
            System.out.println("Tipo do Robô: [1] Ofensivo  [2] Defensivo  [3] Equilibrado");
            t = lerInt(s, "Tipo: ");
            if (t < 1 || t > 3)
                System.out.println("Tipo inválido. Escolha 1, 2 ou 3.");
        } while (t < 1 || t > 3);
        return t;
    }
}