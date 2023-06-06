package data.model;

import chess.board.ChessBoard;
import chess.moves.raw.RawMove;
import chess.results.ValidMoveResult;
import data.model.games.GamesUpdateEvent;
import data.model.metadata.GameData;
import data.model.metadata.MetaData;
import log.Log;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class DiagramManager {
    public GamesUpdateEvent merge(Diagram tree, Diagram node) {
        if (tree.partiallyEquals(node)) {
            return copyData(tree, node).join(
                    node.getNextDiagrams().stream()
                            .map(diagram -> {
                                List<Diagram> matching = tree.getNextDiagrams().stream().filter(diagram::partiallyEquals).toList();
                                switch (matching.size()) {
                                    case 0 -> {
                                        diagram.setParent(tree);
                                        tree.getNextDiagrams().add(diagram);
                                        return GamesUpdateEvent.empty();
                                    }
                                    case 1 -> {
                                        return merge(matching.get(0), diagram);
                                    }
                                    default -> {
                                        Log.log().fail("Too many matching nodes to insert");
                                        return GamesUpdateEvent.empty();
                                    }
                                }
                            })
                            .reduce(GamesUpdateEvent.empty(),
                                    GamesUpdateEvent::join)
            );
        } else {
            Log.log().fail("Cant merge");
            return GamesUpdateEvent.empty();
        }
    }

    public void insert(Diagram tree, LinkedList<RawMove> moves) {
        RawMove move = moves.poll();
        Optional<Diagram> optionalDiagram = getByRawMove(tree, move);
        if (optionalDiagram.isPresent()) {
            insert(optionalDiagram.get(), moves);
        } else {
            ChessBoard chessBoard = tree.getBoard();
            Optional<ValidMoveResult> validMoveResult = chessBoard.makeMove(move).validate(null);
            if (validMoveResult.isEmpty()) {
                Log.log().fail("Cant insert");
            } else {
                tree.getNextDiagrams().add(new LazyDiagram(
                        validMoveResult.get().getExecutableMove(),
                        validMoveResult.get().getResult(),
                        tree,
                        moves
                ));
            }
        }
    }

    private Optional<Diagram> getByRawMove(Diagram diagram, RawMove move) {
        for (Diagram nextDiagram : diagram.getNextDiagrams()) {
            if (nextDiagram.getCreatingMove().isPresent()) {
                if (nextDiagram.getCreatingMove().get().equals(move)) {
                    return Optional.of(nextDiagram);
                }
            }
        }
        return Optional.empty();
    }

    private GamesUpdateEvent copyData(Diagram to, Diagram from) {
        to.getAnnotations().addAll(from.getAnnotations());
        to.getMetaData().addAll(from.getMetaData());
        return GamesUpdateEvent.of(from.getMetaData(), to);
    }

    public GamesUpdateEvent updateMetadata(Diagram node) {
        if (node.getMetaData().isEmpty()) {
            Log.log().warn("Cant update metadata for diagram without it");
            return GamesUpdateEvent.empty();
        }

        Optional<Diagram> optionalParent = node.getParent();
        if (optionalParent.isEmpty()) {
            return GamesUpdateEvent.empty();
        }
        Diagram parent = optionalParent.get();

        return switch (parent.getNextDiagrams().size()) {
            case 1 -> moveMetaData(node, parent).join(updateMetadata(parent));
            case 2 -> updateBrotherMetadata(node, parent);
            default -> GamesUpdateEvent.empty();
        };
    }

    private GamesUpdateEvent moveMetaData(Diagram from, Diagram to) {
        GamesUpdateEvent event = GamesUpdateEvent.of(from.getMetaData(), to);
        to.getMetaData().addAll(from.getMetaData());
        from.getMetaData().clear();
        return event;
    }

    private GamesUpdateEvent updateBrotherMetadata(Diagram diagram, Diagram parent) {
        Diagram brother = getBrother(diagram);
        brother.getMetaData().addAll(getMetadataFromPathToRoot(parent));
        return GamesUpdateEvent.of(brother.getMetaData(), brother);
    }

    private List<MetaData> getMetadataFromPathToRoot(Diagram node) {
        List<GameData> gameData = node.getNonEndingGameData();
        if (!gameData.isEmpty()) {
            List<MetaData> result = List.copyOf(gameData);
            node.getMetaData().removeAll(gameData);
            return result;
        } else {
            if (node.getParent().isEmpty()) {
                return List.of();
            } else {
                return getMetadataFromPathToRoot(node.getParent().get());
            }
        }
    }

    private Diagram getBrother(Diagram diagram) {
        Diagram parent = diagram.getParent().orElseThrow();
        Diagram diagram1 = parent.getNextDiagrams().get(0);
        Diagram diagram2 = parent.getNextDiagrams().get(1);
        if (diagram == diagram1) {
            return diagram2;
        } else if (diagram == diagram2) {
            return diagram1;
        }
        Log.log().fail("Too many diagrams to choose from");
        return diagram1;
    }
}
