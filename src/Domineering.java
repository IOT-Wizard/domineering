import java.util.Scanner;

public class Domineering extends GameSearch {

        public boolean wonPosition(Position p, boolean player) {
            if (GameSearch.DEBUG) System.out.println("drawnPosition("+p+")");
            boolean ret = true;
            DomineeringPosition pos = (DomineeringPosition)p;

            if(player == PROGRAM){ // 19 20 21 22 23
                for (int i = 0; i < 25; i++) {
                    if (i == 19 || i == 20 || i == 21|| i == 22|| i == 23)
                        continue;
                    else {
                        if (pos.board[i] == DomineeringPosition.BLANK && pos.board[i+1] == DomineeringPosition.BLANK) {
                            ret = false;
                            break;
                        }
                    }
                }
            }

            if(player == HUMAN){
                for (int i = 0; i < 25; i++) { // 4 9 13 18 23
                    if (i == 4 || i == 9 || i == 13 || i == 18|| i == 23)
                        continue;
                    else {
                        if (pos.board[i] == DomineeringPosition.BLANK && pos.board[i+5] == DomineeringPosition.BLANK) {
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

        public float positionEvaluation(Position p, boolean player) {
            int count = 0;
            DomineeringPosition pos = (DomineeringPosition)p;
            for (int i=0; i<25; i++) {
                if (pos.board[i] == 0) count++;
            }
            count = 10 - count;
            // prefer the center square:
            float base = 1.0f;
            if (pos.board[4] == DomineeringPosition.HUMAN &&
                    player) {
                base += 0.4f;
            }
            if (pos.board[4] == DomineeringPosition.PROGRAM &&
                    !player) {
                base -= 0.4f;
            }
            float ret = (base - 1.0f);
            if (wonPosition(p, player))  {
                return base + (1.0f / count);
            }
            if (wonPosition(p, !player))  {
                return -(base + (1.0f / count));
            }
            return ret;
        }
        public void printPosition(Position p) {
            System.out.println("Board position:");
            DomineeringPosition pos = (DomineeringPosition)p;
            int count = 0;

            for (int row=0; row<5; row++) {
                System.out.println();
                for (int col=0; col<5; col++) {
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
        public Position [] possibleMoves(Position p, boolean player) {
            if (GameSearch.DEBUG) System.out.println("posibleMoves("+p+","+player+")");
            DomineeringPosition pos = (DomineeringPosition)p;
            int count = 0;
            for (int i=0; i<25; i++) if (pos.board[i] == 0 && pos.board[i+ 5] == 0 ) count++; // pos.board[i+ taille] == 0
            if (count == 0) return null;
            Position [] ret = new Position[count];
            count = 0;
            for (int i=0; i<25; i++) {
                if (pos.board[i] == 0) {
                    DomineeringPosition pos2 = new  DomineeringPosition();
                    for (int j=0; j<25; j++) pos2.board[j] = pos.board[j];
                    if (player) pos2.board[i] = 1; else pos2.board[i] = -1;
                    ret[count++] = pos2;
                    if (GameSearch.DEBUG) System.out.println("    "+pos2);
                }
            }
            return ret;
        }
        public Position makeMove(Position p, boolean player, Move move) {
            if (GameSearch.DEBUG) System.out.println("Entered TicTacToe.makeMove");
            DomineeringMove m = (DomineeringMove)move;
            DomineeringPosition pos = (DomineeringPosition)p;
            DomineeringPosition pos2 = new  DomineeringPosition();
            for (int i=0; i<25; i++) pos2.board[i] = pos.board[i];
            int pp;
            if (player) pp =  1;
            else        pp = -1;
            if (GameSearch.DEBUG) System.out.println("makeMove: m.moveIndex = " + m.moveIndex);
            if (player == HUMAN){
                pos2.board[m.moveIndex] = pp;
                pos2.board[m.moveIndex+1] = pp;

            }
            if (player == PROGRAM){
                pos2.board[m.moveIndex] = pp;
                pos2.board[m.moveIndex+5] = pp;

            }
            return pos2;
        }
        public boolean reachedMaxDepth(Position p, int depth) {
            boolean ret = false;
            if(depth>=5) return true;
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

