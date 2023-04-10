package chess.utility;

import chess.Position;
import chess.board.ChessBoard;
import chess.board.lowlevel.Field;
import chess.moves.RawMove;
import chess.pieces.*;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ShortAlgebraicParser {
    private final AlgebraicParserUtility utility = new AlgebraicParserUtility();

    public RawMove parseShortAlgebraic(String move, ChessBoard chessBoard) {
        return utility.algebraicCastleToMove(move, chessBoard.getColor())
                .orElseGet(() -> shortAlgebraicToMove(move, chessBoard)
                        .orElseThrow(() -> new IllegalArgumentException("Wrong algebraic"))
                );
    }

    private Optional<RawMove> shortAlgebraicToMove(String move, ChessBoard chessBoard) {
        return switch (move.length()) {
            case 2 -> pawnToMove(move, chessBoard);
            case 3 -> xor(
                    pawnCaptureToMove(move, chessBoard),
                    xor(
                            specialPawnToMove(move, chessBoard),
                            pieceToMove(move, chessBoard)));
            case 4 -> xor(
                    pieceCaptureToMove(move, chessBoard),
                    xor(
                            specialPieceToMove(move, chessBoard),
                            specialPawnCaptureToMove(move, chessBoard))
            );
            case 5 -> specialPieceCaptureToMove(move, chessBoard);
            default -> throw new IllegalStateException("Unexpected algebraic length: " + move.length());
        };
    }

    private Optional<RawMove> xor(Optional<RawMove> first, Optional<RawMove> second) {
        if (first.isPresent() && second.isEmpty()) {
            return first;
        } else if (first.isEmpty() && second.isPresent()) {
            return second;
        } else if (first.isEmpty() && second.isEmpty()) {
            return Optional.empty();
        } else {
            throw new IllegalArgumentException();
        }
    }

    private Optional<RawMove> pawnToMove(String move, ChessBoard chessBoard) {
        Position end = utility.algebraicToPosition(move);
        return getSinglePieceMove(new Pawn(chessBoard.getColor(), end, chessBoard), chessBoard);
    }

    private Optional<RawMove> pawnCaptureToMove(String move, ChessBoard chessBoard) {
        if (move.charAt(0) != 'x') {
            return Optional.empty();
        }
        return pawnToMove(move.substring(1), chessBoard);
    }

    private Optional<RawMove> pieceToMove(String move, ChessBoard chessBoard) {
        Position end = utility.algebraicToPosition(move.substring(1));
        Optional<Piece> optionalPiece = charToPiece(move.charAt(0), end, chessBoard);
        if (optionalPiece.isEmpty()) {
            return Optional.empty();
        }
        return getSinglePieceMove(optionalPiece.get(), chessBoard);
    }

    private Optional<RawMove> specialPawnToMove(String move, ChessBoard chessBoard) {
        return Optional.empty();//TODO
    }

    private Optional<RawMove> pieceCaptureToMove(String move, ChessBoard chessBoard) {
        if (move.charAt(1) != 'x') {
            return Optional.empty();
        }
        return pieceToMove(move.charAt(0) + move.substring(2), chessBoard);
    }

    private Optional<RawMove> specialPieceToMove(String move, ChessBoard chessBoard) {
        return Optional.empty();//TODO
    }

    private Optional<RawMove> specialPawnCaptureToMove(String move, ChessBoard chessBoard) {
        return Optional.empty();//TODO
    }

    private Optional<RawMove> specialPieceCaptureToMove(String move, ChessBoard chessBoard) {
        return Optional.empty();//TODO
    }

    private Optional<RawMove> getSinglePieceMove(Piece piece, ChessBoard chessBoard) {
        Set<Position> positionSet = piece
                .getPossibleStartPositions()
                .stream()
                .map(chessBoard::getField)
                .filter(Field::hasPiece)
                .map(Field::getPiece)
                .filter(piece::partiallyEquals)
                .map(Piece::getPosition)
                .collect(Collectors.toSet());
        if (positionSet.size() != 1) {
            return Optional.empty();
        }
        return Optional.of(new RawMove(positionSet.stream().findAny().get(), piece.getPosition()));
    }

    private Optional<Piece> charToPiece(char piece, Position position, ChessBoard chessBoard) {
        return Optional.ofNullable(switch (piece) {
            case 'N' -> new Knight(chessBoard.getColor(), position, chessBoard);
            case 'B' -> new Bishop(chessBoard.getColor(), position, chessBoard);
            case 'R' -> new Rook(chessBoard.getColor(), position, chessBoard);
            case 'Q' -> new Queen(chessBoard.getColor(), position, chessBoard);
            case 'K' -> new King(chessBoard.getColor(), position, chessBoard);
            default -> null;
        });
    }
}
