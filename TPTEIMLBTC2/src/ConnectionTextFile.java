import java.io.*;
import java.lang.*;
import java.util.*;


public class ConnectionTextFile 
{
		//----------------------
		//變數宣告區
		char[][] dbData;    	//存放「交易資料庫的每個項目」   EX: dbData[1][2]: 第2筆交易的第3個item
		int[] transProfit;		//記錄「每筆交易的 profit 值」 EX: trandProfit[4] 第5筆交易profit
		int[] period;			//紀錄交易發生的period (從period 0 開始) 
		int[] itemStartPeriod;	//紀錄每個item第一次出現的period
		int[] periodtotalgain;	//紀錄每個period的totalgain EX: periodtotalgain[2] period 2 所有交易的利潤總和
		int lastperiod;			//紀錄最後一筆交易的period
		int maxitem = 0;		//紀錄數值最大的item
		double totalGain = 0;	//記錄「全部的 Profit 值」
		String FileName;		//連接的檔案名稱
		String FileName2;		//好像沒用到?
		int num = 0;         	//交易資料筆數
		int LSPall;				//詳見論文(所有Item都出現的起始時間 MAX(ISP1, ISP2......))

	    public ConnectionTextFile(String FileName) 
	    {
	        try
	        {
	            this.FileName = FileName;
	  			//-----------------------------
	  			// 01.
	  			// 取得「資料庫交易記錄數量」
	  			Get_DBSize();
	  
	  			//-----------------------------
	  			// 02.
	  			// 宣告「相關陣列大小」
	  			dbData = new char[num][];     //num: 記錄幾筆交易
	       		transProfit = new int[num];
	       		period = new int[num];
		  				
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
        
        public int[] getPeriod() // 回傳「 每筆交易記錄的 transaction period」
        {			
            return period;
        }
        
        public int[] getPeriodTotalGain() // 回傳「 每period的total gain」
        {			
            return periodtotalgain;
        }
        
        public int[] getItemStartPeriod() // 回傳「 每period的total gain」
        {			
            return itemStartPeriod;
        }

        public int getLastPeriod() // 回傳「 最後一筆交易的period 」
        {  			  			
            return lastperiod;
        }
        
        public double getTotalGain() // 回傳「 Total Gain 值」
        {  			  			
            return totalGain;
        }  
        
        public int getmaxitem() // 回傳「 Total Gain 值」
        {  			  			
            return maxitem;
        }
        public int getLSPall() // 回傳「 LSPall 值」
        {  			  			
            return LSPall;
        }
        
        //------------------------------------------------------------
        //------------------------------------------------------------
        //------------------------------------------------------------
        
        String line;         //存放讀取每筆交易的變數
        BufferedReader br;
        FileInputStream fis; //好像沒用到?
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
	            while ((line=br.readLine())!=null) //讀取那列非空
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
        //STEP 1:先抓出一筆交易
        //STEP 2:分割並記錄此交易的items和profit和period
        //STEP 3:將一個一個item記錄下來
        //STEP 4:換下一筆交易
	    public void buildDatabase()
	    {
	        try
	        {
	            this.num=0;
	            br = new BufferedReader(new FileReader(FileName+".txt"));
	            while ((line=br.readLine())!=null)//讀取那列非空
	            {    
	                // 01.
	         		// 記錄「此筆交易記錄的 items dbData) 和 profit (transProfit) 和 period (period)」
	         		String[] trans = line.split("_");						//"_"當作separator  trans[0]: period  trans[1]: profit  trans[2]: items
	        		String[] data = trans[2].split(", ");					//", "當作separator data陣列存放此筆交易的items       		
	        		dbData[num] = new char[data.length];					//宣告此筆交易需要放幾個items的陣列
	        		period[num] = Integer.parseInt(trans[0]);				//記錄每筆交易發生的period
	                transProfit[num] = Integer.parseInt(trans[1]);			//記錄每筆交易的利潤
	                totalGain +=transProfit[num];							//記錄整個DB的利潤總和(這個應該沒用到)          
	                int pos= 0;												//每筆交易的第pos個item  依序存入dbData[num]
	                for(int i=0;i<data.length;i++)
	                {
	                    // 02. 
	         			// 記錄每筆交易的每個「item」
	         			int item = Integer.parseInt(data[i]);
	         			if(item>maxitem)
	         				maxitem = item;									//紀錄數值最大的item
	                    dbData[num][pos] = (char)item;						//第num筆交易的第pos個item記錄下來
	                    pos++;
	         		}
	                this.num++;         			
	            }	
	            br.close();
	            
	            
	            //03.
	            //紀錄每個period其totalgain (periodtotalgain)
	            lastperiod = period[num-1];					//紀錄最後一筆交易的period						
	            periodtotalgain = new int [lastperiod+1];   
	            for(int i=0;i<num;i++) {					//逐一看每交易其period, 並加總gain (num: 交易總數)
	            	int transperiod = period[i];
	            	periodtotalgain[transperiod] += transProfit[i];
	            }
	            
	            
	            //04.
	            //紀錄每個item其第一次出現的時間 (itemStartPeriod)  (-1代表此item沒有出現過)
	            itemStartPeriod = new int [maxitem+1];		//紀錄的array key: item     value: period
	            for(int i=0;i<=maxitem;i++) {				//初始化所有item的period都是-1
	            	itemStartPeriod[i] = -1;
	            }
	            
	            br = new BufferedReader(new FileReader(FileName+".txt"));
	            int num1 = 0;											//紀錄第幾筆交易
	            while ((line=br.readLine())!=null) {					//掃DB去抓出每個item第一次出現的時間
	            	String[] trans = line.split("_");
	        		String[] data = trans[2].split(", ");
	        		for(int i=0;i<data.length;i++) {
	        			int item = Integer.parseInt(data[i]);
	        			if(itemStartPeriod[item]==-1)					//若item第一次出現, item的時間還沒被設定
	        				itemStartPeriod[item] = period[num1];
	        		}
	        		num1++;
	            }
	            br.close();
	            
	            
	            //算出LSPall
	            for(int i=0;i<itemStartPeriod.length;i++) {
	            	if(itemStartPeriod[i]>LSPall)
	            		LSPall=itemStartPeriod[i];
	            }     
	          
	        }																
	        catch(Exception e)
	        {
	            System.out.println(" Error about loading the dataset into the memory : "+e.toString());
	        }
	    }
}