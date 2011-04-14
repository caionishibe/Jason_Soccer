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
	   
//caso o atacante seja o jogador mais proximo da bola e ela n�o 
//estiver sob a posse de nenhum outro jogador, ent�o domine a bola
+!atacar: naoDominada(bola) & maisPerto(bola)
	<-	!!posChute; 
		?posBola(X,Y);
		irLinhaReta(X,Y);
		!atacar.
		

//se com a bola e perto do gol, chute
+!atacar: com(bola) & perto(gol)	
	<- 	!melhorPosChute; 
		chutar;
		!atacar.

//caso contr�rio reposiciona
+!atacar: true
	<- 	!!olheBola; 
		?posicaoIni(X,Y);
		irLinhaReta(X,Y);
		!atacar.
	   
//Resgate a posicao da bola (percepcao) e rotacione olhando pra bola
+!olheBola : true
	<- 	?posBola(X,Y);
		rotacioneParaBola(X,Y).
		
//decide melhor ponto do gol para chutar utilizando Fuzzy.
+!melhorPosChute : true
	<- posicaoChute.
	
+!estrategiaAtaque : not com(bola) & not perto(gol)
	<-	!!olhaBola;	
		posicionaAtaque.





