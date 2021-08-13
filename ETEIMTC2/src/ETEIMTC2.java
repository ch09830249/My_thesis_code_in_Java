import java.lang.*;
import java.math.BigDecimal;
import java.util.*;
import java.io.*;

public class ETEIMTC2 
{
	//-------------------------------
    // 門檻值設定區
    static	double	minGain=0d;		//記錄「minimum temporal erasable itemset mining threshold」
    //-------------------------------
    // 宣告 input data 相關參數	
    static	char[][] dbData;    					// 存放「原始交易資料」
    static	int[] transProfit;					 	// 記錄「每筆交易的 profit 值」	
    static	int[] period;							// 記錄「每筆交易發生的period」
    static  int[] itemStartPeriod;					//紀錄每個item開始出現的period
    static  int[] periodtotalgain;
    static  int lastperiod;							//紀錄最後一筆交易的period
    static 	int maxitem;
    static	double totalGain = 0;
    static String FilePath;
    // 記錄「全部產品的 profit 值總和」
                        
    //-------------------------------
    //記憶體所需參數區
    static	String usedStr;   			        //列印「使用記憶體量」字串
    static	float freeMemory, totalMemory; 		
    static	Runtime r = Runtime.getRuntime();   //這是宣告相關參數
    
	
	//-------------------------------
	// 取得「處理結果」		
	static int CK_Num=0;
	static int EI_Num=0;
	
	static int E1_Num=0;
	static int E2_Num=0;
	static int E_Num=0;
	
	static int NE1_Num=0;
	static int NE2_Num=0;
	
	static ElementaryTable E1 = new ElementaryTable();	//記錄「 Temporal erasable 1-itemsets 」
	static ElementaryTable E2 = new ElementaryTable();	//記錄「 Temporal erasable 2-itemsets 」
	static ElementaryTable E3 = new ElementaryTable();	//記錄「 Temporal erasable 3-itemsets 」
	
	static int E1_memory = 0;
	static int E2_memory = 0;	
	static int E3_memory = 0;	
	
	static String E1_items="";
	public static double getThreshold(double threshold)
	{   //設定minGain
		minGain=threshold;
		return minGain;
	}
	public static String getDatabase(String filepath)
	{
		FilePath=filepath;
		return FilePath;
	}
	
