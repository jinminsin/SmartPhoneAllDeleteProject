
import java.io.File;
import java.io.FileOutputStream;
import java.security.SecureRandom;

public class file {
	FileOutputStream FOS;
	File f;
	SecureRandom random = new SecureRandom();
	byte[] buffer = new byte[4*1024*1024];//1mb
	
	public file(int i)
	{
		f = new File("File " + i + ".txt");
		try {
			FOS = new FileOutputStream(f);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void fillbuffer()
	{
		random.nextBytes(buffer);
	}
	
	public void close()
	{
		try {
			FOS.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void print()
	{
		try {
			FOS.write(buffer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
