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
+!atacar: maisPerto(bola)
	<-	!!olheBola;
		?posBola(X,Y);
		irLinhaReta(X,Y);
		!atacar.
		
//se sem bola e longe do gol, vá para perto do gol
+!atacar: not com(bola) & not perto(gol)
	<- 	!!olheBola;
		?posicao(X,Y);
		posicionaAtaque(420,Y);
		!atacar.
		
//se sem bola e perto do gol, fique onde está	
+!atacar: not com(bola) & perto(gol)
	<- !!olheBola;
		?posicao(X,Y);
		irLinhaReta(X,Y);
		!atacar.
		

//se com a bola e perto do gol, chute
+!atacar: com(bola) & perto(gol)	
	<- 	!!melhorPosChute; 
		chutar(100);
		!atacar.

//se com a bola e longe do gol, passar para o companheiro mais proximo.
+!atacar: com(bola) & not perto(gol)
	<-	!!olharCompanheiro;
		chutar(1);
		!atacar.
	
//caso contrário reposiciona
+!atacar: true
	<- 	!!olheBola; 
		?posicaoIni(X,Y);
		irLinhaReta(X,Y);
		!atacar.
	   
//Resgate a posicao da bola (percepcao) e rotacione olhando pra bola
+!olheBola : true
	<- 	?posBola(X,Y);
		rotacionePara(X,Y).
		
//decide melhor ponto do gol para chutar utilizando Fuzzy.
+!melhorPosChute : true
	<- posicaoChute.
	

+!olharCompanheiro:true
	<- 	?companheiroMaisProximo(X,Y);
		rotacionePara(X,Y).



