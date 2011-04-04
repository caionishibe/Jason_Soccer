// Agente Atacante

/* Initial beliefs and rules */
posicaoInicial(8, 8).

/* Initial goals */

// objetivo inicial apenas entrar em campo
!entrarEmCampo.

/* Plans */

// ao entrar em campo, obter a posicao inicial do jogador em campo,
// criar o jogador no tewnta e comeca a dancar.
+!entrarEmCampo : true
    <- ?posicaoInicial(X, Y);
       createPlayer(X, Y, TEAM_A);
       !dance.

// ao inserir o objetivo dance na base de conhecimentos, sob qualquer condicao
// (sempre true), gire, pule e continue dancando.
+!dance : true
    <- gire;
       pule;
       !dance.




