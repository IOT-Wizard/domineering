import java.util.Arrays;
import java.util.Scanner;
import java.util.Scanner;

public class Domineering extends GameSearch {

    public boolean wonPosition(Position p, boolean player) {
        if (GameSearch.DEBUG) System.out.println("drawnPosition("+p+")");
        boolean ret = true;
        DomineeringPosition pos = (DomineeringPosition)p;
        if (pos == null) {
            System.out.println("Error: DomineeringPosition is null.");
            return false;
        }


        if(player == PROGRAM){ // 19 20 21 22 23
            for(int i = 0; i < pos.size * pos.size - pos.size; i++) {   // (int i = 0; i < pos.size * pos.size - size; i++) 💱

                    if (pos.board[i] == DomineeringPosition.BLANK && pos.board[i+pos.size] == DomineeringPosition.BLANK) {
                        ret = false;
                        break;
                    }

            }
        }

        if(player == HUMAN){
            for (int i = 0; i < pos.size * (pos.size - 1); i++)  { // 4 9 14 18 23 // (int i = 0; i < pos.size * (pos.size - 1); i++) 💱
                if (i % pos.size == pos.size - 1) //  (i % pos.size == pos.size - 1) 💱 i % pos.size == pos.size - 1 = 4 dans ce cas 📑Math 🤖
                    continue;
                else {
                    if (pos.board[i] == DomineeringPosition.BLANK && pos.board[i+1] == DomineeringPosition.BLANK) {
                        ret = false;
                        break;
                    }
                }
            }
        }

        if (GameSearch.DEBUG) System.out.println("     ret="+ret);
        return ret;
    }

    private boolean winCheck(int i1, int i2, int i3,
                             boolean player, DomineeringPosition pos) {
        int b = 0;
        if (player) b = DomineeringPosition.HUMAN;
        else        b = DomineeringPosition.PROGRAM;
        if (pos.board[i1] == b &&
                pos.board[i2] == b &&
                pos.board[i3] == b)         return true;
        return false;
    }

    @Override
    public float positionEvaluation(Position p, boolean player) {
        DomineeringPosition pos = (DomineeringPosition) p;

        // Critique heuristique 1
        int countH = numHorizontalPos(pos.board, pos.size);
        int countV = numVerticalPos(pos.board, pos.size);
        float ret1 = countH - countV;

        // Critique heuristique 2
        int count2 = 0;
        for (int i = 0; i < (pos.size * pos.size) - pos.size; i++) {
            if (i % pos.size % 2 == 1 && pos.board[i] == 0 && pos.board[i + pos.size] == 0) {
                count2++;
            }
        }
        float ret2 = 10 - count2;

        // Facteur de pondération pour chaque critique
        float weight1 = 1.0f;  // ajustez cela en fonction de l'importance de la première critique
        float weight2 = 0.5f;  // ajustez cela en fonction de l'importance de la deuxième critique

        // Combinaison des deux critiques
        float combinedEvaluation = (weight1 * ret1) + (weight2 * ret2);

        // Ajout des facteurs de pondération pour les positions gagnantes et au centre
        if (wonPosition(p, player)) {
            combinedEvaluation += 10.0f;
        } else if (wonPosition(p, !player)) {
            combinedEvaluation -= 10.0f;
        }

        if (pos.board[(pos.size * pos.size) / 2] == DomineeringPosition.HUMAN && player) {
            combinedEvaluation += 0.4f;
        } else if (pos.board[(pos.size * pos.size) / 2] == DomineeringPosition.PROGRAM && !player) {
            combinedEvaluation -= 0.4f;
        }

        return combinedEvaluation;
    }

