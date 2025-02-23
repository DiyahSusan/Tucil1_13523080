package component;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.imageio.ImageIO;



public class Grid {
    private char[][] grid;
    private int rows;
    private int cols;

    public Grid (int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        grid = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = '.'; 
            }
        }
    }

    public int getRows(){
        return rows;
    }

    public int getCols(){
        return cols;
    }

    public boolean canPlaceBlock(Block block, int startRow, int startCol) {
        char[][] shape = block.getShape();
        int blockRows = shape.length;
        int blockCols = shape[0].length;
    
        for (int i = 0; i < blockRows; i++) {
            for (int j = 0; j < blockCols; j++) {
                if (shape[i][j] != '.') {
                    int gridRow = startRow + i;
                    int gridCol = startCol + j;
    
                    // cek apakah posisi di dalam grid dan tidak terisi
                    if (gridRow >= rows || gridCol >= cols || grid[gridRow][gridCol] != '.') {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    public void placeBlock(Block block, int startRow, int startCol) {
        char[][] shape = block.getShape();
        int blockRows = shape.length;
        int blockCols = shape[0].length;
    
        for (int i = 0; i < blockRows; i++) {
            for (int j = 0; j < blockCols; j++) {
                if (shape[i][j] != '.') {
                    grid[startRow + i][startCol + j] = shape[i][j];
                }
            }
        }
    }
    
    public char[][] copyGrid() {
        char[][] copy = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(grid[i], 0, copy[i], 0, cols);
        }
        return copy;
    }
    
    public void setGrid(char[][] newGrid) {
        for (int i = 0; i < rows; i++) {
            System.arraycopy(newGrid[i], 0, grid[i], 0, cols);
        }
    }
   
    public void printGrid() {
        Map<Character, String> colorMap = new LinkedHashMap<>();
        String resetColor = "\u001B[0m";

        // print dengan warna
        for (int i = 0; i < 26; i++) {
            char block = (char) ('A' + i); 
            String color = "\u001B[38;5;" + (i*18) + "m"; 
            colorMap.put(block, color);
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char cell = grid[i][j];
                String color = colorMap.getOrDefault(cell, "\u001B[37m"); 
                System.out.print(color + cell + " " + resetColor);
            }
            System.out.println();
        }
    }

    public void saveGrid(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    char cell = grid[i][j];
                    writer.write(cell + " ");
                }
                writer.write(System.lineSeparator());
            }
            System.out.println();
            System.out.println("Berhasil menyimpan solusi.");
        } catch (IOException e) {
            System.out.println("Error saat menyimpan solusi : " + e.getMessage());
        }
    }  

    public void saveGridAsPNG(String filename) {
        final int size = 30;
        final int padding = 2;
        
        // dimensi
        int width = cols * size;
        int height = rows * size;
        
        
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        
        BufferedImage image = gc.createCompatibleImage(width, height, Transparency.OPAQUE);
        Graphics2D g2d = image.createGraphics();
        
        // agar text ngga pecah
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // background
        g2d.setPaint(java.awt.Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        
        Map<Character, Color> colorMap = new LinkedHashMap<>();
        for (int i = 1; i < 27; i++) {
            char block = (char) ('A' + i);
            int red = (i * 15) % 256;
            int green = (i * 30) % 256;
            int blue = (i * 45) % 256;
            Color color = new Color(red, green, blue); 
            colorMap.put(block, color);
        }
        
        Color defaultColor = Color.WHITE;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int x = j * size;
                int y = i * size;
                
                char cell = grid[i][j];
                if (cell != ' ') {  
                    
                    Color cellColor = colorMap.getOrDefault(cell, defaultColor);
                    
                    g2d.setPaint(cellColor);
                    g2d.fillRect(x + padding, y + padding, size - 2*padding, size - 2*padding);
                    
                    g2d.setPaint(java.awt.Color.BLACK);
                    g2d.drawRect(x + padding, y + padding, size - 2*padding, size - 2*padding);
                    
                    g2d.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, size/2));
                    FontMetrics fm = g2d.getFontMetrics();
                    String text = String.valueOf(cell);
                    int textX = x + (size - fm.stringWidth(text))/2;
                    int textY = y + ((size - fm.getHeight())/2) + fm.getAscent();
                    g2d.setPaint(java.awt.Color.BLACK); 
                    g2d.drawString(text, textX, textY);
                }
            }
        }
        
        g2d.dispose();
        
        // menyimpan gambar
        try {
            if (!filename.toLowerCase().endsWith(".png")) {
                filename += ".png";
            }
            File outputFile = new File(filename);
            ImageIO.write(image, "PNG", outputFile);
            System.out.println("Berhasil menyimpan gambar.");
        } catch (IOException e) {
            System.out.println("Error saat menyimpan gambar: " + e.getMessage());
        }
    }
}
