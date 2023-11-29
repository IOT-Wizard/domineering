public class DomineeringPosition extends Position {
    final static public int BLANK = 0;
    final static public int HUMAN = 1;
    final static public int PROGRAM = -1;



    int size ;
    int [] board ;// = new int[25]; //size²





    public DomineeringPosition(int size) {
        this.size = size;
        this.board = new int[size * size];
    }
    public DomineeringPosition() {
        this.size = size;
        this.board = new int[size * size];
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("[");
        for (int i=0; i<25; i++) { // size * size //// size² -> ex : taille = 5  ->  5² = 25 places

            sb.append(""+board[i]+",");
        }
        sb.append("]");
        return sb.toString();


    }

    public boolean wonPosition( int player) {
        boolean ret = true;

        if(player == PROGRAM){ // 19 20 21 22 23
            for (int i = 0; i < 20; i++) {   // (int i = 0; i < pos.size * pos.size; i++) 💱
                if (i == 20 || i == 21 || i == 22|| i == 23|| i == 24) // Non traite a suprrimer LOL car i esr tjrs <20 🙂🙂🙂
                    continue;

                else {
                    if (board[i] == DomineeringPosition.BLANK && board[i+5] == DomineeringPosition.BLANK) {
                        ret = false;
                        break;
                    }
                }
            }
        }

        if(player == HUMAN){
            for (int i = 0; i < 24; i++) { // 4 9 14 18 23 // (int i = 0; i < pos.size * (pos.size - 1); i++) 💱
                if (i == 4 || i == 9 || i == 14 || i == 19|| i == 24) //  (i % pos.size == pos.size - 1) 💱 i % pos.size == pos.size - 1 = 4 dans ce cas 📑Math 🤖
                    continue;
                else {
                    if (board[i] == DomineeringPosition.BLANK && board[i+1] == DomineeringPosition.BLANK) {
                        ret = false;
                        break;
                    }
                }
            }
        }

        if (GameSearch.DEBUG) System.out.println("     ret="+ret);
        return ret;
    }

}
