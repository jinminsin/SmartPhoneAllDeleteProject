
public class runto implements Runnable{
	file f;
	
	public runto(int i)
	{
		f = new file(i);
	}

	public void run(){
		int j=0;
		while(j<256) {
			f.fillbuffer();
			f.print();
			j++;
		}
		
		f.close();
    }
}
