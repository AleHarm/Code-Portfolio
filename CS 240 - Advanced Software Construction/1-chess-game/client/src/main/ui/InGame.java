package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class InGame {

    private static final String BG_COLOR1 = SET_BG_COLOR_LIGHT_GREY;
    private static final String BG_COLOR2 = SET_BG_COLOR_BLUE;
    private static final int BOARD_WIDTH = 8;
    private static final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);


    public static void inGame(){

        drawInitialBoard();

        while(true) {
            listMenuOptions();
            String userInput = getUserInput();

            if(userInput.equals("leave")){

                break;
            }
        }
    }

    private static void drawInitialBoard(){

        ChessBoardImple board = new ChessBoardImple();
        board.resetBoard();
        out.print(ERASE_SCREEN);

        drawBoardWhite(board);
        drawSpacer();
        drawBoardBlack(board);
    }

    private static void drawCornerBlock(){

        out.print(SET_BG_COLOR_YELLOW);
        out.print("   ");
    }

    private static void drawBorderRowWhite(){

        out.print(SET_TEXT_BOLD);
        out.print(SET_TEXT_COLOR_DARK_GREY);

        drawCornerBlock();
        for(int i = 0; i < BOARD_WIDTH; i++){

            out.print(" " + (char)('a' + i) + " ");
        }
        drawCornerBlock();
        out.print(RESET_BG_COLOR);
        out.print("\n");
    }

    private static void drawBorderRowBlack(){

        drawCornerBlock();
        out.print(SET_TEXT_BOLD);
        out.print(SET_TEXT_COLOR_DARK_GREY);
        for(int i = 0; i < BOARD_WIDTH; i++){

            out.print(" " + (char)('h' - i) + " ");
        }
        out.print(RESET_TEXT_COLOR);
        out.print(RESET_TEXT_BOLD_FAINT);
        drawCornerBlock();
        out.print(RESET_BG_COLOR);
        out.print("\n");
    }

    private static void drawSideBlockWhite(int num){

        num++;

        out.print(SET_BG_COLOR_YELLOW);
        out.print(SET_TEXT_BOLD);
        out.print(SET_TEXT_COLOR_DARK_GREY);
        out.print(" " + num + " ");
        out.print(RESET_TEXT_COLOR);
        out.print(RESET_TEXT_BOLD_FAINT);
    }

    private static void drawSideBlockBlack(int num){

        num++;

        out.print(SET_BG_COLOR_YELLOW);
        out.print(SET_TEXT_BOLD);
        out.print(SET_TEXT_COLOR_DARK_GREY);
        out.print(" " + num + " ");
        out.print(RESET_TEXT_COLOR);
        out.print(RESET_TEXT_BOLD_FAINT);
    }

    private static void drawBoardBlack(ChessBoardImple board){

        boolean isBGBlue = false;
        ChessPiece currentPiece;
        ChessPosition currentPosition;

        drawBorderRowBlack();

        out.print(BG_COLOR1);

        for(int i = 0; i < BOARD_WIDTH; i++){

            drawSideBlockWhite(i);
            isBGBlue = !isBGBlue;

            for(int j = 0; j < BOARD_WIDTH; j++){

                isBGBlue = switchBGColor(isBGBlue);
                out.print(" ");
                currentPosition = new ChessPositionImple(i + 1, j + 1);

                currentPiece = board.getPiece(currentPosition);

                if(currentPiece != null){

                    if(currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE){

                        out.print(SET_TEXT_COLOR_WHITE);
                    }else{
                        out.print(SET_TEXT_COLOR_BLACK);
                    }

                    switch(currentPiece.getPieceType()){

                        case ROOK -> out.print("R");
                        case PAWN -> out.print("P");
                        case KNIGHT -> out.print("N");
                        case KING -> out.print("K");
                        case QUEEN -> out.print("Q");
                        case BISHOP -> out.print("B");
                    }
                }else{
                    out.print(" ");
                }
                out.print(" ");
            }

            drawSideBlockWhite(i);
            out.print(RESET_BG_COLOR);
            out.print("\n");
        }

        drawBorderRowBlack();
    }

    private static void drawBoardWhite(ChessBoardImple board){

        boolean isBGBlue = false;
        ChessPiece currentPiece;
        ChessPosition currentPosition;

        drawBorderRowWhite();

        out.print(BG_COLOR1);

        for(int i = BOARD_WIDTH - 1; i >= 0; i--){

            drawSideBlockBlack(i);
            isBGBlue = !isBGBlue;

            for(int j = BOARD_WIDTH - 1; j >= 0; j--){

                isBGBlue = switchBGColor(isBGBlue);
                out.print(" ");
                currentPosition = new ChessPositionImple(i + 1, j + 1);

                currentPiece = board.getPiece(currentPosition);

                if(currentPiece != null){

                    if(currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE){

                        out.print(SET_TEXT_COLOR_WHITE);
                    }else{
                        out.print(SET_TEXT_COLOR_BLACK);
                    }

                    switch(currentPiece.getPieceType()){

                        case ROOK -> out.print("R");
                        case PAWN -> out.print("P");
                        case KNIGHT -> out.print("N");
                        case KING -> out.print("K");
                        case QUEEN -> out.print("Q");
                        case BISHOP -> out.print("B");
                    }
                }else{
                    out.print(" ");
                }
                out.print(" ");
            }

            drawSideBlockBlack(i);
            out.print(RESET_BG_COLOR);
            out.print("\n");
        }

        drawBorderRowWhite();
    }

    private static boolean switchBGColor(boolean isBGBlue){

        if(isBGBlue){
            out.print(BG_COLOR1);
            isBGBlue = false;
        }else{
            out.print(BG_COLOR2);
            isBGBlue = true;
        }

        return isBGBlue;
    }

    private static void drawSpacer(){

        out.print(SET_TEXT_BOLD);
        out.print(SET_TEXT_COLOR_DARK_GREY);

        for(int i = 0; i < 10; i++){

            out.print("   ");
        }
        out.print(RESET_BG_COLOR);
        out.print("\n");
    }

    private static void listMenuOptions(){

        System.out.print("Type \"leave\" to exit the current game\n");
        System.out.print("Type \"help\" to see the menu options again\n\n");
        System.out.print("[IN_GAME] >>> ");
    }

    private static String getUserInput(){

        Scanner myObj = new Scanner(System.in);
        String userInput = myObj.nextLine();
        System.out.print("\n");
        return userInput.toLowerCase();
    }
}
