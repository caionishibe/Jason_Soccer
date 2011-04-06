// Agente: Atacante

/* Initial beliefs and rules */

posicao(5, 8).
time(team_a).



/* Initial goals */

// Objetivo inicial: entrar em campo
!entrarEmCampo.

/* Plans */

// ao entrar em campo, obter a posicao inicial do jogador em campo e o time,
// criar o jogador no tewnta e comecar a rastrear a bola.
+!entrarEmCampo : true
    <-  ?posicao(X,Y); ?time(Z);
       createPlayer(X, Y, Z);
       !iniciaAtaque.
	   
//Plano que contem todos os subplanos executados para que a estrategia de ataque
//seja cumprida. Inicialmente existe apenas dois subplanos possiveis: olheBola e
//buscaBola. Porem serao adicionados durante o decorrer do trabalho os demais subplanos
//como passe, chute etc.
+!iniciaAtaque : true
	<- !buscaBola; 
	   !olheBola;
	   !iniciaAtaque.
	
	
//Resgate a posicao da bola (percepcao) e rotacione olhando pra bola
+!olheBola : true
	<- ?posBola(X,Y);
		rotacioneParaBola(X,Y).
		
		
//Vai em direcao a posicao da bola.
+!buscaBola : true
	<- ?posBola(X,Y);
		irLinhaReta(X,Y).
		





