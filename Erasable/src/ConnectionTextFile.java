import java.io.*;
import java.lang.*;
import java.util.*;


public class ConnectionTextFile 
{
	//----------------------
	//變數宣告區
	char[][] dbData;           	// 存放「交易資料庫的每個項目」
	int[] transProfit;			// 記錄「每筆交易的 profit 值」	
	double totalGain=0;      	// 記錄「全部的 Profit 值」

	String FileName;           
	String FileName2;
	int num=0;         					// 資料筆數

    public ConnectionTextFile(String FileName) 
    {
        try
        {
            this.FileName = FileName;
  			//-----------------------------
  			// 01.
  			// 取得「資料庫交易記錄數量」
  			Get_DBSize();
  			//System.out.println(num);
    
  			//-----------------------------
  			// 02.
  			// 宣告「相關陣列大小」
         	dbData = new char[num][];
       		transProfit = new int[num];
  				
  			//-----------------------------
  			// 03.
  			// 讀取「交易資料庫」
  			 buildDatabase();		
  				
            } 
            catch(Exception e) 
            {
                 System.out.println(e.getMessage());
            }
        }
        //------------------------------------------------------------
        //------------------------------------------------------------
        //------------------------------------------------------------
        //回傳資訊
        public char[][] getdbData() // 回傳「資料庫每筆交易記錄裡的item」
        {			
          return dbData;
        }

        public int[] getTransProfit() // 回傳「 每筆交易記錄的 transaction profit」
        {			
            return transProfit;
        }

        public double getTotalGain() // 回傳「 Total Gain 值」
        {  			  			
            return totalGain;
        }  
        //------------------------------------------------------------
        //------------------------------------------------------------
        //------------------------------------------------------------
        
        String line;
        BufferedReader br;
        FileInputStream fis;//useless
        //----------------------------
        // 01.
        // 取得「資料庫交易記錄數量」
        public void Get_DBSize()
        {
            try
            {	
         	//------------------------------
  			//先獲得「資料庫筆數」
         	br = new BufferedReader(new FileReader(FileName+".txt"));
            while ((line=br.readLine())!=null)
            {
                this.num++; //統計資料筆數
            }   	
             br.close();
        }
        catch(Exception e)
        {
  			System.out.println(" Error about getting the size of database : "+e.toString());
  		}	
  		
    }
    //----------------------------
    // 02.
    // 讀取「交易資料庫」	
    public void buildDatabase()
    {
        try
        {
            this.num=0;
            
            br = new BufferedReader(new FileReader(FileName+".txt"));
            while ((line=br.readLine())!=null)//統計資料筆數
            {    
                // 01.
         		// 記錄「此筆交易記錄的 items 和 profit」
         		String[] trans = line.split("_");  
        		String[] data = trans[1].split(", ");			
        		dbData[num] = new char[data.length];					
                transProfit[num] = Integer.parseInt(trans[0]);		//紀錄每筆交易的利潤
                totalGain +=transProfit[num];						//算總利潤
                int pos=0;
                for(int i=0;i<data.length;i++)
                {   // 02. 
         			// 記錄「item」
         			int item = Integer.parseInt(data[i]);         	//第num筆交易第pos個item			
                    dbData[num][pos] = (char)item;
                    //System.out.print((int)dbData[num][pos]);
     				pos++;
         		}
                
                 this.num++;         			
            }	
                 br.close();
        }
        catch(Exception e)
        {
            System.out.println(" Error about loading the dataset into the memory : "+e.toString());
        }
    }
}