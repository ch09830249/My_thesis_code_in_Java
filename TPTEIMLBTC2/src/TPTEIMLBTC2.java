import java.lang.*;
import java.math.BigDecimal;
import java.util.*;
import java.io.*;

public class TPTEIMLBTC2
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
    static  int LSPall;								//紀錄Lower bound要用到的LSPall
    // 記錄「全部產品的 profit 值總和」
                        
    //-------------------------------
    //記憶體所需參數區
    static	String usedStr;   			        //列印「使用記憶體量」字串
    static	float freeMemory, totalMemory; 		
    static	Runtime r = Runtime.getRuntime();   //這是宣告相關參數
    
	
	//-------------------------------
	// 取得「處理結果」		
	static int CK_Num=0;	//紀錄第一階段的每個level候選集總數量
	static int CK2 = 0;		//紀錄第一階段結束, 所有TELBI當作第二階段候選集的總數量
	static int EI_Num=0;	
	static int EI2 = 0;		
	
	static int E1_Num=0;
	static int E2_Num=0;
	static int E_Num=0;
	
	static int NE1_Num=0;
	static int NE2_Num=0;
	
	static ElementaryTable E1 = new ElementaryTable();	//記錄「 Temporal erasable lower bound 1-itemsets 」
	static ElementaryTable E2 = new ElementaryTable();	//記錄「 Temporal erasable lower bound 2-itemsets 」
	static ElementaryTable E3 = new ElementaryTable();	//記錄「 Temporal erasable lower bound 3-itemsets 」
	
	static int E1_memory = 0;
	static int E2_memory = 0;	
	static int E3_memory = 0;
	static int total_memory = 0;
	
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
        {   //紀錄記憶體(程式執行前)
			r.gc();	//先gc
			freeMemory = (float) r.freeMemory();
			totalMemory = (float) r.totalMemory();
			int diffmemory_int1 = ((int) (totalMemory - freeMemory)/1024);
			
    		//紀錄時間
			long t1 =System.currentTimeMillis();//開始算時間
			
    		minGain=getThreshold(0.28);      	//設定門檻值
			FilePath=getDatabase("TT_new");		//讀取檔案檔名
			
 			System.out.println("-------------------------------------------------------------");
 			System.out.println("Temporal Erasable Itemset Mining with Lower Bound Model Algorithm");     	
            
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
			LSPall = db.getLSPall();				//抓出LSPall
			
			long t2 =System.currentTimeMillis(); 
			
			System.out.println("-------------------------------------------------------------");
			System.out.println(" ISPall: "+LSPall);
			System.out.println(" minGain: "+minGain);
			System.out.println(" The number of products: "+dbData.length);
			System.out.println(" Used Memory (Before executing program): "+diffmemory_int1);
			System.out.println(" Time of connecting database: "+ Double.toString((t2-t1)/1000.0)+"s");//紀錄讀取資料庫擷取資訊所需時間
			System.out.println("-------------------------------------------------------------");					        				
			
			
			//----------------------------------
			//----------------------------------
			//----------------------------------
			//Phase 1: 找出所有temporal erasable lower bound itemset
 			//----------------------------------
			//----------------------------------
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
			Find_E1  fe1 = new Find_E1(dbData, transProfit, minGain, period, itemStartPeriod, periodtotalgain, totalGain, LSPall);
			E1 = fe1.getE1();                                      //取得 Temporal Erasable 1-itemsets
			E1_memory = fe1.getE1_memory();						   //取得使用的記憶體使用量	
			CK_Num = fe1.getCK_Num();							   //取得Candidate 1-itemsets的數量
			EI_Num = fe1.getE1().size();						   //取得Erasable 1-itemsets的數量
			NE1_Num=CK_Num-EI_Num;								   // NE1=CE1-E1
			E1_Num=EI_Num;
			System.out.println("*** Temporal Erasable Lower Bound 1-itemset ***"); 
			System.out.println("CK_Num ="+CK_Num);
			System.out.println("C1_Num ="+CK_Num);
			System.out.println("E1_Num ="+E1_Num);
			System.out.println("NE1_Num ="+NE1_Num);
			System.out.println("E1 used memory: "+ E1_memory);
			//fe1.Print(E1);
			//System.out.println(EI_Num);
			long t4 =System.currentTimeMillis(); 
			System.out.println("Total time of getting Temporal Erasable lower bound 1-itemset: "+ Double.toString((t4-t3)/1000.0)+"s"); 
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
				System.out.println("*** Temporal Erasable Lower Bound 2-itemset ***");
				Find_E2  fe2 = new Find_E2(dbData, transProfit, minGain, E1, period, itemStartPeriod, periodtotalgain, totalGain, LSPall);
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
				System.out.println("Total time of getting Temporal Erasable lower bound 2-itemset: "+ Double.toString((t6-t5)/1000.0)+"s");    
				//System.out.println(" Consumming total memory of getting E2 itemset (Create C2 => get E2=> delete C2)，共使用: "+ E2_memory + "K used");     
				System.out.println("=======================================");
				System.out.println("*** Temporal Erasable Lower Bound k-itemset (k>=3) ***");
				
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
				Find_ErasableItemsets efi = new Find_ErasableItemsets(dbData, transProfit, minGain, totalGain, E1, E2, fe1.getCK_Num(), period, itemStartPeriod, periodtotalgain, LSPall);
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
				//----------------------------------
				//----------------------------------
				//Phase 2: 找出所有temporal erasable itemset
	 			//----------------------------------
				//----------------------------------
				//----------------------------------
				
				r.gc();	//先gc
				freeMemory = (float) r.freeMemory();
				totalMemory = (float) r.totalMemory();
				total_memory = ((int) (totalMemory - freeMemory)/1024); 
				usedStr = ""+ total_memory + "K used";  
				
				ElementaryTable[] CI = efi.getEI();
				CK2 = 0;
				for(int i=0;i<CI.length; i++) {
					if(CI[i]==null) break;
					CK2 = CK2 + CI[i].size;
					ElementaryTable Kept_Items = new ElementaryTable();
					Set set3 = CI[i].ElementSet();
		 			Iterator iter3 = set3.iterator();
		 			Element nx3;
					int itemsetStartPeriod;							//記錄此k-itemset的起始時間
					int itemsetLastPeriod;							//記錄此k-itemset的結束時間
		     		while (iter3.hasNext())    
		 			{	
		     			itemsetStartPeriod=0;									//初始起始時間
		   			    itemsetLastPeriod= period[period.length-1];				//初始結束時間(此資料庫最後一個period)
		   			    Kept_Items.clear();
						nx3 = (Element)iter3.next();						    
							    
						//取出itemset含有哪些1-item，並放置Kept_Items中並抓出此itemset的生命週期
						for (int k=0; k<nx3.getKey().length; k++)
		   			    {	
		   			    	char[] checked_itemset = new char[1];
		   			        checked_itemset[0] = nx3.getKey()[k];
		   			        //找出itemset's lifespan
		   			        int item = (int)checked_itemset[0];
		   			        if(itemStartPeriod[item]>itemsetStartPeriod)    	//記錄此k-itemset的起始時間
					        	itemsetStartPeriod = itemStartPeriod[item];
		   			        Kept_Items.add(checked_itemset, 0);    		 //引數為item字元和gain等於0
		   			        //System.out.print(""+item+", ");
		   			    }
						/*System.out.println();
						System.out.println("起始時間"+itemsetStartPeriod);
						System.out.println("結束時間"+itemsetLastPeriod);
						System.out.println();*/
									
						
							    
						//the temporal gain ratio of each candidate k-itemset					          					        
						//找產品中含有itemset中的items並且此交易要在生命週期內
						int gain = 0;
						for(int a=0;a<dbData.length;a++)		//抓出一筆交易
						{	
							if(period[a]<itemsetStartPeriod)// || period[a]>itemsetLastPeriod)	//只抓出此itemset的生命週期中的交易 更改
								continue;//下一筆交易
							for(int b=0;b<dbData[a].length;b++)	//交易的item逐一去看
							{	
								// check the item whether the item is a valid erasable item
								//只要檢查產品中有含A或B原料，則該產品利潤值則是itemset的gain值
								if(Kept_Items.ContainsKey(dbData[a][b]))
								{							  							  
									gain += transProfit[a]; //此交易加到此itemset的gain                           
									b = dbData[a].length;   //主要使用精神是為了跳出for迴圈, 找到item就跳出
								}							              
							}
						}
						
						
						//算其生命週期內的分母值
						int totalGain=0;
						for(int l=itemsetStartPeriod;l<=lastperiod;l++){//i<=itemsetLastPeriod;i++) {	//加總此itemset出現期間其totalgain //更改
							totalGain += periodtotalgain[l];
						}
						//System.out.println("total: "+totalGain);
						
						double eGain = ((double)gain/totalGain);
						/*System.out.print(" itemset: ");
					    for(int l=0;l<nx3.getKey().length-1;l++)
							System.out.print((int)nx3.getKey()[l]+", ");
						System.out.print((int)nx3.getKey()[nx3.getKey().length-1]);
			            System.out.print(" gain: "+ gain);
			            System.out.print(" totalgain: "+ totalGain);
			            System.out.println("	gain ratio: "+ eGain);*/
			             
						if(eGain>minGain)
						{
							CI[i].remove(nx3.getKey());
						}
						else
						{	
							CI[i].remove(nx3.getKey());
							CI[i].add(nx3.getKey(), gain);
						}						
					}
		     		EI2 = EI2 + CI[i].size;					
				}
				
				//----------------------------------
				// 07.     			
				// 輸出「所有的 Temporal erasable Itemsets 資訊」於文字檔裡！     			
				Output_Info oi = new Output_Info(FilePath , minGain, CI, totalGain, itemStartPeriod, periodtotalgain);
				E1_items=oi.getE1(CI);
			}
			else
			{										
				//----------------------------------
				// 07.     		
				// 輸出「所有的 HAUI Itemsets 資訊」於文字檔裡！     			
				//Output_Info oi = new Output_Info(FilePath , minGain, E1, totalGain);															
			}							
			
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
			System.out.println(" Number of CK (In the first phase) : "+CK_Num);
			System.out.println(" Number of TELBI (In the first phase) : "+EI_Num);
			System.out.println(" Number of CK (In the second phase) : "+CK2);
			System.out.println(" Number of TEI (In the second phase) : "+EI2);
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

