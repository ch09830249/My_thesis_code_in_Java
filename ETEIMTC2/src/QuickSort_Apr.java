class QuickSort_Apr {	
	
	String[] trans;   //指到要排序陣列的參考
	
	private void swap(int i, int j){
		String temp = trans[i];
		trans[i]=trans[j];
		trans[j]=temp;
	}
	
	public char[][] put(char[][] arr){
		String[] temp = new String[arr.length];
		for (int i=0; i<temp.length; i++)
			temp[i] = new String(arr[i]);
		
		put(temp);			//用下面的put函數排序
		
		char[][] ans = new char[temp.length][];
		for (int i=0; i<ans.length; i++)
			ans[i] = temp[i].toCharArray();
		return ans;
	}
	
	public void put(String[] arr){
		trans = arr;  		  //將trans指向要排序的字串陣列
		Sort(0,arr.length-1); //抓進來後進行排序
		//return trans;
	}
	
	public void Sort(int L_bound, int R_bound){
		if (R_bound-L_bound <= 0 ) 
			return;
    		String p = trans[R_bound];  //p代表pivot(標準)
    		int l=L_bound;         	 	//l代表左指標
    		int r=R_bound-1;        	//r代表右指標
    		while (true) {       		//此while迴圈進行切割
      			while (trans[l].compareTo(p)<0)  l++;  		  //l往右移直到碰到比p小或等於p的元素
      			while (r>0 && (trans[r].compareTo(p)>0)) r--; //r往左移直到碰到比p大或等於p的元素
      			if (l>=r) 
      				break;   	   //若左右指標交叉表示切割完成                      
      			swap(l,r);         //將左右指標l、r所指之元素對調
    		}
    		swap(l,R_bound);    //將p調整至左指標l之位置
    		Sort(L_bound,l-1);  //繼續以遞迴方式處理小於p的部份
    		Sort(l+1,R_bound);  //繼續以遞迴方式處理大於p的部份
    		return;
	}
}