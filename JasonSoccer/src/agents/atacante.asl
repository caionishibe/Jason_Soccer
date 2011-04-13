// Agente: Atacante

/* Initial goals */

// Objetivo inicial: entrar em campo
!entrarEmCampo.

/* Plans */

// ao entrar em campo, obter a posicao inicial do jogador em campo e o time,
// criar o jogador no tewnta e comecar a rastrear a bola.
+!entrarEmCampo : true
    <-  ?posicaoIni(X,Y); ?time(Z);
       createPlayer(X, Y, Z);
       !atacar.
	   
//caso o atacante seja o jogador mais proximo da bola e ela não 
//estiver sob a posse de nenhum outro jogador, então domine a bola
+!atacar: naoDominada(bola) & maisPerto(bola)
	<- !!olheBola; 
	?posBola(X,Y);
	irLinhaReta(X,Y);
	!atacar.
	   

	
//Resgate a posicao da bola (percepcao) e rotacione olhando pra bola
+!olheBola : true
	<- ?posBola(X,Y);
		rotacioneParaBola(X,Y).
		
		

		





