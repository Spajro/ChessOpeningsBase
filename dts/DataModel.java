package dts;

import java.io.*;

public class DataModel {
    private Board actualBoard;
    private Diagram tree;
    private String name;

    public DataModel() {
        tree=new Diagram();
        actualBoard=tree.T;
        name="new datamodel";
    }

    public void readDiagramFromFile(String filename){
        tree=load(filename);
        name=filename;
    }

    public void makeMove(Move m){
        tree=tree.Make_move(m);
        setActualBoard(tree.T);
    }

    Diagram load(String filename){
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename+".bin"))) {
            return (Diagram) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    void save(){
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(name+".bin"))) {
            outputStream.writeObject(tree.Original());
            System.out.print("Saved sukces");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public Board getActualBoard() {
        return actualBoard;
    }

    public void setActualBoard(Board actualBoard) {
        this.actualBoard = actualBoard;
    }
}
