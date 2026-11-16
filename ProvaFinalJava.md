# ProvaFinalJava

Projeto **MedaBots / Rinha de Robôs** em Java, com o código completo e a explicação de onde estão **herança**, **polimorfismo** e **encapsulamento**.

Como rodar: coloque os seis arquivos `.java` na mesma pasta, compile com `javac *.java` e execute com `java Main`.

```
Robo  (abstract)
 ├── RoboOfensivo
 ├── RoboDefensivo
 └── RoboEquilibrado

Combate   → duas referências Robo
Main      → menu (estilo ProvaJava) e ArrayList<Robo>
Campeonato → métodos que executam cada opção do menu
```

---

## 1. Robo.java (superclasse abstrata)

```java
public abstract class Robo {
    private int codigo;
    private String nome;
    private int ataque;
    private int defesa;
    private int energiaAtual;
    private int vitorias;
    private int derrotas;
    private int pontos;
    private int totalCombates;

    public Robo(int codigo, String nome, int ataque, int defesa) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("Código deve ser positivo.");
        }
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }
        if (ataque < 10 || ataque > 30) {
            throw new IllegalArgumentException("Ataque deve estar entre 10 e 30.");
        }
        if (defesa < 0 || defesa > 20) {
            throw new IllegalArgumentException("Defesa deve estar entre 0 e 20.");
        }
        this.codigo = codigo;
        this.nome = nome.trim();
        this.ataque = ataque;
        this.defesa = defesa;
        this.energiaAtual = 100;
        this.vitorias = 0;
        this.derrotas = 0;
        this.pontos = 0;
        this.totalCombates = 0;
    }

    public int getCodigo() {
        return codigo;
    }
    public String getNome() {
        return nome;
    }
    public int getAtaque(){
        return ataque;
    }
    public int getDefesa(){
        return defesa;
    }
    public int getVitorias(){
        return vitorias;
    }
    public int getDerrotas(){
        return derrotas;
    }
    public int getEnergiaAtual(){
        return energiaAtual;
    }
    public int getPontos(){
        return pontos;
    }
    public int getTotalCombates(){
        return totalCombates;
    }

    public boolean estaDestruido(){
        return energiaAtual <= 0;
    }
    public boolean podeLutar(){
        return energiaAtual >= 30;
    }
    public String getSituacao(){
        if(energiaAtual >= 30){
            return "Disponível";
        }
        return "Em Recuperação";
    }

    public int calcularDano(Robo adversario, int rodada){
        int dano = (ataque + getBonusAtaque()) - adversario.defesa;
        if (dano < 5){
            dano = 5;
        }
        if (rodada % 2 == 0) {
            dano += 5;
        }
        return adversario.ajustarDanoRecebido(dano);
    }
    public void receberDano(int dano){
        if (dano <= 0){
            return;
        }
        energiaAtual -= dano;
        if (energiaAtual < 0){
            energiaAtual = 0;
        }
    }

    public void registrarVitoria(){
        vitorias++;
        pontos += 3;
        totalCombates++;
    }
    public void registrarDerrota(){
        derrotas++;
        totalCombates++;
    }
    public void registrarEmpate(){
        pontos += 1;
        totalCombates++;
    }
    public void registrarFolga(){
        pontos += 1;
    }

    public boolean recuperarEnergia(int quantidade){
        if (quantidade <= 0 || quantidade % 10 != 0){
            return false;
        }
        int custo = quantidade / 10;
        if (custo > pontos || energiaAtual + quantidade > 100){
            return false;
        }
        pontos -= custo;
        energiaAtual += quantidade;
        return true;
    }

    public double getAproveitamento(){
        if (totalCombates == 0){
            return 0;
        }
        return (vitorias * 100.0) / totalCombates;
    }

    public void exibirDados() {
        System.out.println("Código: " + codigo);
        System.out.println("Nome: " + nome);
        System.out.println("Ataque: " + ataque);
        System.out.println("Tipo: " + getTipo());
        System.out.println("Defesa: " + defesa);
        System.out.println("Energia Atual: " + energiaAtual);
        System.out.println("Vitórias: " + vitorias);
        System.out.println("Derrotas: " + derrotas);
        System.out.println("Pontos: " + pontos);
        System.out.println("Situação: " + getSituacao());
        System.out.println("Total de Combates: " + totalCombates);
        System.out.println("============================");
    }
    public abstract String getTipo();
    public int getBonusAtaque(){
        return 0;
    }
    public int ajustarDanoRecebido(int dano){
        return dano;
    }
}
```