	public static void main(String[] args)
    {   
		try
        {   //紀錄記憶體
			r.gc();	//先gc
			freeMemory = (float) r.freeMemory();
			totalMemory = (float) r.totalMemory();
			int diffmemory_int1 = ((int) (totalMemory - freeMemory)/1024);
			
    		//紀錄時間
			long t1 =System.currentTimeMillis();//開始算時間
			
    		minGain=getThreshold(0.6);      	//設定門檻值
			FilePath=getDatabase("TT(NOTC)");		//讀取檔案檔名
			
 			System.out.println("-------------------------------------------------------------");
 			System.out.println("Temporal Erasable Itemset Mining Algorithm");     	
            
 			//----------------------------------
 			// 01.
 			// 連結資料庫 			
 			ConnectionTextFile db = new ConnectionTextFile(FilePath);				
			dbData = db.getdbData();
			transProfit = db.getTransProfit();
			period = db.getPeriod();
			periodtotalgain = db.getPeriodTotalGain();
			itemStartPeriod = db.getItemStartPeriod();
			lastperiod = db.getLastPeriod();
			totalGain = db.getTotalGain();
			maxitem = db.getmaxitem();
			
			long t2 =System.currentTimeMillis(); 
			
			System.out.println("-------------------------------------------------------------");
			System.out.println(" minGain: "+minGain);
			System.out.println(" The number of products: "+dbData.length);
			System.out.println(" Used Memory (Before executing program): "+diffmemory_int1);
			System.out.println(" Time of connecting database: "+ Double.toString((t2-t1)/1000.0)+"s");//紀錄讀取資料庫擷取資訊所需時間
			System.out.println("-------------------------------------------------------------");					        				
			
			
 			//----------------------------------
 			// 02.
 			// 計算「minimum erasable itemset mining threshold」  
 			// 直接使用「百分比」   		
			//System.out.println(" minGain: " + minGain);		
			
			//----------------------------------
 			// 03.
 			// get the set of E1 
 			// get Temporal Erasable 1-itemset
 			System.out.println("=======================================");
 			long t3 =System.currentTimeMillis(); 
			Find_E1  fe1 = new Find_E1(dbData, transProfit, minGain, period, itemStartPeriod, periodtotalgain);
			E1 = fe1.getE1();                                      //取得 Temporal Erasable 1-itemsets
			E1_memory = fe1.getE1_memory();						   //取得使用的記憶體使用量	
			CK_Num = fe1.getCK_Num();							   //取得Candidate 1-itemsets的數量
			EI_Num = fe1.getE1().size();						   //取得Erasable 1-itemsets的數量
			NE1_Num=CK_Num-EI_Num;								   // NE1=CE1-E1
			E1_Num=EI_Num;
			System.out.println("*** Temporal Erasable 1-itemset ***"); 
			System.out.println("CK_Num ="+CK_Num);
			System.out.println("C1_Num ="+CK_Num);
			System.out.println("E1_Num ="+E1_Num);
			System.out.println("NE1_Num ="+NE1_Num);
			System.out.println("E1 used memory: "+ E1_memory);
			//fe1.Print(E1);
			//System.out.println(EI_Num);
			long t4 =System.currentTimeMillis(); 
			System.out.println("Total time of getting Temporal Erasable 1-itemset: "+ Double.toString((t4-t3)/1000.0)+"s"); 
			//System.out.println(" Consumming total memory of getting E1 itemset (Create C1 => get E1=> delete C1)，共使用: "+ E1_memory + "K used");           
			System.out.println("=======================================");
			if(E1.size()!=0)    //若E1非空
			{
				//----------------------------------
				// 03.
				// get the set of E2
				// get Erasable 2-itemset
				//System.out.println("=======================================");
				long t5 =System.currentTimeMillis(); 
				System.out.println("*** Temporal Erasable 2-itemset ***");
				Find_E2  fe2 = new Find_E2(dbData, transProfit, minGain, E1, period, itemStartPeriod, periodtotalgain);
				E2 = fe2.getE2();
				E2_memory = fe2.getE2_memory();
				E2_Num = fe2.getE2().size();
				NE2_Num=((CK_Num*(CK_Num-1))/2)-E2_Num; //NE2 = CE2 - E2
				CK_Num = CK_Num + fe2.getCK_Num();
				EI_Num = EI_Num + fe2.getE2().size();
				long t6 =System.currentTimeMillis();				 
				System.out.println("CK_Num ="+CK_Num);
				System.out.println("C2_Num ="+fe2.getCK_Num());
				System.out.println("E2_Num ="+E2_Num);
				System.out.println("NE2_Num ="+NE2_Num);
				System.out.println("E2 used memory: "+ E2_memory);
				//System.out.println(EI_Num);
				System.out.println("Total time of getting Temporal Erasable 2-itemset: "+ Double.toString((t6-t5)/1000.0)+"s");    
				//System.out.println(" Consumming total memory of getting E2 itemset (Create C2 => get E2=> delete C2)，共使用: "+ E2_memory + "K used");     
				System.out.println("=======================================");
				System.out.println("*** Temporal Erasable k-itemset (k>=3) ***");
				
				//----------------------------------
				// 04.
				// get the set of E3
				// get Erasable 3-itemset
				/*
				System.out.println("=======================================");
				long t8 =System.currentTimeMillis(); 
				Find_E3  fe3 = new Find_E3(dbData, transProfit, minGain, totalGain, E1);
				E3 = fe3.getE3();
				E3_memory = fe3.getE3_memory();
				CK_Num = CK_Num + fe3.getCK_Num();
				EI_Num = EI_Num + fe3.getE3().size();
				System.out.println(EI_Num);
				long t9 =System.currentTimeMillis(); 
				//System.out.println(" Total time of getting Erasable 3-itemset: "+ Double.toString((t9-t8)/1000.0)+"s");    
				//System.out.println(" Consumming total memory of getting E3 itemset (Create C3 => get E3=> delete C3)，共使用: "+ E3_memory + "K used");      
				//System.out.println("=======================================");
				*/
				
				// 04.
				// get the set of Ei (i>=2)
				// get Erasable i-itemset
				//System.out.println("1-item數量: " + fe1.getCK_Num());
				Find_ErasableItemsets efi = new Find_ErasableItemsets(dbData, transProfit, minGain, totalGain, E1, E2, fe1.getCK_Num(), period, itemStartPeriod, periodtotalgain);
				EI_Num = EI_Num + efi.getEINum();
				CK_Num = CK_Num + efi.getCKNum();
				E_Num=efi.getEINum();
				E3_memory = efi.getEK_memory();
				System.out.println("Ek_Num (k>=3) ="+E_Num);
				System.out.println("Ck_Num (k>=3) ="+efi.getCKNum());
				System.out.println("EK used memory: "+ E3_memory);
				System.out.println("=======================================");
				//CK_Num = efi.getCKNum();
				//EI_Num = efi.getEINum();
				//efi.getEI();
				
				//----------------------------------
				// 07.     			
				// 輸出「所有的 HAUI Itemsets 資訊」於文字檔裡！     			
				Output_Info oi = new Output_Info(FilePath , minGain, efi.getEI(), totalGain, itemStartPeriod, periodtotalgain);
				E1_items=oi.getE1(efi.getEI());
			}
			else
			{										
				//----------------------------------
				// 07.     		
				// 輸出「所有的 HAUI Itemsets 資訊」於文字檔裡！     			
				//Output_Info oi = new Output_Info(FilePath , minGain, E1, totalGain);															
			}							
			
			//紀錄記憶體
			r.gc();	//先gc
			freeMemory = (float) r.freeMemory();
			totalMemory = (float) r.totalMemory();
			int diffmemory_int2 = ((int) (totalMemory - freeMemory)/1024); 
			usedStr = String.valueOf(Math.max(Math.max(diffmemory_int2, E3_memory),Math.max(E1_memory, E2_memory))+ "K used");   
			System.out.println(" Used Memory (After executing program): "+diffmemory_int2);
			
			long t7 =System.currentTimeMillis();//紀錄結束時間 							
			
			//列印「探勘結果」
			System.out.println("-------------------------------------------------------------");
			System.out.println("-------------------------------------------------------------");
			System.out.println("-------------------------------------------------------------");
			System.out.println();
			System.out.println("Temporal Erasable Itemsets Mining  在記憶體te方面，共使用: "+usedStr); 
			System.out.println(" minGain: "+minGain);   
			//System.out.println(" minGain: "+(int)(minGain*totalGain));     	
			System.out.println(" Filename of database:"+ FilePath);			
			System.out.println(" Number of CK : "+CK_Num);
			System.out.println(" Number of EI : "+EI_Num);
			//System.out.println(" Execution time for TEI (No DB loading) : "+ Double.toString((t7-t2)/1000.0)+"s");  
			System.out.println(" Execution time for all program : "+ Double.toString((t7-t1)/1000.0)+"s");  
			System.out.println();
			System.out.println("-------------------------------------------------------------");
			System.out.println("-------------------------------------------------------------");
			System.out.println("-------------------------------------------------------------");
		
		}
		catch(Exception ex)
        {
			System.out.println(" Error about main function (in OHUI.java):"+ex.getMessage());
		}
	}
}

