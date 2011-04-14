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
	   
//caso o atacante seja o jogador mais proximo da bola, então domine a bola
+!atacar: maisPerto(bola) & not com(bola)
	<-	!!olheBola;
		?posBola(X,Y);
		irLinhaReta(X,Y);
		!atacar.
	
//se sem bola e longe do gol, vá para perto do gol
+!atacar: not com(bola) & not perto(gol)
	<- 	!!olheBola;
		?posicao(X,Y);
		?posicaoIni(A,B);
		posicionaAtaque(420,B);
		!atacar.
		
//se sem bola e perto do gol, posicao de ataque	
+!atacar: not com(bola) & perto(gol)
	<-  !!olheBola;
		?posicao(X,Y);
		?posicaoIni(A,B);
		posicionaAtaque(420,B);
		!atacar.
		

//se com a bola e perto do gol, ache melhor posicao para chute
+!atacar: com(bola) & perto(gol)	
	<- 	posicaoChute; 
		!atacar.



//se com a bola e longe do gol, passar para o companheiro mais proximo.
+!atacar: com(bola) & not perto(gol)
	<-	//!olharCompanheiro;
		passar;
		!atacar.
	

//caso contrário reposiciona
+!atacar: true
	<- 	//!!olheBola; 
		?posicaoIni(X,Y);
		irLinhaReta(X,Y);
		!atacar.
	   
//Resgate a posicao da bola (percepcao) e rotacione olhando pra bola
+!olheBola : true
	<- 	?posBola(X,Y);
		rotacionePara(X,Y).
		


+!olharCompanheiro:true
	<- 	?companheiroMaisProximo(X,Y);
		rotacionePara(X,Y).
	