   /* @Override
    public float positionEvaluation(Position p, boolean player) {
        DomineeringPosition dp = (DomineeringPosition) p;
        int[] table = dp.board;
        int size = dp.size;
        int countPlayer1;
        int countPlayer2;
        float result;

        countPlayer1 = numberOfHorizontalPos(table, size);
        countPlayer2 = numberOfVerticalPos(table, size);

        result = countPlayer1 - countPlayer2;
        if (player) {
            return result;
        } else {
            return -result;
        }
    }
*/
    public int numVerticalPos(int[] table, int size) {
        int ret = 0;
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size; j++) {
                if (table[i * size + j] == DomineeringPosition.BLANK &&
                        table[(i + 1) * size + j] == DomineeringPosition.BLANK) {
                    ret++;
                }
            }
        }
        return ret;
    }

    public int numHorizontalPos(int[] table, int size) {
        int ret = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size - 1; j++) {
                if (table[i * size + j] == DomineeringPosition.BLANK &&
                        table[i * size + j + 1] == DomineeringPosition.BLANK) {
                    ret++;
                }
            }
        }
        return ret;
    }



    public void printPosition(Position p) {
        System.out.println("Board position:");
        DomineeringPosition pos = (DomineeringPosition)p;
        int count = 0;

        for (int row=0; row<pos.size; row++) {
            System.out.println();
            for (int col=0; col<pos.size; col++) {
                if (pos.board[count] == DomineeringPosition.HUMAN) {
                    System.out.print("H");
                } else if (pos.board[count] == DomineeringPosition.PROGRAM) {
                    System.out.print("P");
                } else {
                    System.out.print("0");
                }
                count++;
            }
        }
        System.out.println();
    }
    /*public Position [] possibleMoves(Position p, boolean player) {
        if (GameSearch.DEBUG) System.out.println("posibleMoves("+p+","+player+")");
        DomineeringPosition pos = (DomineeringPosition)p;
        int count = 0;
        for (int i=0; i<20; i++) if (pos.board[i] == 0 && pos.board[i+ 5] == 0 ) count++; // pos.board[i+ taille] == 0
        if (count == 0) return null;
        Position [] ret = new Position[count];
        count = 0;
        for (int i=0; i<20; i++) {
            if (pos.board[i] == 0 && pos.board[i+ 5] == 0 ) {
                DomineeringPosition pos2 = new  DomineeringPosition();
                for (int j=0; j<25; j++) pos2.board[j] = pos.board[j];
                if (player) {pos2.board[i] = 1; }else{ pos2.board[i] = -1;pos2.board[i+5] = -1; };
                ret[count++] = pos2;
                if (GameSearch.DEBUG) System.out.println("    "+pos2);
            }
        }
        return ret;
    }*/

    public Position[] possibleMoves(Position p, boolean player) {
        if (GameSearch.DEBUG) System.out.println("posibleMoves(" + p + "," + player + ")");
        DomineeringPosition pos = (DomineeringPosition) p;
        int count = 0;

        for (int i = 0; i < pos.size * pos.size - pos.size; i++) {
            if (i + pos.size < pos.size * pos.size  && pos.board[i] == 0 && pos.board[i + pos.size] == 0) {
                count++;
            }
        }

        if (count == 0) {
            return new Position[0]; // Return an empty array if there are no possible moves
        }

        Position[] ret = new Position[count];
        count = 0;

        for (int i = 0; i < pos.size * pos.size - pos.size; i++) {
            if (i + pos.size  < pos.size * pos.size  && pos.board[i] == 0 && pos.board[i + pos.size] == 0) {
                DomineeringPosition pos2 = new DomineeringPosition();
                pos2.board = Arrays.copyOf(pos.board, pos.size * pos.size );

                if (player) {
                    pos2.board[i] = 1;
                    pos2.board[i + pos.size ] = 1;
                } else {
                    pos2.board[i] = -1;
                    pos2.board[i + pos.size] = -1;
                }

                ret[count++] = pos2;

                if (GameSearch.DEBUG) System.out.println("    " + pos2);
            }
        }

        return ret;
    }


    public Position makeMove(Position p, boolean player, Move move) {
        if (GameSearch.DEBUG) System.out.println("Entered TicTacToe.makeMove");
        DomineeringMove m = (DomineeringMove)move;
        DomineeringPosition pos = (DomineeringPosition)p;
        DomineeringPosition pos2 = new  DomineeringPosition();
        for (int i=0; i<pos.size * pos.size ; i++) pos2.board[i] = pos.board[i];
        int pp;
        if (player) pp =  1;
        else        pp = -1;
        if (GameSearch.DEBUG) System.out.println("makeMove: m.moveIndex = " + m.moveIndex);
        if (player == HUMAN){
            pos2.board[m.moveIndex] = pp;
            pos2.board[m.moveIndex+1] = pp;

        }
        /*
        if (player == PROGRAM){
            pos2.board[m.moveIndex] = pp;
            pos2.board[m.moveIndex+5] = pp;

        }

         */
        return pos2;
    }
    public boolean reachedMaxDepth(Position p, int depth) {
        boolean ret = false;

        if(depth>=HomePage.getSelectedcomplex()) return true;
        if (wonPosition(p, false)) ret = true;
        else if (wonPosition(p, true))  ret = true;
        if (GameSearch.DEBUG) {
            System.out.println("reachedMaxDepth: pos=" + p.toString() + ", depth="+depth
                    +", ret=" + ret);
        }
        return ret;
    }
    public Move createMove() {
        if (GameSearch.DEBUG) System.out.println("Enter blank square index [0,8]:");
        int i = 0;
        try {
            Scanner sc = new Scanner(System.in) ;
            int ch = sc.nextInt() ;
            i = ch ;
            //System.in.read();

        } catch (Exception e) { }
        DomineeringMove mm = new DomineeringMove();
        mm.moveIndex = i;
        return mm;
    }
    static public void main(String [] args) {
        DomineeringPosition p = new DomineeringPosition();
        Domineering ttt = new Domineering();
        ttt.playGame(p, true);
    }
}