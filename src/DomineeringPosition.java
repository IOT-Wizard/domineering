public class DomineeringPosition extends Position {
    final static public int BLANK = 0;
    final static public int HUMAN = 1;
    final static public int PROGRAM = -1;



    int size ;
    int [] board = new int[25]; //size²


    /*
    public DomineeringPosition(int size) {
        this.size = size;
        this.board = new int[size * size];
    }

     */
    public String toString() {
        StringBuffer sb = new StringBuffer("[");
        for (int i=0; i<25; i++) { // size * size //// size² -> ex : taille = 5  ->  5² = 25 places

            sb.append(""+board[i]+",");
        }
        sb.append("]");
        return sb.toString();


    }
}