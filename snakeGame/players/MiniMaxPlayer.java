package players;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import snake.GameState;
import snake.Snake;
import static players.RandomPlayer.rand;


/**
 *
 * @author Lewis Francis C1826277.
 * 20 iterations of the game were run and my minimax player had only won 2 while the majority winners are the Astar algorithms with a total of 18 which meant that the random player lost all of them.
 */

public class MiniMaxPlayer extends RandomPlayer {


    public MiniMaxPlayer(GameState state, int index, Snake game) {
        super(state, index, game);
    }

    public void doMove() {
        state.setOrientation(index, chooseMove(this.game));
    }

    public int chooseMove(Snake game) {
        long startTime = System.currentTimeMillis();
        long rTime = startTime + 110;
        
        GameState state = this.state;
        int depth = 0;
        int depthLimit = 5;
        int player = getIndex();
        
        int bestMove = -10;
        int bestVal = 0;
        int unfavourableBest = 10;
        int lastMove;

        for (; System.currentTimeMillis() < rTime; depthLimit++){

            //variables for the depthLimit.

            depth = 0;
            bestVal = 0;
            bestMove = -10;
            

            //Start of the search to find possible states.


            for (GameState c : getChildN(state, player)) {
                int val = winLosePointUtil(getnextstate(c, getNextPlayer(state, player), depth, depthLimit, rTime), player);
                
                

                if (bestMove == -10 || val > bestVal){
                    bestVal = val;
                    bestMove = c.getLastOrientation(player);
                }
                if(System.currentTimeMillis() > rTime){
                    break;
                }
            }
            if (System.currentTimeMillis() < rTime)
            unfavourableBest = bestMove;
        }
        lastMove = unfavourableBest;
        return lastMove;
    }

    //Prediction for future moves

    public GameState getnextstate(GameState state, int currentPlayer, int depth, int depthLimit, long rTime) {

        //stop loop after searching to the depth limit/ game over.

        if(state.isGameOver() || depth == depthLimit) {
            return state;
        } else {

            GameState newc = state;
            GameState bestc = state;
            int nextPlayer;
            int bestVal = 0;
            int val;
            
            

            for (GameState c : getChildN(state, currentPlayer)){
                if (System.currentTimeMillis() > rTime) {
                    return state;
                }

                nextPlayer = getNextPlayer(state, currentPlayer);
                
                //Adjusting child to new state when required.

                newc = getnextstate(c, nextPlayer, depth +1, depthLimit, rTime);
                val = winLosePointUtil(newc, currentPlayer);

                if (val > bestVal) {
                    bestVal = val;
                    bestc = c;
                }
            }
            return bestc;
        }
    }
    //Return state used in getChildN.

    

    public GameState tryMove(int player, int move, GameState startState) {
        GameState newState = new GameState(startState);
        newState.setOrientation(player, move);
        newState.updatePlayerPosition(player);
        return newState;
   
    }
    //Current states.

    public List<GameState> getChildN(GameState state, int player) {
        List<GameState> children = new ArrayList<GameState>();
        for (int j = 1; j <= 5; j++) {
            if(state.isLegalMove(player, j)){
                children.add(tryMove(player, j, state));
            }
        }
    return children;
    }
    
    //Gets next players movement/state.

    public int getNextPlayer(GameState state, int currentPlayer) {
        int nextPlayer;
        nextPlayer = (currentPlayer + 1) % state.getNrPlayers();
        while (state.isDead(nextPlayer)) {
            nextPlayer = (nextPlayer + 1) % state.getNrPlayers();
        }
        return nextPlayer;
    }

    //Check for scoring point.

    public boolean ifScorePoint(GameState state, int player) {
        int size = state.getSize(player);
        for (GameState c : getChildN(state, player)){
            if(c.getSize(player) > size) {
                return true;
            }
        }
        return false;
    }
    //Util used for finding how the game state is currently at. Win or Loss or Score.

    public int winLosePointUtil(GameState state, int player) {

        //Counter for dead players.

        int counterDeadPL = 0;
        for (int j = 1; j <state.getNrPlayers(); j++) {
            if(state.isDead(j)) {
                counterDeadPL++;
            }
        }

        //Scores.        

        if (ifScorePoint(state, player)){
            return state.getHeight() + state.getWidth() + 1; 

        }
        //Winner.

        else if ((counterDeadPL == state.getNrPlayers()-1) && (!state.isDead(player))){
            return Integer.MAX_VALUE;
        
        //Loser.

        } else if (state.isDead(player)){
            return Integer.MIN_VALUE;

         }else {
            int util = 0;    
            util = -(Math.abs(state.getTargetX() - state.getPlayerX(player).get(0)) + (Math.abs(state.getTargetY() - state.getPlayerY(player).get(0))));
            state.updatePlayerPosition(player);

            return util;
        }
    }
        // fix for problems with snake just patrolling border via manoeuvre
        // snake is better at manoevring with opponents (occupied).
        public int manoeuvre(GameState state, int player) {
                int manoeuvre = 0;
                for (int opponent= 0; opponent<state.getNrPlayers(); opponent++) {
                    if (!(opponent==player)) {
                    manoeuvre  += Math.abs(state.getPlayerX(opponent).get(0) - state.getPlayerX(player).get(0));
                    manoeuvre  += Math.abs(state.getPlayerY(opponent).get(0) - state.getPlayerY(player).get(0));
                    }   
             }
                return manoeuvre ;
         }

    }
