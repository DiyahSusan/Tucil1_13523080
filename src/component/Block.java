package component;

public class Block {
    private char letter; 
    private char[][] shape; 

    public Block(char letter) {
        this.letter = letter;
        this.shape = new char[0][0];
    }

    public char[][] getShape() {
        return shape; // representasi blok sebagai matriks 2D
    }
    
    public void addRow(String row) {
        int currentRows = shape.length;
        int rowLength = row.length();
    
        int maxCols = 0;
        if (currentRows > 0) {
            maxCols = shape[0].length;
        }
        maxCols = Math.max(maxCols, rowLength);
    
        // matriks dengan dimensi baru
        char[][] newShape = new char[currentRows + 1][maxCols];
    
        for (int i = 0; i < currentRows; i++) {
            for (int j = 0; j < maxCols; j++) {
                newShape[i][j] = (j < shape[i].length) ? shape[i][j] : '.'; 
            }
        }
    
        for (int j = 0; j < maxCols; j++) {
            newShape[currentRows][j] = (j < rowLength) ? row.charAt(j) : '.';
        }
    
        shape = newShape;
    }
    

    public void rotateClockwise() {
        int rows = shape.length;
        int cols = shape[0].length;

        char[][] rotatedShape = new char[cols][rows];

        // rotasi matriks 90 derajat searah jarum jam
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotatedShape[j][rows - 1 - i] = shape[i][j];
            }
        }

        shape = rotatedShape;
    }

    public void mirrorHorizontally() {
        int rows = shape.length;
        for (int r = 0; r < rows / 2; r++) {
            char[] temp = shape[r];
            shape[r] = shape[rows - 1 - r];
            shape[rows - 1 - r] = temp;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Block ").append(letter).append(":\n");
        for (char[] row : shape) {
            sb.append(new String(row)).append("\n");
        }
        return sb.toString();
    }
}

