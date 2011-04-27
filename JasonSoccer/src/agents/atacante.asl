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
+!atacar: maisPertoBola(A) & meuNome(A) & not comBola(A)
	<-	?posBola(X,Y);
		irLinhaReta(X,Y);
		!atacar.

		
//se sem bola e longe do gol, vá para perto do gol
+!atacar: comBola(A) & companheiro(A) 
	<-  ?posicaoIni(X,Y);
		posicionaAtaque(A,Y);
		!atacar.

//se com a bola e perto do gol, ache melhor posicao para chute
+!atacar: comBola(A) & meuNome(A) & perto(gol) 	
	<- 	posicaoChute; 
		!atacar.


/*
//se com a bola e longe do gol, passar para o companheiro mais proximo.
+!atacar: com(bola) & not perto(gol)
	<-	//!olharCompanheiro;
		passar;
		!atacar.
	
*/
//caso contrário reposiciona
+!atacar: true
	<- 	?posicaoIni(X,Y);
		irLinhaReta(X,Y);
		!atacar.

		
		
		
//caso contrário reposiciona		
+!defender: true
	<- ?posicaoIni(X,Y);
		irLinhaReta(X,Y);
		!defender.
		
		

	   

