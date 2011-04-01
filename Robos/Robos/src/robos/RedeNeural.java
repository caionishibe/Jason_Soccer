package robos;

import org.neuroph.core.NeuralNetwork;

/**
 * Classe que implementa os métodos de consulta a rede neural
 * @author Caio Nishibe
 * @author Renato Gasoto
 */
public class RedeNeural {

    /**
     * Rede neural
     */
    private NeuralNetwork nn = null;
    /**
     * Nome do arquivo da rede treinada
     */
    public static final String CHUTE = "myPerceptron.nnet";

    /**
     * Método que define qual rede será utilizada
     * @param arquivo nome do arquivo em que se econtra a rede treinada
     */
    public void setSavedNeural(String arquivo) {
        // carrega a rede previamente treinada
        nn = NeuralNetwork.load(arquivo);

    }

    /**
     * Método que consulta a rede neural a fim de saber se o robo deve ou não chutar
     * Rede neural:
     * - entradas: PosJogX e PosJogY
     * - saída : chuto ou não chuto?
     *
     * @param posX posicao X do jogador
     * @param poxY posicao Y do jogador
     *
     * @return Valor entre 0 e 1 que indica a probabilidade do chute
     */
    public double calculaChute(double posX, double poxY) {
        nn.setInput(new double[]{posX, poxY});
        nn.calculate();
        System.out.println(nn.getOutput().get(0));

        return nn.getOutput().get(0);
    }

    /**
     * Método que ajusta as funções de pertinência (NÃO IMPLEMENTADO!)
     * @param isGol
     * @param posYGoleiro
     * @param posYChute
     * @param posYJog
     */
    public void ajustaFuzzy(boolean isGol, double posYGoleiro, double posYChute, double posYJog) {
        double gol = Double.MIN_VALUE;

        if (isGol) {
            gol = 1.0;
        } else {
            gol = 0.0;
        }

        nn.setInput(new double[]{gol, posYGoleiro, posYChute, posYJog});
        nn.calculate();

        double ajuste = nn.getOutput().get(0);
    }
}
