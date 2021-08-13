import java.io.*;
import java.lang.*;
import java.util.*;


public class ConnectionTextFile 
{
		//----------------------
		//變數宣告區
		char[][] dbData;    	//存放「交易資料庫的每個項目」   EX: dbData[1][2]: 第2筆交易的第3個item
		int[] transProfit;		//記錄「每筆交易的 profit 值」 EX: trandProfit[4] 第5筆交易profit
		int[] period;			//紀錄每筆交易發生的period (從period 0 開始) 
		int[][] lifespan;	    //紀錄每個item起始時間和結束時間 [item][0]:起始  [item][1]:結束
		int[] periodtotalgain;	//紀錄每個period的totalgain EX: periodtotalgain[2] period 2所有交易的利潤總和
		int[] periodproductnum; //紀錄每個period有幾個products
		int lastperiod;			//紀錄最後一筆交易的period
		int maxitem = 0;		//紀錄數值最大的item
		double totalGain = 0;	//記錄「全部的 Profit 值」
		String FileName;		//連接的檔案名稱
		String FileName2;		//好像沒用到?
		int num = 0;         	//交易資料筆數

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

        public int[] getperiodproductnum() // 回傳「 每筆交易記錄的 transaction profit」
        {			
            return periodproductnum;
        }
        
        public int[] getTransProfit() // 回傳「 每個period有幾個products」
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
        
        public int[][] getlifespan() // 回傳起始和結束時間
        {			
            return lifespan;
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
	         		// 記錄「此筆交易記錄的 items (dbData) 和 profit (transProfit) 和 period (period)」
	         		String[] trans = line.split("_");						//"_"當作separator  trans[0]: period  trans[1]: profit  trans[2]: items
	        		String[] data = trans[2].split(", ");					//", "當作separator data陣列存放此筆交易的items       		
	        		dbData[num] = new char[data.length];					//宣告此筆交易需要放幾個items的陣列
	        		period[num] = Integer.parseInt(trans[0]);				//記錄每筆交易發生的period
	                transProfit[num] = Integer.parseInt(trans[1]);			//記錄每筆交易的利潤profit
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
	            //紀錄每個item其第一次出現的時間和結束時間 (lifespan)  	(起始和結束都是-1代表此item沒有出現過)
	            lifespan = new int [maxitem+1][2];		//[item][0]:起始時間    [item][1]:結束時間
	            for(int i=0;i<=maxitem;i++) {					//起始和結束時間初始化為-1
	            	lifespan[i][0] = -1;
	            	lifespan[i][1] = -1;
	            }

	            //----紀錄項目起始時間----
	            for(int j=0;j<=dbData.length-1;j++) {
	            	for(int c=0;c<dbData[j].length;c++) {
	            		int item = (int)dbData[j][c];
	            		if(lifespan[item][0]==-1)					//從頭掃到尾, 若item第一次出現, item的起始時間還沒被設定
	            			lifespan[item][0] = period[j];			//設定為第j筆交易的period
	            	}
	            }
	            
	            /*br = new BufferedReader(new FileReader(FileName+".txt"));
	            int num1 = 0;											//紀錄第幾筆交易
	            while ((line=br.readLine())!=null) {					//掃DB去抓出每個item第一次出現的時間
	            	String[] trans = line.split("_");
	        		String[] data = trans[2].split(", ");
	        		for(int i=0;i<data.length;i++) {
	        			int item = Integer.parseInt(data[i]);
	        			if(lifespan[item][0]==-1)					//若item第一次出現, item的時間還沒被設定
	        				lifespan[item][0] = period[num1];
	        		}
	        		num1++;
	            }
	            br.close();*/
	            
	            //----紀錄項目結束時間----
	            for(int j = dbData.length-1; j>=0 ; j--) {
	            	for(int c=0;c<dbData[j].length;c++) {
	            		int item = (int)dbData[j][c];
	            		if(lifespan[item][1]==-1)					//從尾掃到頭, 若item第一次出現, item的結束時間還沒被設定
	            			lifespan[item][1] = period[j];			//設定為第j筆交易的period
	            	}
	            }
	            
	            
	            
	            
	            //05.
	            //紀錄每個period有幾個products (periodproductnum)
	            periodproductnum = new int[lastperiod+1];
	            for(int k=0;k<=lastperiod;k++) {
	            	int num = 0;
	            	for(int j=0;j<=period.length-1;j++) {
	            		if(period[j]==k) {
	            			num++;
	            		}
	            	}
	            	periodproductnum[k] = num;
	            }
	            /*for(int l=0;l<periodproductnum.length;l++)
	            	System.out.print(periodproductnum[l]);
	            */
	            
	            
	            //System.out.println(totalGain);
	            /*for(int l=0;l<transProfit.length;l++) {
            	System.out.println("product "+l+": "+transProfit[l]);
        		}*/        
	            /*for(int l=0;l<periodtotalgain.length;l++) {
	            	System.out.println("period "+l+": "+periodtotalgain[l]);
            	}*/
	            /*for(int l=0;l<=maxitem;l++) {
	            	System.out.println("itemset "+l+": "+lifespan[l][0]+", "+lifespan[l][1]+"");
	            }*/
  	          
	        }																
	        catch(Exception e)
	        {
	            System.out.println(" Error about loading the dataset into the memory : "+e.toString());
	        }
	    }
}