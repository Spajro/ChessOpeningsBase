package cli;

import chess.fields.Field;
import data.model.Diagram;
import chess.Position;
import chess.color.Color;
import chess.moves.RawMove;
import chess.utility.AlgebraicTranslator;
import data.model.FileManager;

import java.io.*;

public class CommandLineHandler {
    Diagram node = new Diagram();
    FileManager fileManager = new FileManager();

    public void makeAction(ActionData data) {
        switch (data.getCode()) {
            case "LD" -> {
                try {
                    node = fileManager.load((String) data.getParam());
                } catch (FileNotFoundException e) {
                    System.out.print("Loading failed");
                }
            }
            case "SV" -> fileManager.save((String) data.getParam(), node.toJson());
            case "MM" -> makeMove((RawMove) data.getParam());
            case "DL" -> deleteDiagram();
            case "GB" -> goBack((int) data.getParam());
            case "PM" -> printMoves();
            case "PH" -> printHistory();
            case "JB" -> jumpBack();
            case "JF" -> jumpForward();
            case "HP" -> printHelp();
            default -> System.out.print("Unknown code" + data.getCode());
        }
    }

    void makeMove(RawMove M) {
        node = node.makeMove(M);
    }

    void deleteDiagram() {
        if (node.getParent() != null) {
            Diagram Temp = node;
            node = node.getParent();
            node.getNextDiagrams().remove(Temp);
        } else {
            System.out.print("Cant delete");
        }

    }

    void goBack(int pos) {
        node = node.getDiagramOfId(pos);
    }

    void printMoves() {
        node.getMoves().forEach(string -> System.out.print(string + " "));
    }

    void printHistory() {
        node.getHistory().forEach(string -> System.out.print(string + " "));
    }

    void jumpBack() {
        if (node.getParent() != null) {
            node = node.getParent();
        } else {
            System.out.print("Cant jump back");
        }
    }

    void jumpForward() {
        if (!node.getNextDiagrams().isEmpty()) {
            node = node.getNextDiagrams().peekFirst();
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

    public Color getColor() {
        return node.getBoard().getColor();
    }

    public void display() {
        System.out.println();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                Field field = node.getBoard().getField(new Position(j, i));
                if (field.isEmpty()) {
                    System.out.print("00");
                } else {
                    if (field.getPiece().getColor().isWhite()) {
                        System.out.print("W");
                    } else {
                        System.out.print("B");
                    }
                    System.out.print(AlgebraicTranslator.pieceToAlgebraic(field.getPiece()));
                }
                System.out.print(" ");
            }
            System.out.println();
            System.out.println();
        }
    }
}
