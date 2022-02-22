package dts;

import ant.Annotation;

import java.io.Serializable;
import java.util.LinkedList;

public class Diagram implements Serializable {
    //class representing node of openings tree
    private final int moveId;
    private String moveName;
    private final Diagram lastMove;
    private LinkedList<Diagram> nextDiagrams;
    private Annotation annotation;
    private Board board;
    private int Color;

    public Diagram() {
        moveId = 0;
        moveName = null;
        board = Board.getStart();
        lastMove = null;
        nextDiagrams = new LinkedList<>();
        annotation = new Annotation();
        Color = Board.WHITE;
    }

    public Diagram(Board NT, Diagram Last, int C, int Id) {
        moveId = Id;
        board = Board.getCopy(NT);
        lastMove = Last;
        nextDiagrams = new LinkedList<>();
        annotation = new Annotation();
        Color = C;
    }

    public Diagram makeMove(Move M) {
        if (M.IsCorrect()) {
            Board NT = Board.getCopy(board);
            M.Make_move(NT);
            Diagram Next = new Diagram(NT, this, Board.swapColor(Color), moveId + 1);
            Next.moveName = M.GetName();
            for (Diagram D : nextDiagrams) {
                if (D.board.equals(Next.board) && D.moveId == Next.moveId) {
                    //TODO
                    return D;
                }
            }
            nextDiagrams.add(Next);
            return Next;
        } else {
            System.out.print("Move Corrupted");
            return this;
        }
    }

    public Diagram findMove(int Id) {
        if (moveId == Id) {
            return this;
        } else if (Id < moveId) {
            return lastMove.findMove(Id);
        } else {
            System.out.print("Findmove fail");
            return null;
        }
    }

    public Diagram getOriginal() {
        return findMove(0);
    }

    public String[] getMoves() {
        int n = nextDiagrams.size();
        if (n == 0) {
            System.out.print("No moves");
            return null;
        } else {
            String[] S = new String[n];
            int i = 0;
            for (Diagram D : nextDiagrams) {
                S[i] = D.moveName;
                i++;
            }
            return S;
        }
    }

    public String[] getHistory() {
        if (moveId != 0) {
            Diagram D = this;
            String[] S = new String[D.moveId + 1];
            while (D.moveId >= 0) {
                S[D.moveId] = D.moveName;
                D = D.lastMove;
                if (D == null) {
                    break;
                }
            }
            return S;
        } else return null;
    }

    public Board getBoard() {
        return board;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public Diagram getLastMove() {
        return lastMove;
    }

    public LinkedList<Diagram> getNextDiagrams() {
        return nextDiagrams;
    }

    public int getColor() {
        return Color;
    }

    public Diagram getNextDiagram(int index){
        if(index>=0 && index< nextDiagrams.size()){
            return nextDiagrams.get(index);
        }
        return null;
    }

    public int getNextDiagramsCount(){
        return nextDiagrams.size();
    }

    public int getIndexInNextDiagrams(Diagram d){
        return nextDiagrams.indexOf(d);
    }
}
