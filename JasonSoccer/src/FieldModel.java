
import br.ufrgs.f180.math.Point;
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

/**
 * Classe que implementa o modelo do campo de futebol de robos
 * @author Caio Nishibe
 * @author Luís Mendes
 */
public class FieldModel extends GridWorldModel {

    //constantes para o tamanho do grid
    public static final int GRID_WIDTH = 25;
    public static final int GRID_HEIGHT = 17;
    //constantes  para o tamanho real das celulas do grid
    public static final double CELL_WIDTH = 19.6;
    public static final double CELL_HEIGHT = 19.88;
    //constante para o objeto bola no grid
    public static final int BALL = 16;
    //localização da bola
    Location lBall = null;

    public FieldModel(int xBola, int yBola, int numberAgents) {
        //cria um grid com numberAgents agentes
        super(GRID_WIDTH, GRID_HEIGHT, numberAgents);

        this.lBall = new Location(xBola, yBola);
    }

    /**
     * Método que atualiza a posicao da bola no grid
     * @param x posicao X no grid
     * @param y posicao Y no grid
     */
    public void updateBallLocation(int x, int y) {
        this.lBall = new Location(x, y);

    }

    public Point toTewntaPosition(int x, int y) {
        Point tewntaPosition = null;

        double xNew = 30 + (x * FieldModel.CELL_WIDTH) + (FieldModel.CELL_WIDTH / 2);
        double yNew = 30 + (y * FieldModel.CELL_HEIGHT) + (FieldModel.CELL_HEIGHT / 2);

        tewntaPosition = new Point(xNew, yNew);

        return tewntaPosition;
    }

    public int[] toJasonPosition(Point p)
    {
        int jasonPosition[] = new int[2];

        double xNew = (p.getX() - 30)/FieldModel.CELL_WIDTH;
        double yNew = (p.getY() - 30)/FieldModel.CELL_HEIGHT;

        jasonPosition[0] = (int)xNew;
        jasonPosition[1] = (int)yNew;
        
        return jasonPosition;
    }
}
