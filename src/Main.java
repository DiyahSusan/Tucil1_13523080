import component.Block;
import component.Grid;
import component.Solution;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import javax.swing.JFileChooser;

public class Main{
    
    public static void main(String[] args) {
        String header = " _  _____     ___    _   _  _______  _______  _      ___       ___    ___    _____ \r\n" + //
                        "(_)(  _  )   (  _`\\ ( ) ( )(_____  )(_____  )( )    (  _`\\    (  _`\\ |  _`\\ (  _  )\r\n" + //
                        "| || ( ) |   | |_) )| | | |     /'/'     /'/'| |    | (_(_)   | |_) )| (_) )| ( ) |\r\n" + //
                        "| || | | |   | ,__/'| | | |   /'/'     /'/'  | |  _ |  _)_    | ,__/'| ,  / | | | |\r\n" + //
                        "| || (('\\|   | |    | (_) | /'/'___  /'/'___ | |_( )| (_( )   | |    | |\\ \\ | (_) |\r\n" + //
                        "(_)(___\\_)   (_)    (_____)(_______)(_______)(____/'(____/'   (_)    (_) (_)(_____)\r\n" + //
                        "                                                                                   \r\n" + //
                        "                                                                                   ";
        System.out.println(header);

        boolean valid = false;

        // memilih file
        while (!valid){
            valid = true;
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Pilih File Test Case");
            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                

                try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                    // membaca dimensi
                    String[] dimensions = br.readLine().trim().split(" ");
                    int rows = Integer.parseInt(dimensions[0]); 
                    int cols = Integer.parseInt(dimensions[1]); 

                    int blockCount = Integer.parseInt(dimensions[2]);
                    
                    if(rows <= 0 || cols <= 0){
                        valid = false;
                    }

                    // type
                    String type = br.readLine().trim();

                    // membaca blocks dan menyimpannya di sebuah map
                    Map<Character, Block> blocks = new LinkedHashMap<>();
                    Character currentBlock = null;
                    int counter = 0;

                    while (blocks.size() < blockCount || currentBlock != null && valid) { 
                        String line = br.readLine();
                        if (line != null) {
                            // kalau ada kasus space maka akan diubah sebagai dot
                            line = line.replace(' ', '.'); 
                    
                            if (!line.trim().isEmpty()) { 
                                char firstChar = line.trim().charAt(0);
                    
                                // jika ada block baru
                                if (currentBlock == null || (firstChar != '.' && currentBlock != firstChar)) {
                                    currentBlock = firstChar;
                                    blocks.putIfAbsent(currentBlock, new Block(currentBlock));
                                }
                    
                                Block block = blocks.get(currentBlock);
                                if (block != null) {
                                    block.addRow(line);
                                } else {
                                    System.err.println("Error: currentBlock tidak ditemukan dalam blocks.");
                                }
                            } else {
                                currentBlock = null;
                            }
                        } else {
                            break; 
                        }
                    }
                    
                   if (valid){
                        // menghitung jumlah kotak
                        for (Block block : blocks.values()) {
                            char [][] shape = block.getShape();
                            int baris = shape.length;
                            int kolom = shape[0].length;
                            for (int i=0; i<baris; i++){
                                for (int j=0; j<kolom; j++){
                                    if(shape[i][j] != '.'){
                                        counter += 1;
                                    }
                                }
                            }
                        }
                   }

                    // validasi input
                    if (blocks.size() > 26){
                        valid = false;
                    }
                    else if (blockCount != blocks.size()){
                        valid = false;
                    }
                    else if (counter != rows * cols){
                        valid = false;
                    }


                    if (type.equalsIgnoreCase("DEFAULT") && valid){
                        // grid papan
                        Grid grid = new Grid(rows, cols);
                        // menghitung waktu
                        long startTime = System.currentTimeMillis();
                        if (Solution.solutionBF(grid, blocks, 0, 0)) {
                            System.out.println("Puzzle berhasil diselesaikan:");
                            grid.printGrid();
                        } else {
                            System.out.println("Tidak ada solusi untuk puzzle.");
                        }
                        long endTime = System.currentTimeMillis();

                        long duration = endTime - startTime;
                        System.out.println("Durasi : " + duration + " ms");

                        Scanner scanner = new Scanner(System.in);

                        System.out.println();
                        System.out.print("Apakah anda ingin menyimpan solusi? (ya/tidak) : ");
                        String save = scanner.nextLine();

                        if (save.equalsIgnoreCase("ya")){
                            System.out.print("Masukkan nama file yang ingin disimpan : ");
                            String fileName = scanner.nextLine();
                            grid.saveGrid("test/" + fileName);
                            grid.saveGridAsPNG("test/" + fileName);
                        }
                    }
                    else{
                        valid = false;
                        System.out.println("Input tidak valid!");
                        System.out.println("Silahkan memasukkan test case lainnya!");

                    }
                } catch (IOException e) {
                    System.out.println("Terjadi kesalahan saat membaca file: " + e.getMessage());
                } 
            } else {
                System.out.println("Tidak ada file yang dipilih.");
            }        
        }
    }
}