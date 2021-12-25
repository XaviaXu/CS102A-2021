package view;

import components.ChessGridComponent;
import model.ChessPiece;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ChessBoardPanel extends JPanel {
    private final int CHESS_COUNT = 8;
    private final int BLACK = 1;
    private final int WHITE = -1;
    private ChessGridComponent[][] chessGrids;
    private int[][] board;
    private static int[][] dir = {
            {0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
    };

    public ChessBoardPanel(int width, int height) {
        this.setVisible(true);
        this.setFocusable(true);
        this.setLayout(null);
        this.setBackground(Color.BLACK);
        int length = Math.min(width, height);
        this.setSize(length, length);
        ChessGridComponent.gridSize = length / CHESS_COUNT;
        ChessGridComponent.chessSize = (int) (ChessGridComponent.gridSize * 0.8);
        System.out.printf("width = %d height = %d gridSize = %d chessSize = %d\n",
                width, height, ChessGridComponent.gridSize, ChessGridComponent.chessSize);

        initialChessGrids();//return empty chessboard
        initialGame();//add initial four chess

        repaint();
    }

    /**
     * set an empty chessboard
     */
    public void initialChessGrids() {
        chessGrids = new ChessGridComponent[CHESS_COUNT][CHESS_COUNT];

        //draw all chess grids
        for (int i = 0; i < CHESS_COUNT; i++) {
            for (int j = 0; j < CHESS_COUNT; j++) {
                ChessGridComponent gridComponent = new ChessGridComponent(i, j);
                gridComponent.setLocation(j * ChessGridComponent.gridSize, i * ChessGridComponent.gridSize);
                chessGrids[i][j] = gridComponent;
                this.add(chessGrids[i][j]);
            }
        }
    }

    /**
     * initial origin four chess
     */
    public void initialGame() {
        board = new int[CHESS_COUNT][CHESS_COUNT];
        chessGrids[3][3].setChessPiece(ChessPiece.BLACK);
        board[3][3] = BLACK;
        chessGrids[3][4].setChessPiece(ChessPiece.WHITE);
        board[3][4] = WHITE;
        chessGrids[4][3].setChessPiece(ChessPiece.WHITE);
        board[4][3] = WHITE;
        chessGrids[4][4].setChessPiece(ChessPiece.BLACK);
        board[4][4] = BLACK;
    }
    
//    public void restart(){
//        for (int i = 0; i < CHESS_COUNT; i++) {
//            for (int j = 0; j < CHESS_COUNT; j++) {
//                chessGrids[i][j].clearChess();
//            }
//        }
//    }

    public void update(int ini_x,int ini_y,int color){
        board[ini_x][ini_y]=color;
        int[] flipDir = new int[dir.length];
        for (int i = 0; i <dir.length ; i++) {
            int row = dir[i][0];
            int col = dir[i][1];
            int x = row + ini_x;
            int y = col + ini_y;
            if ((x >= 0 && x <= 7) && (y >= 0 && y <= 7) && board[x][y] != 0 && board[x][y] != color) {
                while ((x >= 0 && x <= 7) && (y >= 0 && y <= 7)) {
                    if (board[x][y] == color) {
                        flipDir[i]++;
                    } else if (board[x][y] == 0) {
                        break;
                    }
                    x += row;
                    y += col;
                }
            }
        }
        for (int i = 0; i < flipDir.length; i++) {
            if(flipDir[i]!=0){
                int row = dir[i][0];
                int col = dir[i][1];
                int x = row + ini_x;
                int y = col + ini_y;
                while(true){
                    if(board[x][y]==color){
                        break;
                    }else{
                        board[x][y] = -board[x][y];
                        //todo: update color
                        chessGrids[x][y].flip();
                        System.out.printf("%d %d\n",x,y);

                    }
                    x += row;
                    y += col;
                }
            }
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    public boolean canClickGrid(int ini_x, int ini_y, ChessPiece currentPlayer) {
        int color = currentPlayer==ChessPiece.BLACK?BLACK:WHITE;
        for (int i = 0; i < 8; i++) {
            int row = dir[i][0];
            int col = dir[i][1];
            int x = row + ini_x;
            int y = col + ini_y;
            if ((x >= 0 && x <= 7) && (y >= 0 && y <= 7) && board[x][y] != 0 && board[x][y] != color) {
                while ((x >= 0 && x <= 7) && (y >= 0 && y <= 7)) {
                    if (board[x][y] == color) {
                        return true;
                    } else if (board[x][y] == 0) {
                        break;
                    }
                    x += row;
                    y += col;
                }
            }

        }
        return false;
    }

    public int getScore(int color){
        int cnt = 0;
        for (int i = 0; i < CHESS_COUNT ; i++) {
            for (int j = 0; j < CHESS_COUNT; j++) {
                if(board[i][j]==color){
                    cnt++;
                }
            }
        }
        return cnt;
    }
}
