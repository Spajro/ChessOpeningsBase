package data.model;

import chess.moves.raw.RawMove;
import chess.moves.valid.executable.ExecutableMove;
import data.model.games.GamesRepository;
import data.model.metadata.MetaData;
import log.Log;

import java.util.*;

public class DataModel {
    private Diagram actualNode;
    private final GamesRepository games = new GamesRepository();
    private final TreeDataModel treeDataModel = new TreeDataModel();
    private final DiagramManager diagramManager = new DiagramManager();
    private PromotionTypeProvider promotionTypeProvider;

    public DataModel() {
        actualNode = new Diagram();
        treeDataModel.setActualNode(actualNode);
    }

    public void makeMove(RawMove m) {
        Diagram tempNode = actualNode;
        actualNode = actualNode.makeMove(m, promotionTypeProvider);
        if (tempNode != actualNode) {
            treeDataModel.setActualNode(actualNode);
            treeDataModel.notifyListenersOnInsert(actualNode);
        }
    }

    public void insert(ArrayDeque<ExecutableMove> moves, MetaData metaData) {
        Log.log().info("DataModel insertion");
        Diagram actualRoot = actualNode.getRoot();
        games.update(diagramManager.insert(actualRoot, moves, metaData));
        games.update(diagramManager.updateMetadata(games.get(metaData)));
    }

    public Optional<Diagram> getLast(Diagram diagram) {
        return switch (diagram.getNextDiagrams().size()) {
            case 0 -> Optional.of(diagram);
            case 1 -> getLast(diagram.getNextDiagrams().get(0));
            default -> Optional.empty();
        };
    }

    public void setActualNode(Diagram actualNode) {
        this.actualNode = actualNode;
        treeDataModel.setActualNode(actualNode);
    }

    public void setNewTree(Diagram tree) {
        this.actualNode = tree;
        treeDataModel.setActualNode(tree);
        games.setNewTree(tree.getRoot());
    }

    public Diagram getActualNode() {
        return actualNode;
    }

    public void setPromotionTypeProvider(PromotionTypeProvider promotionTypeProvider) {
        this.promotionTypeProvider = promotionTypeProvider;
    }

    public TreeDataModel asTree() {
        return treeDataModel;
    }


    public GamesRepository getGames() {
        return games;
    }
}