**Neste arquivo:**

- **Encapsulamento:** atributos `private` (linhas 2 a 10), só getters e nenhum setter. O construtor (linhas 12 a 25) valida os dados e lança `IllegalArgumentException`. `receberDano`, `registrarVitoria`, `registrarDerrota`, `registrarEmpate`, `registrarFolga` e `recuperarEnergia` são os únicos caminhos para alterar energia, pontos e indicadores, e cada um aplica as regras do campeonato.
- **Herança:** `abstract class Robo` é a classe pai. Não dá para fazer `new Robo()`.
- **Polimorfismo:** `getTipo()` é abstrato (linha 148). `getBonusAtaque()` (linha 149) e `ajustarDanoRecebido()` (linha 152) têm comportamento padrão e podem ser sobrescritos. `calcularDano()` (linhas 77 a 86) chama os dois sem saber o tipo do robô.

---

## 2. RoboOfensivo.java

```java
public class RoboOfensivo extends Robo {
    public RoboOfensivo(int codigo, String nome, int ataque, int defesa) {
        super(codigo, nome, ataque, defesa);
    }

    @Override
    public String getTipo(){
        return "Ofensivo";
    }

    @Override
    public int getBonusAtaque(){
        return 5;
    }
}
```

- **Herança:** `extends Robo` e `super(...)` no construtor.
- **Polimorfismo:** `@Override` de `getTipo()` e de `getBonusAtaque()`, que passa de 0 para 5.

## 3. RoboDefensivo.java

```java
public class RoboDefensivo extends Robo {
    public RoboDefensivo(int codigo, String nome, int ataque, int defesa) {
        super(codigo, nome, ataque, defesa);
    }

    @Override
    public String getTipo(){
        return "Defensivo";
    }

    @Override
    public int ajustarDanoRecebido(int dano){
        int reduzido = dano - 3;
        if (reduzido < 2){
            reduzido = 2;
        }
        return reduzido;
    }
}
```

- **Herança:** `extends Robo` e `super(...)`.
- **Polimorfismo:** `@Override` de `getTipo()` e de `ajustarDanoRecebido()`, que reduz o dano em 3 (mínimo 2).

## 4. RoboEquilibrado.java

```java
public class RoboEquilibrado extends Robo {
    public RoboEquilibrado(int codigo, String nome, int ataque, int defesa) {
        super(codigo, nome, ataque, defesa);
    }

    @Override
    public String getTipo(){
        return "Equilibrado";
    }
}
```

- **Herança:** `extends Robo` e `super(...)`.
- **Polimorfismo:** só sobrescreve `getTipo()`. O resto usa o comportamento padrão de `Robo`.

---

## 5. Combate.java

