
import java.security.SecureRandom;
import java.util.*;

public class randomw {
	
	public static void main(String[] args) {
		Thread ct[] = new Thread[10];
		
    long beforeTime = System.currentTimeMillis();
    for(int count = 0;count < 1;count++) {
       for(int i=0;i<ct.length;i++)
       {
    	   	ct[i] = new Thread(new runto(count*10 + i));
    	   	ct[i].start();
       }
       
       for(int i=0;i<ct.length;i++)
       {
    	   try{ct[i].join();}
    	   catch(Exception e){};
       }
    }
    
    long afterTime = System.currentTimeMillis();
    long secDiffTime = (afterTime - beforeTime)/1000;
    System.out.println("소요시간 : " + secDiffTime);
	}
}
