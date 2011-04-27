// Agente: Goleiro

/* Initial goal */

// Objetivo inicial: entrar em campo 
!entrarEmCampo.

/* Plans */

// Ao entrar em campo, obter a posicao inicial do jogador em campo,
// criar o jogador no tewnta e iniciar a defesa.
+!entrarEmCampo : true
    <- ?posicaoIni(X, Y); ?time(Z);
       createPlayer(X, Y,Z);
	   !defender.


//caso o goleiro esteja com a bola, chutar
+!defender : com(bola)
	<- chutar;
	!defender.


//caso contrário, defenda o gol
+!defender : true
	<- 	?posBola(XBola,YBola);
		?posicao(XGoleiro, YGoleiro);
		defender(XBola, YBola, XGoleiro, YGoleiro); 
		!defender.
	   
	   
	

		
		