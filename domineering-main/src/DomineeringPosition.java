import java.util.Vector;

public class DomineeringPosition extends Position {
    final static public int BLANK = 0;
    final static public int HUMAN = 1;
    final static public int PROGRAM = -1;
    public static int EMPTY;
    private Domineering gameSearch;




    int size ;
    int [] board ;// = new int[25]; //size²

    int Complex ;





    public DomineeringPosition() {
        this.size =Integer.parseInt(HomePage.getSelectedSize().substring(0, 1));
        this.board = new int[size * size];
        this.gameSearch = new Domineering();
        //this.Complex = cmpx ;
    }

  /*  public String toString() {
        StringBuffer sb = new StringBuffer("[");
        for (int i=0; i<25; i++) { // size * size //// size² -> ex : taille = 5  ->  5² = 25 places

            sb.append(""+board[i]+",");
        }
        sb.append("]");
        return sb.toString();


    }*/

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size * size; i++) {
            sb.append(board[i]).append(",");
        }
        sb.append("]");
        return sb.toString();
    }


    public boolean wonPosition(int player) {
        boolean ret = true;

        if (player == PROGRAM) {
            for (int i = 0; i < size * size; i++) {
                if (i % size == size - 1) // Skip the last column
                    continue;
                if (board[i] == DomineeringPosition.BLANK && board[i + 1] == DomineeringPosition.BLANK) {
                    ret = false;
                    break;
                }
            }
        }

        if (player == HUMAN) {
            for (int i = 0; i < size * (size - 1); i++) {
                if (i % size == size - 1) // Skip the last column
                    continue;
                if (board[i] == DomineeringPosition.BLANK && board[i + 1] == DomineeringPosition.BLANK) {
                    ret = false;
                    break;
                }
            }
        }

        if (GameSearch.DEBUG) System.out.println("     ret=" + ret);
        return ret;
    }

    public void makeProgramMove() {
        Vector v = gameSearch.alphaBeta(0, this, GameSearch.PROGRAM);
        Position newPosition = (Position) v.elementAt(1);

        if (newPosition != null) {
            this.board = ((DomineeringPosition) newPosition).board;
        }
    }




}