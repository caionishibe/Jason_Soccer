package robos;

import net.sourceforge.jFuzzyLogic.FIS;

/**
 *
 * @author Caio Nishibe
 */
public class Fuzzy {

    private static final String POS_CHUTE_FILE = "posicaoDeChute.fcl";
    private static final String POS_X_Y_FILE = "controladorXY.fcl";
    private FIS fisChute;
    private FIS fisXY;

    public Fuzzy() {

        fisChute = FIS.load(POS_CHUTE_FILE);
        fisXY =FIS.load(POS_X_Y_FILE);
        if (fisChute == null || fisXY.getFunctionBlock("pos_x") == null||fisXY.getFunctionBlock("pos_y") == null) {
            System.out.println("Can't load file");
            System.exit(1);
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

    public double getPosX(double posBolaX, double posJogX)
    {
        double posX = 0.0;

        if (fisXY.getFunctionBlock("pos_x") != null) {

            fisXY.getFunctionBlock("pos_x").setVariable("pos_bola_x", posBolaX);
            fisXY.getFunctionBlock("pos_x").setVariable("pos_jogador_x", posJogX);
            fisXY.getFunctionBlock("pos_x").evaluate();

            posX = fisXY.getFunctionBlock("pos_x").getVariable("pos_x").defuzzify();
            System.out.println("Vá para X : "+posX);
        }

        
        return posX;
    }

      public double getPosY(double posBolaY, double posJogY)
    {
        double posY = 0.0;

        if (fisXY.getFunctionBlock("pos_y") != null) {

            fisXY.getFunctionBlock("pos_y").setVariable("pos_bola_y", posBolaY);
            fisXY.getFunctionBlock("pos_y").setVariable("pos_jogador_y", posJogY);
            fisXY.getFunctionBlock("pos_y").evaluate();

            posY = fisXY.getFunctionBlock("pos_y").getVariable("pos_y").defuzzify();
            System.out.println("Vá para Y : "+posY);
        }


        return posY;
    }

    
}
