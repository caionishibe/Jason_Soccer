package robos;

import br.ufrgs.f180.api.Player;
import br.ufrgs.f180.math.Point;

/**
 * Classe que monitora o evento do gol
 * @author Caio Nishibe
 * @author Renato Gasoto
 */
public class MonitoraGol extends Thread {

    /**
     * Cliente do simulador. Através deste pode-se acessar as informações do jogo
     */
    private Player cliente;
    /**
     * Booleano que indica a ocorrência ou não de gol
     */
    private boolean gol = false;

    /**
     * Construtora da classe
     * @param cliente Cliente do simulador
     */
    public MonitoraGol(Player cliente) {
        this.cliente = cliente;
    }

    /**
     * Método run da thread que monitora a ocorrência do gol
     */
    @Override
    public void run() {


        while (true) {

            Point posBola = cliente.getBallInformation().getPosition();
  gol = true;
                
            //se a bola está no gol
            if (posBola.getX() < 29.0
                    && posBola.getY() > 165.0 && posBola.getY() < 235.0) {
                //gol é verdadeiro
              
            } 

        }


    }

    /**
     * Getter do atributo gol
     * @return <code>true</code> se ocorreu o gol e <code>false</code> caso contrário
     */
    public boolean isGol() {
        return gol;
    }

    /**
     * Setter do atributo gol
     */
    public void setGolFalse()
    {
        this.gol = false;
    }
}