```java
public class Combate {
    private Robo a;
    private Robo b;

    public Combate(Robo r1, Robo r2) {
        boolean r1Primeiro = r1.getPontos() < r2.getPontos()
                || (r1.getPontos() == r2.getPontos() && r1.getCodigo() < r2.getCodigo());
        this.a = r1Primeiro ? r1 : r2;
        this.b = r1Primeiro ? r2 : r1;
    }

    public void executar() {
        System.out.println("\n===== COMBATE: " + a.getNome() + " (" + a.getTipo() + ") x " + b.getNome() + " (" + b.getTipo() + ") =====");
        System.out.println("Ataca primeiro: " + a.getNome());

        for (int rodada = 1; rodada <= 5 && !a.estaDestruido() && !b.estaDestruido(); rodada++) {
            System.out.println("-- Rodada " + rodada + " --");
            atacar(a, b, rodada);
            if (!b.estaDestruido()) atacar(b, a, rodada);
        }
        int dif = a.getEnergiaAtual() - b.getEnergiaAtual();
        if (dif == 0) {
            a.registrarEmpate();
            b.registrarEmpate();
            System.out.println("RESULTADO: EMPATE! (+1 ponto para cada)");
        } else {
            Robo vencedor = dif > 0 ? a : b;
            Robo perdedor = dif > 0 ? b : a;
            vencedor.registrarVitoria();
            perdedor.registrarDerrota();
            System.out.println("RESULTADO: " + vencedor.getNome() + " venceu! (+3 pontos)");
        }
        System.out.println("Energia final: " + a.getNome() + " = " + a.getEnergiaAtual()
                + " | " + b.getNome() + " = " + b.getEnergiaAtual());
    }

    private void atacar(Robo atacante, Robo defensor, int rodada) {
        int dano = atacante.calcularDano(defensor, rodada);
        defensor.receberDano(dano);
        System.out.println(atacante.getNome() + " ataca " + defensor.getNome() + " | dano: " + dano
                + " | energia de " + defensor.getNome() + ": " + defensor.getEnergiaAtual());
    }
}
```

- **Encapsulamento:** `a` e `b` são `private`. A ordem de ataque é definida no construtor e não muda de fora.
- **Polimorfismo:** trabalha só com `Robo`. Em `atacar`, `atacante.calcularDano(defensor, rodada)` usa o bônus do tipo de quem ataca e a redução do tipo de quem defende, sem nenhum `if` por tipo.

---

## 6. Main.java (menu no estilo ProvaJava)

```java
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
                System.out.println("Digite novamente a informação!");
                opt = -1;
            }
        } while (opt < 0);
        s.nextLine();

        return opt;
    }
}
```

- Só tem o menu, o `switch` e o `buscarOperacao`. Cada opção chama um método da classe `Campeonato`.
- **Polimorfismo:** `ArrayList<Robo>` guarda Ofensivo, Defensivo e Equilibrado juntos.

---

## 7. Campeonato.java (métodos separados da Main)

