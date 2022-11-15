package cli;

import data.model.Diagram;
import chess.Position;
import chess.color.Color;
import chess.moves.RawMove;
import chess.utility.AlgebraicTranslator;

import java.io.*;

public class CommandLineHandler {
    Diagram base;
    boolean AnnotatingOn = false;

    public CommandLineHandler() {
        base = new Diagram();
    }

    public void makeAction(ActionData AD) {
        if (!AnnotatingOn) {
            switch (AD.getCode()) {
                case "LD" -> {
                    base = Load((String) AD.getParam());
                    if (base == null) System.out.print("Loading failed");
                }
                case "SV" -> Save();
                case "MM" -> makeMove((RawMove) AD.getParam());
                case "AN" -> startAnnotating();
                case "DL" -> deleteDiagram();
                case "GB" -> goBack((int) AD.getParam());
                case "PM" -> printMoves();
                case "PH" -> printHistory();
                case "JB" -> jumpBack();
                case "JF" -> jumpForward();
                case "HP" -> printHelp();
                default -> System.out.print("Unknown code MA");
            }
        }
        //TODO annotating

    }


    Diagram Load(String Namefile) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(Namefile + ".bin"))) {
            return (Diagram) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    void Save() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("temp.bin"))) {
            outputStream.writeObject(base.getOriginal());
            System.out.print("Saved sukces");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void makeMove(RawMove M) {
        base = base.makeMove(M);
    }

    void deleteDiagram() {
        if (base.getParent() != null) {
            Diagram Temp = base;
            base = base.getParent();
            base.getNextDiagrams().remove(Temp);
        } else {
            System.out.print("Cant delete");
        }

    }

    void startAnnotating() {
        AnnotatingOn = !AnnotatingOn;

    }

    void goBack(int pos) {
        base = base.getDiagramOfId(pos);
    }

    void printMoves() {
        base.getMoves().forEach(string -> System.out.print(string + " "));
    }

    void printHistory() {
        base.getHistory().forEach(string -> System.out.print(string + " "));
    }

    void jumpBack() {
        if (base.getParent() != null) {
            base = base.getParent();
        } else {
            System.out.print("Cant jump back");
        }
    }

    void jumpForward() {
        if (!base.getNextDiagrams().isEmpty()) {
            base = base.getNextDiagrams().peekFirst();
        } else {
            System.out.print("Cant jump forward");
        }
    }

    void printHelp() {
        System.out.print(
                """
                        To make action write code , follow it by parameter if neccesary in next line\s
                        case "SV" -> Save();
                        case "MM" -> Makes move from parameter
                        case "Q" -> Exit();
                        case "DL" -> Delete_diagram();
                        case "GB" -> Goes back to position indexed by parameter;
                        case "PM" -> PrintMoves();
                        case "PH" -> PrintHistory();
                        case "JB" -> JumpBack();
                        case "JF" -> JumpForward();
                        case "HP" -> PrintHelp();""".indent(12));

    }

    public Diagram getDiag() {
        return base;
    }

    public Color getColor() {
        return base.getBoard().getColor();
    }

    public void display() {
        System.out.println();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                int temp = base.getBoard().read(new Position(j, i));
                if (temp == 0) {
                    System.out.print("00");
                } else {
                    System.out.print(AlgebraicTranslator.numberToFigure(temp));
                }
                System.out.print(" ");
            }
            System.out.println();
            System.out.println();
        }
    }
}
