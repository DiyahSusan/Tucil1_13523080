package component;
import java.util.Map;

public class Solution {
    public static boolean solutionBF(Grid grid, Map<Character, Block> blocks, int currentBlockIndex, int count) {
        if (currentBlockIndex >= blocks.size()) {
            System.out.println("Banyak percobaan : " + count);
            // semua blok telah ditempatkan, puzzle selesai
            return true;
        }
    
        // ambil blok saat ini berdasarkan indeks
        Block currentBlock = (Block) blocks.values().toArray()[currentBlockIndex];
    
        char[][] originalGrid = grid.copyGrid();

        // coba semua posisi di grid
        for (int row = 0; row < grid.getRows(); row++) {
            for (int col = 0; col < grid.getCols(); col++) {
                // rotasi
                for (int rotation = 0; rotation < 4; rotation++) {
                    count += 1;
                    if (grid.canPlaceBlock(currentBlock, row, col)) {
                        grid.placeBlock(currentBlock, row, col);

                        // rekursi untuk blok berikutnya
                        if (solutionBF(grid, blocks, currentBlockIndex + 1, count)) {
                            
                            return true;
                        }

                        // backtrack jika solusi gagal
                        grid.setGrid(originalGrid);
                    }

                    currentBlock.rotateClockwise();
                }

                // mirror
                currentBlock.mirrorHorizontally(); 
                for (int rotation = 0; rotation < 4; rotation++) {
                    count += 1;
                    if (grid.canPlaceBlock(currentBlock, row, col)) {
                        grid.placeBlock(currentBlock, row, col);

                        if (solutionBF(grid, blocks, currentBlockIndex + 1, count)) {
                            return true;
                        }

                        grid.setGrid(originalGrid);
                    }

                    currentBlock.rotateClockwise(); 
                }
            }
        }
        return false; 
    }
}