```java
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Campeonato {
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
            if (r.getCodigo() == codigo) return r;
        }
        return null;
    }

    public static void cadastrar(Scanner s, ArrayList<Robo> robos) {
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
            if (tipo == 1) novo = new RoboOfensivo(codigo, nome, ataque, defesa);
            else if (tipo == 2) novo = new RoboDefensivo(codigo, nome, ataque, defesa);
            else novo = new RoboEquilibrado(codigo, nome, ataque, defesa);
            robos.add(novo);
            System.out.println("Robô cadastrado com sucesso!!");
        } catch (IllegalArgumentException e) {
            System.out.println("Cadastro recusado: " + e.getMessage());
        }
    }

    public static void consultar(Scanner s, ArrayList<Robo> robos) {
        Robo r = buscar(robos, lerInt(s, "Código do Robô: "));
        if (r == null) System.out.println("Robô não Encontrado.");
        else r.exibirDados();
    }

    public static void listar(ArrayList<Robo> robos) {
        if (robos.isEmpty()) System.out.println("Nenhum robô cadastrado.");
        for (Robo r : robos) r.exibirDados();
    }

    public static void iniciarCombate(Scanner s, ArrayList<Robo> robos) {
        int c1 = lerInt(s, "Código do primeiro Robô: ");
        int c2 = lerInt(s, "Código do segundo Robô: ");
        Robo r1 = buscar(robos, c1);
        Robo r2 = buscar(robos, c2);
        if (c1 == c2) System.out.println("Os Robôs devem ser diferentes.");
        else if (r1 == null || r2 == null) System.out.println("Pelo menos um dos códigos não existe.");
        else if (!r1.podeLutar() || !r2.podeLutar()) System.out.println("Ambos precisam ter pelo menos 30 de energia.");
        else new Combate(r1, r2).executar();
    }

    public static void recuperarEnergia(Scanner s, ArrayList<Robo> robos) {
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

    public static void rodadaGeral(ArrayList<Robo> robos) {
        ArrayList<Robo> disp = new ArrayList<>();
        for (Robo r : classificar(robos)) {
            if (r.podeLutar()) disp.add(r);
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
        if (a.getPontos() != b.getPontos()) return a.getPontos() > b.getPontos();
        if (a.getVitorias() != b.getVitorias()) return a.getVitorias() > b.getVitorias();
        if (a.getEnergiaAtual() != b.getEnergiaAtual()) return a.getEnergiaAtual() > b.getEnergiaAtual();
        return a.getCodigo() < b.getCodigo();
    }

    private static ArrayList<Robo> classificar(ArrayList<Robo> robos) {
        ArrayList<Robo> c = new ArrayList<>(robos);
        for (int i = 0; i < c.size() - 1; i++) {
            int melhor = i;
            for (int j = i + 1; j < c.size(); j++) {
                if (vemAntes(c.get(j), c.get(melhor))) melhor = j;
            }
            Robo temp = c.get(i);
            c.set(i, c.get(melhor));
            c.set(melhor, temp);
        }
        return c;
    }

    public static void classificacaoGeral(ArrayList<Robo> robos) {
        if (robos.isEmpty()) {
            System.out.println("Nenhum robô cadastrado.");
            return;
        }
        ArrayList<Robo> t = classificar(robos);
        System.out.println("Classificação Geral");
        for (int i = 0; i < t.size(); i++) {
            Robo r = t.get(i);
            System.out.println((i + 1) + "º " + r.getNome() + " (cód " + r.getCodigo() + ", " + r.getTipo() + ") | Pts: " + r.getPontos()
                    + " | Vit: " + r.getVitorias() + " | Energia: " + r.getEnergiaAtual() + " | " + r.getSituacao());
        }
    }

    public static void estatisticas(ArrayList<Robo> robos) {
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
        if (!algum) System.out.println("  Nenhum.");
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

    public static void excluirRobo(Scanner s, ArrayList<Robo> robos) {
        Robo r = buscar(robos, lerInt(s, "Código do Robô: "));
        if (r == null) System.out.println("Robô não Encontrado.");
        else if (r.getTotalCombates() > 0) System.out.println("Recusado: robôs que já lutaram não podem ser excluídos.");
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
            if (t < 1 || t > 3) System.out.println("Tipo inválido. Escolha 1, 2 ou 3.");
        } while (t < 1 || t > 3);
        return t;
    }
}
```

- **Polimorfismo:** em `cadastrar`, a variável `Robo novo` recebe um dos três tipos. `r.getTipo()` imprime o tipo certo de cada robô.
- **Encapsulamento:** esta classe nunca mexe nos atributos de `Robo`. Só usa getters e métodos como `recuperarEnergia(qtd)`, que devolve `true` ou `false`.

---

## Resumo

| Conceito                             | Onde                                                                                   | Para que serve no projeto                                        |
| ------------------------------------ | -------------------------------------------------------------------------------------- | ---------------------------------------------------------------- |
| **Herança**                          | `RoboOfensivo`, `RoboDefensivo`, `RoboEquilibrado` com `extends Robo` e `super(...)`   | Reaproveitar atributos, validações e métodos de `Robo`           |
| **Classe abstrata**                  | `abstract class Robo` e `abstract String getTipo()`                                    | Impedir `new Robo()` e obrigar cada tipo a se identificar        |
| **Polimorfismo (sobrescrita)**       | `getTipo()`, `getBonusAtaque()`, `ajustarDanoRecebido()` com `@Override`               | Cada tipo de robô se comporta de um jeito                        |
| **Polimorfismo (referência do pai)** | `ArrayList<Robo>`, `Robo novo`, `Combate(Robo, Robo)`                                  | Tratar todos os tipos de forma única, sem `if` por tipo          |
| **Encapsulamento**                   | Atributos `private` + getters + métodos com regras em `Robo`, e `private` em `Combate` | Proteger energia, pontos e indicadores contra alteração indevida |