package daoc.mpj;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class MiMatriz implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String IMG_PATH_01 = "zoom_20x20.png";
	public static final String IMG_PATH_01_B = "zoom_20x20_b.png";
	public static final String IMG_PATH_02 = "zoom_50x50.png";
	public static final String IMG_PATH_02_B = "zoom_50x50_b.png";	
	public static final String IMG_PATH_03 = "zoom_512x512.png";
	public static final String IMG_PATH_03_B = "zoom_512x512_B.png";	
	
	double[][] matriz;
	
	MiMatriz() {}
	MiMatriz(int fs, int cs) {
		matriz = new double[fs][cs];
	}
	MiMatriz(double[][] matriz) {
		this.matriz = matriz;
	}
	
	double[][] getMatriz() {
		return matriz;
	}
	void setMatriz(double[][] matriz) {
		this.matriz = matriz;
	}
	
	int getFilas() {
		return matriz==null ? 0 : matriz.length;
	}
	int getCols() {
		return matriz==null ? 0 : matriz[0].length;
	}
	
	double getValor(int f, int c) {
		return matriz[f][c];
	}
	void setValor(double valor, int f, int c) {
		matriz[f][c] = valor;
	}		
	
	int cuentaVecinos(int f, int c) {
		int fi, ff, ci, cf;
		fi = f-1 < 0 ? 0 : f-1;
		ff = f+1 < getFilas() ? f+1 : f;
		ci = c-1 < 0 ? 0 : c-1;
		cf = c+1 < getCols() ? c+1 : c;
		return (ff-fi+1) * (cf-ci+1);
	}

	double sumaVecinos(int f, int c) {
		int fi, ff, ci, cf;
		fi = f-1 < 0 ? 0 : f-1;
		ff = f+1 < getFilas() ? f+1 : f;
		ci = c-1 < 0 ? 0 : c-1;
		cf = c+1 < getCols() ? c+1 : c;
		
		double suma = 0;
		for(int row = fi; row <= ff; row++) {
			for(int col = ci; col <= cf; col++) {
				suma += getValor(row, col);
			}
		}		
		
		return suma;
	}
	
	MiMatriz clonar() {
		double[][] copy = Arrays.stream(matriz).map(double[]::clone).toArray(double[][]::new);
		return new MiMatriz(copy);
	}
	
	MiMatriz getSubMatriz(int initF, int finF, int initC, int finC) {
		MiMatriz subM = new MiMatriz(finF-initF+1, finC-initC+1);
		for(int f = initF; f <= finF; f++) {
			for(int c = initC; c <= finC; c++) {
				subM.setValor(this.getValor(f, c), f-initF, c-initC);
			}
		}
		return subM;
	}
	
	void setSubMatriz(MiMatriz data, int initF, int finF, int initC, int finC) {
		for(int f = initF; f <= finF; f++) {
			for(int c = initC; c <= finC; c++) {
				this.setValor(data.getValor(f-initF, c-initC), f, c);
			}
		}
	}	
	
	MiMatriz[] divideEnBloques(int numMat, int bloque) {
		MiMatriz[] chicas = new MiMatriz[numMat];
		int counter = 0;
		for(int f = 0; f < numMat; f+=bloque) {
			for(int c = 0; c < numMat; c+=bloque) {
				chicas[counter++] = this.getSubMatriz(f, f+bloque-1, c, c+bloque-1);
			}
		}
		return chicas;
	}
	
	static MiMatriz reuneBloques(MiMatriz[] chicas) {
		int numMat = chicas.length;
		int bloque = chicas[0].getCols();
		int matLado = (int) Math.sqrt(numMat);
		int cellLado = matLado * bloque;
		MiMatriz nueva = new MiMatriz(cellLado, cellLado);
		
		int counter = 0;
		for(int f = 0; f < numMat; f+=bloque) {
			for(int c = 0; c < numMat; c+=bloque) {
				nueva.setSubMatriz(chicas[counter++], f, f+bloque-1, c, c+bloque-1);
			}
		}
		
		return nueva;
	}
	
	static MiMatriz[] desdeImagenEscalada(String imgFile, int newWidth, int newHeight) {
		try {
			BufferedImage img = ImageIO.read(new File(imgFile));
		
	        Image tmp = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
	        BufferedImage destimg = new BufferedImage(newWidth, newHeight, img.getType());
	
	        Graphics2D g2d = destimg.createGraphics();
	        g2d.drawImage(tmp, 0, 0, null);
	        g2d.dispose();
	
	        return desdeImagen(img);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	static MiMatriz[] desdeImagen(String imgFile) {
		try {
			BufferedImage img = ImageIO.read(new File(imgFile));
			return desdeImagen(img);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	static MiMatriz[] desdeImagen(BufferedImage img) {
		MiMatriz[] rgb = new MiMatriz[3];

        double[][] r = new double[img.getHeight()][img.getWidth()];
        double[][] g = new double[img.getHeight()][img.getWidth()];
        double[][] b = new double[img.getHeight()][img.getWidth()];
        
        for(int row = 0; row < img.getHeight(); row++) {
            for(int col = 0; col < img.getWidth(); col++) {
                Color c = new Color(img.getRGB(col, row));
                r[row][col] = c.getRed();
                g[row][col] = c.getGreen();
                b[row][col] = c.getBlue();
            }
        }
        
        rgb[0] = new MiMatriz(r);
        rgb[1] = new MiMatriz(g);
        rgb[2] = new MiMatriz(b);
        
        return rgb;   			
	}
	
	static void haciaImagen(MiMatriz[] rgb, String imgFile) {	
		BufferedImage img = new BufferedImage(rgb[0].getFilas(), rgb[0].getCols(), BufferedImage.TYPE_INT_RGB);
		
        for(int row = 0; row < img.getHeight(); row++) {
            for(int col = 0; col < img.getWidth(); col++) {
                Color c = new Color(
                		(int)rgb[0].getValor(row, col), 
                		(int)rgb[1].getValor(row, col), 
                		(int)rgb[2].getValor(row, col));
                img.setRGB(col, row, c.getRGB());
            }
        }  		
		   
        try {
        	String ext = imgFile.substring(imgFile.length()-3);
        	File outFile = new File(imgFile);
			ImageIO.write(img, ext, outFile);
		} catch (IOException e) {
			e.printStackTrace();
		}        
	}
	
	static MiMatriz ejemplo01 () {
		double[][] matriz = {
			{1, 2, 3, 10, 20, 30, 100, 200, 300},
			{1, 2, 3, 10, 20, 30, 100, 200, 300},
			{1, 2, 3, 10, 20, 30, 100, 200, 300},
			{4, 5, 6, 40, 50, 60, 400, 500, 600},
			{4, 5, 6, 40, 50, 60, 400, 500, 600},
			{4, 5, 6, 40, 50, 60, 400, 500, 600},
			{7, 8, 9, 70, 80, 90, 700, 800, 900},
			{7, 8, 9, 70, 80, 90, 700, 800, 900},
			{7, 8, 9, 70, 80, 90, 700, 800, 900}
		};
		return new MiMatriz(matriz);		
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < getFilas(); i++) {
			str.append(Arrays.toString(matriz[i]));
			str.append('\n');
		}
		return str.toString();
	}
	
	public static void main(String[] args) {
//		MiMatriz[] rgb = MiMatriz.desdeImagen(IMG_PATH_03);
//		MiMatriz.haciaImagen(rgb, IMG_PATH_03_B);
		MiMatriz uno = MiMatriz.ejemplo01();
		int cuenta = uno.cuentaVecinos(8, 8);
		double suma = uno.sumaVecinos(8, 8);
		System.out.println(cuenta);
		System.out.println(suma);
		System.out.println(suma/cuenta);
		System.out.println("Listo");
	}
	
}
