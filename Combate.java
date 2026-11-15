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
        System.out.println("\n===== COMBATE: " + a.getNome() + " (" + a.getTipo() + ") x " + b.getNome() + " ("
                + b.getTipo() + ") =====");
        System.out.println("Ataca primeiro: " + a.getNome());

        for (int rodada = 1; rodada <= 5 && !a.estaDestruido() && !b.estaDestruido(); rodada++) {
            System.out.println("-- Rodada " + rodada + " --");
            atacar(a, b, rodada);
            if (!b.estaDestruido())
                atacar(b, a, rodada);
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