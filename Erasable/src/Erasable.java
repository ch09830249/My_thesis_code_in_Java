import java.lang.*;
import java.math.BigDecimal;
import java.util.*;
import java.io.*;

public class Erasable 
{
	//-------------------------------
    // 門檻值設定區
    static	double	minGain=0d;		//記錄「minimum erasable itemset mining threshold」
    //-------------------------------
    // 宣告 input data 相關參數	
    static	char[][] dbData;    					// 存放「原始交易資料」
    static	int[] transProfit;					  // 記錄「每筆交易的 profit 值」	
    static	double totalGain = 0;
    static String FilePath;
    // 記錄「全部產品的 profit 值總和」
                        
    //-------------------------------
    //記憶體所需參數區
    static	String usedStr;   			          //列印「使用記憶體量」字串
    static	float freeMemory, totalMemory; 		//宣告未使用前記憶體狀況與使用後記憶體狀況，兩者相減就是使用記憶體量！
    static	Runtime r = Runtime.getRuntime(); //這是宣告相關參數
	
	//-------------------------------
	// 取得「處理結果」		
	static int CK_Num=0;
	static int EI_Num=0;
	
	static int E1_Num=0;
	static int E2_Num=0;
	static int E_Num=0;
	
	static int NE1_Num=0;
	static int NE2_Num=0;
	
	static ElementaryTable E1 = new ElementaryTable();	//記錄「 High Maximum Upper-Bound Utility 1-itemsets 」
	static ElementaryTable E2 = new ElementaryTable();	//記錄「 High Maximum Upper-Bound Utility 2-itemsets 」
	static ElementaryTable E3 = new ElementaryTable();	//記錄「 High Maximum Upper-Bound Utility 3-itemsets 」
	
	static int E1_memory = 0;
	static int E2_memory = 0;	
	static int E3_memory = 0;	
	
