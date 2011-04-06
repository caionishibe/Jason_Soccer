// Agente: Goleiro

/* Initial beliefs and rules */

posicaoInicial(24, 8).
time(team_b).

/* Initial goal */

// Objetivo inicial: entrar em campo 
!entrarEmCampo.

/* Plans */

// Ao entrar em campo, obter a posicao inicial do jogador em campo,
// criar o jogador no tewnta e iniciar a defesa.
+!entrarEmCampo : true
    <- ?posicaoInicial(X, Y); ?time(Z);
       createPlayer(X, Y,Z);
	   !iniciaDefesa.
	   
//Após criado o goleiro, inicia-se o plano de defesa.
//Dentro deste plano estarao os subplanos que serao executados, por enquanto
//existe apenas o olheBola, porém existirá o recua, avanca, defende etc.
+!iniciaDefesa : true
	<- !olheBola;
		!iniciaDefesa.
	   
	   
//Resgate a posicao da bola (percepcao) e rotacione olhando pra bola
+!olheBola : true
	<- ?posBola(X,Y);
		rotacioneParaBola(X,Y).
		
		