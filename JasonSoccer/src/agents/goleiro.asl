// Agente: Goleiro

/* Initial beliefs and rules */

posicao(505, 200).
time(team_b).


/* Initial goal */

// Objetivo inicial: entrar em campo 
!entrarEmCampo.

/* Plans */

// Ao entrar em campo, obter a posicao inicial do jogador em campo,
// criar o jogador no tewnta e iniciar a defesa.
+!entrarEmCampo : true
    <- ?posicao(X, Y); ?time(Z);
       createPlayer(X, Y,Z);
	   !defender.
	   
//Após criado o goleiro, inicia-se o plano de defesa.

//caso o goleiro esteja com a bola, chutar
+!defender : com(bola)
	<- chutar(100);
	!defender.

//caso contrário
+!defender : true
	<- 	?posBola(XBola,YBola);
		?posicao(XGoleiro, YGoleiro);
		!!olheBola;
		defender(XBola, YBola, XGoleiro, YGoleiro); 
		!defender.
	   
	   
//Resgate a posicao da bola (percepcao) e rotacione olhando pra bola
+!olheBola : true
	<- ?posBola(X,Y);
		rotacionePara(X,Y).
		

		
		