	static String E1_items="";
	public static double getThreshold(double threshold)
	{            //設定minGain
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
        {	
			minGain=getThreshold(0.2);
			//minGain=getThreshold(0.6);
			System.out.println("Erasable Itemset Mining Algorithm ");
 			System.out.println("=====================================");
			System.out.println("minGain: "+ minGain);
			FilePath=getDatabase("TT");
			System.out.println("File: "+ FilePath + ".txt");
			//FilePath=getDatabase("non_erasable_T5I2N1KD10K");
			//FilePath=getDatabase("test");
            //System.out.println();
		    //System.out.println();
 			//System.out.println("-------------------------------------------------------------");
 			//System.out.println("		Original	Erasable Mining Algorithm ");     	
 			//System.out.println();
 			//System.out.println("");
            
            long t1 =System.currentTimeMillis(); 
	
 			//----------------------------------
 			// 01.
 			// 連結資料庫
 			ConnectionTextFile db = new ConnectionTextFile(FilePath);
				
			dbData = db.getdbData();
			transProfit = db.getTransProfit();
			totalGain = db.getTotalGain();				

			System.out.println("-----------------------------");
			System.out.println(" dbData.length(產品數量): "+dbData.length);
			System.out.println(" transProfit.length: "+transProfit.length);
			System.out.println(" totalGain: "+totalGain);
			System.out.println("-----------------------------");					
	
		    long t2 =System.currentTimeMillis(); 
 		
		    //System.out.println(" time of connecting database: "+ Double.toString((t2-t1)/1000.0)+"s");    				

			freeMemory = (float) r.freeMemory();//未使用前的記憶體！				
			//freeMemory = (float) r.totalMemory() - (float) r.freeMemory(); //未使用前的記憶體！ 				
			
 			//----------------------------------
 			// 02.
 			// 計算「minimum erasable itemset mining threshold」  
 			// 直接使用「百分比」   		
			  //System.out.println(" minGain: " + minGain);		
			
			//----------------------------------
 			// 03.
 			// get the set of E1 
 			// get Erasable 1-itemset
 			System.out.println("=======================================");
 			System.out.println("1-itemset");
 			long t3 =System.currentTimeMillis(); 
			Find_E1  fe1 = new Find_E1(dbData, transProfit, minGain, totalGain);
			E1 = fe1.getE1();                                      //取得 Erasable 1-itemsets
			E1_memory = fe1.getE1_memory();						   //取得使用的記憶體使用量		
			CK_Num = fe1.getCK_Num();							   //取得Candidate 1-itemsets的數量
			EI_Num = fe1.getE1().size();						   //取得Erasable 1-itemsets的數量
			NE1_Num=CK_Num-EI_Num; // NE1=CE1-E1
			E1_Num=EI_Num;
			System.out.println("CK_Num ="+CK_Num);
			System.out.println("E1_Num ="+E1_Num);
			System.out.println("NE1_Num ="+NE1_Num);
			System.out.println("=======================================");
			//fe1.Print(E1);
			//System.out.println(EI_Num);
			long t4 =System.currentTimeMillis(); 
			//System.out.println(" Total time of getting Erasable 1-itemset: "+ Double.toString((t4-t3)/1000.0)+"s"); 
			//System.out.println(" Consumming total memory of getting E1 itemset (Create C1 => get E1=> delete C1)，共使用: "+ E1_memory + "K used");           
			//System.out.println("=======================================");
			if(E1.size()!=0)
			{
				//----------------------------------
				// 03.
				// get the set of E2
				// get Erasable 2-itemset
				//System.out.println("=======================================");
				System.out.println("2-itemset");
				long t5 =System.currentTimeMillis(); 
				Find_E2  fe2 = new Find_E2(dbData, transProfit, minGain, totalGain, E1);
				E2 = fe2.getE2();
				E2_memory = fe2.getE2_memory();
				E2_Num=fe2.getE2().size();
				NE2_Num=((CK_Num*(CK_Num-1))/2)-E2_Num; //NE2 = CE2 - E2
				System.out.println("E2_Num ="+E2_Num);
				System.out.println("NE2_Num ="+NE2_Num);
				
				CK_Num = CK_Num + fe2.getCK_Num();
				EI_Num = EI_Num + fe2.getE2().size();

				
				//System.out.println(EI_Num);
				long t6 =System.currentTimeMillis(); 
				//System.out.println(" Total time of getting Erasable 2-itemset: "+ Double.toString((t6-t5)/1000.0)+"s");    
				//System.out.println(" Consumming total memory of getting E2 itemset (Create C2 => get E2=> delete C2)，共使用: "+ E2_memory + "K used");     
				System.out.println("=======================================");
				
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
				System.out.println("k-itemset (k>=3)");
				Find_ErasableItemsets efi = new Find_ErasableItemsets(dbData, transProfit, minGain, totalGain, E1, E2, fe1.getCK_Num());
				EI_Num = EI_Num + efi.getEINum();
				CK_Num = CK_Num + efi.getCKNum();
				E_Num=efi.getEINum();
				System.out.println("3以上的erasable itemset ="+E_Num);
				//CK_Num = efi.getCKNum();
				//EI_Num = efi.getEINum();
				//efi.getEI();
				
				//----------------------------------
				// 07.     			
				// 輸出「所有的 HAUI Itemsets 資訊」於文字檔裡！     			
				Output_Info oi = new Output_Info(FilePath , minGain, efi.getEI(), totalGain);
				E1_items=oi.getE1(efi.getEI());
			}
			else
			{										
				//----------------------------------
				// 07.     		
				// 輸出「所有的 HAUI Itemsets 資訊」於文字檔裡！     			
				//Output_Info oi = new Output_Info(FilePath , minGain, E1, totalGain);															
			}							
			
			totalMemory = (float) r.totalMemory(); //使用了多少記憶體！ 
			//totalMemory = (float) r.totalMemory() - (float) r.freeMemory(); //使用了多少記憶體！ 
			usedStr = String.valueOf(((int) (totalMemory - freeMemory))/1024)+ "K used";   
	
			long t7 =System.currentTimeMillis(); 							

			//列印「探勘結果」
			System.out.println("-------------------------------------------------------------");
			System.out.println("-------------------------------------------------------------");
			System.out.println("-------------------------------------------------------------");
			System.out.println();
			System.out.println(" Mining Erasable Itemsets on original method  在記憶體te方面，共使用: "+usedStr); 
			System.out.println(" minGain: "+minGain);   
			System.out.println(" minGain: "+(int)(minGain*totalGain));     	
			System.out.println(" 資料庫:"+ FilePath);			
			System.out.println(" Number of CK : "+CK_Num);
			System.out.println(" Number of EI : "+EI_Num);
			System.out.println(" Execution time for EI : "+ Double.toString((t7-t1)/1000.0)+"s");  
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

