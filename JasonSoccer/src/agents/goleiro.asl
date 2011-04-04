// Agente Goleiro

/* Initial beliefs and rules */
posicaoInicial(0, 8).

/* Initial goals */

// objetivo inicial é apenas entrar em campo
!entrarEmCampo.

/* Plans */

// ao entrar em campo, obter a posição inicial do jogador em campo,
// criar o jogador no tewnta e começa a dançar.
+!entrarEmCampo : true
    <- ?posicaoInicial(X, Y);
       createPlayer(X, Y);
       !dance.

// ao inserir o objetivo dance na base de conhecimentos, sob qualquer condição
// (sempre true), gire, pule e continue dançando.
+!dance : true
    <- gire;
       pule;
       !dance.