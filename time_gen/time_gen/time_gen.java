package time_gen;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class time_gen {

	public static void main(String[] args) {
		try
        {
			String filename;
	        Scanner name=new Scanner(System.in);
	        System.out.println("檔案名稱:");
	        filename = name.next();	        			
			String line;
			int num = 0;
			BufferedReader br = new BufferedReader(new FileReader(filename+".txt"));
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename + "_new.txt"));
	        
			//----統計筆數----
			while ((line=br.readLine())!=null) //讀取那列非空
	        {
	            num++; //統計資料筆數
	        }
	        br.close();
	        
	        //----產生Temporal information----
	        int productnum = 0;
	        do{
	        	Scanner s=new Scanner(System.in);
	        	System.out.println("每個period幾個products(不要超過總筆數)" + num + ":");
	        	productnum = s.nextInt();            //讀取幾個period
	        }while(productnum>num);
	        
	        int startperiod=0;
	        br = new BufferedReader(new FileReader(filename+".txt"));
	        String Output;     //要寫入的那列
	        for(int i=1; i<=num;i++) {
	        	line=br.readLine();
	        	if(i%productnum!=0) {
	        		writefile(line, startperiod, bw);
	        	}
	        	else{
	        		writefile(line, startperiod, bw);
	        		startperiod++;
	          	}	        	
	        }
	        bw.close();
	        br.close();
	    }
	    catch(Exception e)
	    {
	            System.out.println(" Error about loading the dataset into the memory : "+e.toString());
	    }
	    		
	}
	public static void writefile(String line, int startperiod,BufferedWriter bw) {
		String Output = "" + startperiod + '_' + line;
    	System.out.println(Output);
    	try {
    	bw.write(Output); 
   		bw.newLine();}
	    catch(Exception e)
	    {
	            System.out.println(" Error about writing the dataset into the memory : "+e.toString());
	    }
   		
	}

}
