

import net.sourceforge.jFuzzyLogic.FIS;

/**
 *
 * @author Caio Nishibe
 */
public class Fuzzy {

    private static final String POS_CHUTE_FILE = "posicaoDeChute.fcl";
    private FIS fisChute;


    public Fuzzy() {

        fisChute = FIS.load(POS_CHUTE_FILE);
        
        if (fisChute == null ) {
            SoccerEnv.logger.info("Can't load file");
            
        } else {
            //fisChute.chart();
            //fisXY.getFunctionBlock("pos_x").chart();

            //fisXY.getFunctionBlock("pos_y").chart();
        }



    }

    public double getPosChute(double posGoleiro, double posJogador) {
        double posChute = 0.0;

        if (fisChute != null) {


            fisChute.setVariable("pos_goleiro", posGoleiro);
            fisChute.setVariable("pos_jogador", posJogador);
            fisChute.evaluate();

            posChute = fisChute.getVariable("pos_chute").defuzzify();
            System.out.println("Chute em : "+posChute);
        }


        return posChute;
    }

    

    
}
