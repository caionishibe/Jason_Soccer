//Atacante direita

/* Initial beliefs and rules */

posicaoIni(170, 110).
time(team_a).
meuNome(atacanteDireita).

//Inclui os comportamentos de um atacante
{ include("atacante.asl") }
