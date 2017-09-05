package collaborationFiltering;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;
import java.util.ArrayList;
import java.util.Arrays;

import weka.gui.beans.Startable;


public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	  ArrayList<Object> aList = new ArrayList<Object>();
	  ArrayList<Integer> everyArrayList = new ArrayList<Integer>();
	  int Startable =0;
	  
      int[][] temp ={{1,2},
    		         {1,3},
    		         {1,4},
    		         {2,2},
    		         {2,3},
    		         {2,6},
    		         {3,1},
    		         {3,3},
    		         };
      int compare = temp[0][0];
      for (int i = 0; i < temp.length; i++) {
		if (temp[i][0] == compare) {
			System.out.println(temp[i][1]);
			everyArrayList.add(temp[i][1]);
//			System.out.println("top");
			if (i==(temp.length-1)) {
				int[] a = new int[everyArrayList.size()];
				for (int j = 0; j < everyArrayList.size(); j++) {
					a[j] = everyArrayList.get(j);
				}
				aList.add(a);
			}
		}else {			
			int[] a = new int[everyArrayList.size()];
			for (int j = 0; j < everyArrayList.size(); j++) {
				a[j] = everyArrayList.get(j);
//				System.err.print(everyArrayList.get(j)+"¡¢");
			}
			aList.add(a);
			everyArrayList.removeAll(everyArrayList);
			everyArrayList.add(temp[i][1]);
			compare= temp[i][0];

			
		}
	}
      System.out.println("length:"+aList.size());
      
      for (int i = 0; i < aList.size(); i++) {
		int[] tempArrayList = (int[]) aList.get(i);
		for (int j = 0; j < tempArrayList.length; j++) {
			System.out.print(tempArrayList[j]+" ");
		}
		System.out.println("*********");
	}
      
      int count = 0;
      
      for (int i = 0; i < aList.size(); i++) {
    	  int isHasI = 0; 
    	  int[] tempArrayList = (int[]) aList.get(i);
  		for (int j = 0; j < tempArrayList.length; j++) {
  			if (tempArrayList[j]== 2) {
				isHasI += 1;
			}else if (tempArrayList[j]== 3) {
				isHasI = isHasI+1;
			}
  		}
  		if (isHasI ==2&&isHasI!=1) {
			count++;
		}
	}
      
      System.out.println("1&&4:"+count);
      
	}

}
