package dts;

import ant.Annotation;

import java.io.Serializable;
import java.util.LinkedList;

public class Diagram implements Serializable {
    public LinkedList<Diagram> Story;
    public LinkedList<Diagram> Next_moves;
    Annotation Info;
    public Bufor T;
    public boolean Color;
    public Diagram(){
        T=new Bufor(8);
        Story=new LinkedList<>();
        Next_moves = new LinkedList<>();
        Info = new Annotation();
        Story.add(this);
        Color=true;
    }
    public Diagram(Bufor NT, LinkedList<Diagram> S, boolean C){
        T=NT;
        Story=S;
        Next_moves = new LinkedList<>();
        Info = new Annotation();
        Story.add(this);
        Color=C;
    }
    public Diagram Make_move(Move M){
        LinkedList<Diagram>NS=Story;
        Bufor NT=T;
        M.Make_move(NT);
        NS.add(this);
        Diagram Next=new Diagram(NT,NS,!Color);
        for(Diagram D : Next_moves){
            if(D.T== Next.T && D.Story==Next.Story){
                return D;
            }
        }
        Next_moves.add(Next);
    return Next;
    }
    public Diagram Original(){
        return Story.get(0);
    }
}
