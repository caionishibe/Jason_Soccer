//Atacante central

/* Initial beliefs and rules */

posicaoIni(240, 200).
time(team_a).

//Inclui os comportamentos de um atacante
{ include("atacante.asl") }

//se com a bola e longe do gol, diga aos companheiros para se aproximarem do gol
+!atacar: com(bola) & not perto(gol)
	<- 	!!olheBola;		
		.send(atacanteDireita,achieve, estraegiaAtaque);
		!atacar.
