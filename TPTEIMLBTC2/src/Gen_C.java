import java.io.*;
import java.util.*; //為了套用StringTokenizer和TreeMap的元件

class Gen_C{

	char[][] C;     
    int length=0;
       
    //check_c的功用為「確認此候選集的子集在前一階段是否為頻繁項目集」	        
    //ElementaryTable Preceding_LItemsets = new ElementaryTable();	
	

    //產生「經過判斷後的新候選集」
    ElementaryTable New_CItemsets = new ElementaryTable();
    
	public Gen_C(char[][] C, ElementaryTable Preceding_LItemsets, int length) {  //建立的同時設定大小基準

             for(int i=0;i<C.length-1;i++)
             {
             		if(C[i]==null)break;
                  		
      				  for(int j=i+1;j<C.length;j++)	
      				  {
       					  char[] str1 = new char[C[i].length];
       					  char[] str2 = new char[C[j].length];
       						
       					  char[] temp;
       					  System.arraycopy(C[i],0,str1,0,str1.length);
       					  System.arraycopy(C[j],0,str2,0,str2.length);

       							//檢查與合併
       						if (CBC(str1,str2)){ //檢查除了最後一個item以外, 其餘items都要相同
       								temp=Combine(str1,str2);  //合併
		       									
	       						if(temp==null) 
	       								break;
	       								
	       						if (Prune(temp,length, Preceding_LItemsets)) {  ///修剪階段    							       							      							     						       									      										
	       							//對候選集做排序(順序統一由大到小) 
	       							int [] Sortedtemp = new int[temp.length];
	       			    			for(int k = 0;k<temp.length;k++) {
	       			    				Sortedtemp[k] = (int)temp[k];
	       			    			}
	       			    			Arrays.sort(Sortedtemp);
	       			    			for(int a = 0;a<temp.length;a++) {
	       			    				temp[a] = (char) Sortedtemp[temp.length-1-a];
	       			    			}
	       							New_CItemsets.add(temp, 0);
	       						} ///若該項目集通過以上兩階段，則存入C-length中
  							}
                       }
              }        
   }


//==================== 結合的方法 ====================
	public static boolean CBC(char[] a, char[] b) {// ComBine Check
		if (a.length!=b.length) return false;
		for (int i=0; i<a.length-1; i++)  //最後一個字元不檢查, 其他都要相同
			if (a[i]!=b[i])
				return false;
		return true;
	}
	
	public static char[] Combine(char[] a, char[] b) {  //合併
		char[] c = new char[a.length+1];
		System.arraycopy(a,0,c,0,a.length);
		System.arraycopy(b,b.length-1,c,c.length-1,1);
		return c;
	}

//==================== 修剪的方法 ====================	
	public static boolean Prune(char[] a,int length, ElementaryTable Preceding_LItemsets) //a欲修剪的itemset
	{		
	
			for (int k=0; k<a.length; k++) {
       			//產生子項目集
       			char[] tmp = new char[a.length-1];
       			if (k!=0)   		   //前半
       				System.arraycopy(a,0,tmp,0,k);
       			if (k!=(a.length-1))   //後半
       				System.arraycopy(a,k+1,tmp,k,(length-k-1));
       			
       			//對子項目集tmp做排序 (因為{0, 1}和{1, 0}他認為是不同itemset)
    			int [] Sortedtmp = new int[tmp.length];
    			for(int i = 0;i<tmp.length;i++) {
    				Sortedtmp[i] = (int)tmp[i];
    			}
    			Arrays.sort(Sortedtmp);
    			for(int i = 0;i<tmp.length;i++) {
    				tmp[i] = (char) Sortedtmp[tmp.length-1-i];
    			}
       			/*for(int i = 0;i<tmp.length;i++)
       				System.out.print(""+(int)tmp[i]+", "); 
       				System.out.println();*/
    				
       			//檢查
	       		if(Preceding_LItemsets.NoContainsKey(tmp)) {    //看此sub-itemset是否為temporal erasable
	       			/*for(int j=0; j<tmp.length; j++){    		//印出所有非temporal erasable的sub-itemset
	       				System.out.print(""+(int)tmp[j]+", ");
	       			}System.out.println();*/
	       			return false;
	       		}
	       		
       			
       			
       			/*for(int j=0; j<tmp.length; j++){    印出所有sub-itemset
       				System.out.print((int)tmp[j]);
       			}System.out.println();*/
       				
       			
       		}
       		return true;
	}
 


	public ElementaryTable get_CItemsets() {     //取得New_CItemsets
		return New_CItemsets;
	}
  
}