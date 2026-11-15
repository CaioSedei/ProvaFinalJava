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

    public int getAtaque() {
        return ataque;
    }

    public int getDefesa() {
        return defesa;
    }

    public int getVitorias() {
        return vitorias;
    }

    public int getDerrotas() {
        return derrotas;
    }

    public int getEnergiaAtual() {
        return energiaAtual;
    }

    public int getPontos() {
        return pontos;
    }

    public int getTotalCombates() {
        return totalCombates;
    }

    public boolean estaDestruido() {
        return energiaAtual <= 0;
    }

    public boolean podeLutar() {
        return energiaAtual >= 30;
    }

    public String getSituacao() {
        if (energiaAtual >= 30) {
            return "Disponível";
        }
        return "Em Recuperação";
    }

    public int calcularDano(Robo adversario, int rodada) {
        int dano = (ataque + getBonusAtaque()) - adversario.defesa;
        if (dano < 5) {
            dano = 5;
        }
        if (rodada % 2 == 0) {
            dano += 5;
        }
        return adversario.ajustarDanoRecebido(dano);
    }

    public void receberDano(int dano) {
        if (dano <= 0) {
            return;
        }
        energiaAtual -= dano;
        if (energiaAtual < 0) {
            energiaAtual = 0;
        }
    }

    public void registrarVitoria() {
        vitorias++;
        pontos += 3;
        totalCombates++;
    }

    public void registrarDerrota() {
        derrotas++;
        totalCombates++;
    }

    public void registrarEmpate() {
        pontos += 1;
        totalCombates++;
    }

    public void registrarFolga() {
        pontos += 1;
    }

    public boolean recuperarEnergia(int quantidade) {
        if (quantidade <= 0 || quantidade % 10 != 0) {
            return false;
        }
        int custo = quantidade / 10;
        if (custo > pontos || energiaAtual + quantidade > 100) {
            return false;
        }
        pontos -= custo;
        energiaAtual += quantidade;
        return true;
    }

    public double getAproveitamento() {
        if (totalCombates == 0) {
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

    public int getBonusAtaque() {
        return 0;
    }

    public int ajustarDanoRecebido(int dano) {
        return dano;
    }
}