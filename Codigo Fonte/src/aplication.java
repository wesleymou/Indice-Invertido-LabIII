import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import util.MaiorRegistro;

class aplication {

	public static void main(String[] args) {
		MaiorRegistro maiorRegistro = new MaiorRegistro();
		maiorRegistro.fazAí("files/indice-invertido.txt", ";");
		System.out.println("Porra!!");
		
		new File("files/test.bin").delete();
		try (RandomAccessFile file = new RandomAccessFile(new File("files/test.bin") , "rw")){
			file.seek(0);
			file.writeInt(42);
			System.out.println("int: " + file.length() + "|" +  Integer.BYTES + "|" + Integer.SIZE/8);
			
			file.seek(0);
			file.writeFloat(42);
			System.out.println("float: " + file.length() + "|" + Float.BYTES + "|" + Float.SIZE/8);
			
			file.seek(0);
			file.writeDouble(42);
			System.out.println("double: " + file.length() + "|" + Double.BYTES + "|" + Double.SIZE/8);
			
			file.seek(0);
			file.writeUTF("abcdefghij");
			System.out.println("abcdefghij: " + file.length() + "|" + "abcdefghij".length() + "|" + "abcdefghij".getBytes().length);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}