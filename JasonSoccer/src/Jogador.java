

import br.ufrgs.f180.math.Point;

/**
 * Classe que modela um jogador
 * @author Caio Nishibe
 * @author Renato Gasoto
 */
public class Jogador {

    /**
     * Identificador do jogador
     */
    private String nome;
    /**
     * Posição em que se deseja ir
     */
    private Point posicaoDesejada;
    /**
     * Angulo que se deseja virar
     */
    private double anguloDesejado;
    /**
     * Erro de deslocamento no eixo X
     */
    private double erroXAnterior;
    /**
     * Erro integral em x
     */
    private double iXAnt;
    /**
     * Erro de deslocamento no eixo Y
     */
    private double erroYAnterior;
    /**
     * Erro integral em y
     */
    private double iYAnt;
    /**
     * Velocidade no eixo X
     */
    private double vXAnterior;
    /**
     * Velocidade no eixo Y
     */
    private double vYAnterior;
    /**
     * Erro angular anterior
     */
    private double erroAnguloAnterior;
    /**
     * Erro integral angular anterior
     */
    private double iAngAnt;


    /**
     * Construtora da classe
     * @param x posição X desejada
     * @param y posição Y desejada
     */
    public Jogador(double x, double y)
    {
        this.erroXAnterior = 0;
        this.erroYAnterior = 0;
        this.iXAnt = 0;
        this.erroYAnterior = 0;
        this.iYAnt = 0;
        this.vXAnterior = 0;
        this.vYAnterior = 10;
        this.erroAnguloAnterior = 0;
        this.iAngAnt = 0;
        this.posicaoDesejada = new Point(x,y);

    }


    public double getiAngAnt() {
        return iAngAnt;
    }

    public void setiAngAnt(double iAngAnt) {
        this.iAngAnt = iAngAnt;
    }

    public double getiXAnt() {
        return iXAnt;
    }

    public void setiXAnt(double iXAnt) {
        this.iXAnt = iXAnt;
    }

    public double getiYAnt() {
        return iYAnt;
    }

    public void setiYAnt(double iYAnt) {
        this.iYAnt = iYAnt;
    }
    private double vAngAnterior;

    public double getvAngAnterior() {
        return vAngAnterior;
    }

    public void setvAngAnterior(double vAngAnterior) {
        this.vAngAnterior = vAngAnterior;
    }

    public double getvXAnterior() {
        return vXAnterior;
    }

    public void setvXAnterior(double vXAnterior) {
        this.vXAnterior = vXAnterior;
    }

    public double getvYAnterior() {
        return vYAnterior;
    }

    public void setvYAnterior(double vYAnterior) {
        this.vYAnterior = vYAnterior;
    }



    public double getAngulo() {
        return anguloDesejado;
    }

    public void setAngulo(double angulo) {
        this.anguloDesejado = angulo;
    }

    public double getXErro() {
        return erroXAnterior;
    }

    public void setXErro(double erro) {
        this.erroXAnterior = erro;
    }
  public double getYErro() {
        return erroYAnterior;
    }

    public void setYErro(double erro) {
        this.erroYAnterior = erro;
    }
    public double getErroAngulo() {
        return erroAnguloAnterior;
    }

    public void setErroAngulo(double erroAngulo) {
        this.erroAnguloAnterior = erroAngulo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Point getPosicaoDesejada() {
        return posicaoDesejada;
    }

    public void setPosicaoDesejada(Point posicaoDesejada) {
        this.posicaoDesejada = posicaoDesejada;
    }



}
