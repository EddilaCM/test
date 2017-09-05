package collaborationFiltering;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Principal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.activation.FileTypeMap;
import javax.xml.soap.Text;
import java.text.DecimalFormat;

import com.sun.org.apache.regexp.internal.recompile;
import com.sun.org.apache.xml.internal.resolver.helpers.FileURL;
import com.sun.rowset.internal.Row;

//import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.neighboursearch.balltrees.TopDownConstructor;
import weka.core.stemmers.Stemmer;

//import 

public class basedItermCFS {
	public static String fileUrl= "D:/Workspaces/MyEclipse 10/recommendSystem/src/collaborationFiltering/data/";
	public static ArrayList<Object> user_list = new ArrayList<Object>();
	public static double[] itemsNameArr= null;
	
	public static String[] nameArr = null;
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
//		String dataFile = "D:/Workspaces/MyEclipse 10/recommendSystem/src/collaborationFiltering/data/testCF.csv";
		String dataFile = "D:/Workspaces/MyEclipse 10/recommendSystem/src/collaborationFiltering/data/ratings_test.arff";
		basedItermCFS test = new basedItermCFS();
		double[][] tempData = test.getDataFile(dataFile);
		
		System.out.println("end");		
		int[][] aa = test.itemsUserNum(tempData);
		String[][] bb = test.itemsSemblance(aa);
		System.out.println("nameArr is:");
		for (int i = 0; i < nameArr.length; i++) {
			System.out.println(nameArr[i]);
		}
		ArrayList<Object> ccObjList = test.topNNeighbor(bb, itemsNameArr); 
		System.out.println("ccObjList len:"+ccObjList.size());
		for (int i = 0; i < ccObjList.size(); i++) {
			System.out.println(i+"st product");
			double[] tempArr = (double[]) ccObjList.get(i);
			for (int j = 0; j < tempArr.length; j++) {
				System.out.print(tempArr[j]+" ");
			}
			System.out.println("\n");
		}
		
		int userId = 6;
		ArrayList<Double> recommendList = test.recommendSomeone(userId, ccObjList, 20, user_list, itemsNameArr);
		System.out.println("user "+userId+"recommend list are");
		for (int i = 0; i < recommendList.size(); i++) {
			System.out.println(recommendList.get(i));
		}
	
				

	}	
	/**
	 * function read data file
	 * @throws Exception 
	 */
	public double[][] getDataFile(String fileName) throws Exception {
		String[] fileString = fileName.split("\\.");//注意转义
		Instances datas = null;
		int row = 0;
		int col = 0;
		double[][] data = null;
		String FileType = fileString[fileString.length-1]; 
		if (FileType.equals("arff")) {
			try {
				FileReader fileReader = new FileReader(fileName);
				datas = new Instances(fileReader);
				row = datas.numInstances();
				col = datas.numAttributes();
				System.out.println(row + ":" + col);
				data = new double[row][col];
				nameArr = new String[col];
				for (int i = 0; i < row; i++) {
					for (int j = 0; j < col; j++) {
//						data[0][j] = datas.attribute(j).name();
						data[i][j] = datas.instance(i).value(j);
						nameArr[j] = datas.attribute(j).name();
					}// of for j  
				}// of for i
				fileReader.close();
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error occurred while trying trying to read"+ fileName+"in readArffFile().\r\n"+e);
				e.printStackTrace();
			}// of try....catch.....
		}else if (FileType == "csv") {
			try {
//				DataSource source = new DataSource(fileName);
//				datas = source.getDataSet();
//				row = datas.numInstances();
//				col = datas.numAttributes();
//				data = new double[row][col];
//				for (int i = 0; i < row; i++) {
//					for (int j = 0; j < col; j++) {
//						data[i][j] = datas.instance(i).value(j);
//					}// of for j
//				}// of for i
				
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error is " + e);
				e.printStackTrace();
			}
			
		}
		
		return data;
	}// of function getDataFile()
	/**
	 * function compute the semblance of between items
	 * @throws Exception 
	 */
	public int[][] itemsUserNum(double[][] dataArr) {
		int[][] semblanceMatrix = null;
		ArrayList<Double> itemsList = new ArrayList<Double>();
		for (int i = 0; i < dataArr.length; i++) {
			double items = dataArr[i][1];
			boolean ifAdd = false;
			if (itemsList.size()!=0) {
				for (int j = 0; j < itemsList.size(); j++) {
					if (items != itemsList.get(j)) {
						ifAdd = true;
					}else {
						ifAdd = false;
						break;
					} // of if....else....
				}// of for j
				if (ifAdd) {
					itemsList.add(items);
				}
			}else {
				itemsList.add(items);
			}// of if itemsList.size()!=0
			
		}// of for i
		double[] tempArray = ranksBubble(itemsList);
		itemsNameArr = tempArray;
		int semMaxSize = itemsList.size();
		semblanceMatrix = new int[semMaxSize][semMaxSize];		
		for (int i = 0; i < semblanceMatrix.length; i++) {
			for (int j = 0; j < semblanceMatrix[i].length; j++) {
				semblanceMatrix[i][j] = countNum(dataArr,i,j,tempArray);
//				System.out.println("semblanceMatrix["+i+"]["+j+"]:" + semblanceMatrix[i][j]);
			}

		}// of for i	
		
		// look the value semblanceMatrix
		System.out.println("the matrix of detail number:");
		for (int i = 0; i < semblanceMatrix.length; i++) {
			for (int j = 0; j < semblanceMatrix[i].length; j++) {
				System.out.print(semblanceMatrix[i][j]+" ");				
			}
			System.out.println("\n");
		}
		try {
			
			saveFile1(semblanceMatrix,fileUrl+"a.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return semblanceMatrix;
	}
	/**
	 * function count semblanceMatix value
	 * @throws Exception 
	 */
	public int countNum(double[][] dataArr, int i, int j, double[] list) {
		    int count=0;
		    double userId = dataArr[0][0];				
			// get every user's items			
			ArrayList<Object> user_list1 = new ArrayList<Object>();
			ArrayList<Double> userIterms = new ArrayList<Double>(); 
			for (int m = 0; m < dataArr.length; m++) {
				if (dataArr[m][0] == userId) {
					userIterms.add(dataArr[m][1]);
					//the last one
					if (m == (dataArr.length-1)) {
						double[] aa = new double[userIterms.size()];
						for (int k = 0; k < userIterms.size(); k++) {
							aa[k] = userIterms.get(k);
						}
						user_list1.add(aa);
					}// of if m == (dataArr.length-1
				}else {
					double aa[] = new double[userIterms.size()];
					for (int k = 0; k < userIterms.size(); k++) {
						aa[k] = userIterms.get(k);
					}
					user_list1.add(aa);
					userIterms.removeAll(userIterms);
					userIterms.add(dataArr[m][1]);
					userId = dataArr[m][0];
					
				}						
			}// of for int m = 0; m < dataArr.length; m++
			user_list = user_list1;
			if (i!=j) {
				for (int k = 0; k < user_list1.size(); k++) {
					int IAnfJ = 0;
					double[] tempList = (double[]) user_list1.get(k);				
					for (int l = 0; l < tempList.length; l++) {					
						if (tempList[l] == list[i]) {
							IAnfJ +=1;
						}else if (tempList[l] == list[j]){
							IAnfJ +=1;
						}	
					}
					if (IAnfJ == 2 && IAnfJ > 1) {
						count++;
					}
					
				}
			}else {
				for (int k = 0; k < user_list.size(); k++) {
					boolean isHas = false;
					double[] tempList = (double[]) user_list.get(k);
					for (int l = 0; l < tempList.length; l++) {
						if (tempList[l]==list[i]) {
							isHas = true;
							break;
						}
					}
					if (isHas) {
						count++;
					}
					
				}
			}
		return count;
	}//of funcion countNum()
	/**
	 * function compute semblance
	 * 
	 */
 
	
	/**
	 * function rank 
	 * 
	 */
	private double[] ranksBubble(ArrayList<Double> needList) {		
		double[] tempArr = new double[needList.size()];
		for (int i = 0; i < needList.size(); i++) {
			tempArr[i] = needList.get(i);
		}
		
		for (int i = 0; i < tempArr.length-1; i++) {
			for (int j = 0; j < tempArr.length-1; j++) {
				if(tempArr[j]>tempArr[j+1]){
					double temp = tempArr[j];
					tempArr[j] = tempArr[j+1];
					tempArr[j+1]= temp;					
				}
			}
		}
//		System.out.println("rank sort is ");
//		for (int i = 0; i < tempArr.length; i++) {
//			System.out.print(tempArr[i]+ "  ");
//		}
		return tempArr;
	}
	/**
	 * function compute semblance 
	 * 
	 */
	
	private String[][] itemsSemblance(int[][] tempArr2) {
		int Row = tempArr2.length;
		int col = tempArr2[0].length;				
		String[][] semblace = new String[Row][col];
		for (int i = 0; i < semblace.length; i++) {
			for (int j = 0; j < semblace[i].length; j++) {
				if (tempArr2[i][i]!=0 || tempArr2[j][j]!=0) {
					double temp=tempArr2[i][j]/Math.sqrt((tempArr2[i][i])*(tempArr2[j][j]));
					DecimalFormat dF = new DecimalFormat("#0.00");
					semblace[i][j] = dF.format(temp);

				}else {
					semblace[i][j] = "0";
					
				}
				
			}
		}
		
		System.out.println("\n this is the maxtrix of semblance:");
		for (int i = 0; i < semblace.length; i++) {
			for (int j = 0; j < semblace.length; j++) {
				System.out.print(semblace[i][j] +" / ");
			}
			System.out.println("\n");
		}
		try {
			saveFile(semblace,fileUrl+"b.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return semblace;
	}
	
	/**
	 * function save file 
	 * @throws IOException 
	 * 
	 */
   private void saveFile(String[][] semblanceMatrix,String fileName) throws IOException {
	File file = new File(fileName);
	DataOutputStream os = new DataOutputStream(new FileOutputStream(file));
	String temp = "";
	for (int i = 0; i < semblanceMatrix.length; i++) {
		for (int j = 0; j < semblanceMatrix[i].length; j++) {
			temp += semblanceMatrix[i][j]+"   ";
		}
		temp +="\n";
	}
	os.writeChars(temp);
	os.close();
	
}
   
   private void saveFile1(int[][] semblanceMatrix,String fileName) throws IOException {
	FileWriter writer = new FileWriter(fileName);
	File file = new File(fileName);
	DataOutputStream os = new DataOutputStream(new FileOutputStream(file));
	String temp = "";
	for (int i = 0; i < semblanceMatrix.length; i++) {
		for (int j = 0; j < semblanceMatrix[i].length; j++) {
			temp += semblanceMatrix[i][j]+"   ";
		}
		temp +="\n";
	}
	os.writeChars(temp);
	os.close();
   }
   
   /**
	 * function compute a item’s topN near 
	 *  semblance:产品的相似度矩阵  产品一排序（由低到高）
	 * itemName: 
	 */
   private ArrayList<Object> topNNeighbor(String[][] semblance, double[] itemName) {
	   System.out.println("itemName is:"+itemName.length);
	   for (int i = 0; i < itemName.length; i++) {
		 System.out.print(itemName[i]);
	   }// for test
	   ArrayList<Object> allItemsNeightbor = new ArrayList<Object>(); 
	   for (int i = 0; i < semblance.length; i++) {
		   ArrayList<Double> tempList = new ArrayList<Double>(); 
		   for (int j = 0; j < semblance[i].length; j++) {
			   double d = Double.valueOf(semblance[i][i]).doubleValue();
			   tempList.add(d);
		   }// of for j
		   double[] tempArr = ranksBubble(tempList);
		   double[] rankName = new double[semblance[i].length];
		   for (int j = 0; j < tempArr.length; j++) {
			    for (int j2 = 0; j2 < semblance[i].length; j2++) {
			    	double dd = Double.valueOf(semblance[i][j2]).doubleValue();
					if (tempArr[j]==dd) {
						rankName[j] = itemName[j2];
					}// of if
				}// of for j2
		   }// of for j
		   allItemsNeightbor.add(rankName);
	   }//  of for i
	  
	return allItemsNeightbor;
   }// of function topNNeighbor()
   
   /**
  	 * function recommend to someone 
  	 * @throws IOException 
  	 * 
  	 */
   
   private ArrayList<Double> recommendSomeone(int userId, ArrayList<Object> itemsNN, int K,  ArrayList<Object> user_list, double[] nameArr) {
	   double[] userIterms =  (double[]) user_list.get(userId-1);	   
	   int hadItermsLen = userIterms.length;
	   int everyKindReLen = K/hadItermsLen;
	   ArrayList<Double> reList = new ArrayList<Double>();
	   for (int i = 0; i < hadItermsLen; i++) {
		   double item = userIterms[i];
		   int itemID = 0;
		   for (int j = 0; j < nameArr.length; j++) {
			   if (item==nameArr[j]) {
					itemID = j;
			   }//  of if
		   }// of for j
		   double[] rankItemsNN = (double[]) itemsNN.get(itemID); 
		   for (int j = 0; j < everyKindReLen; j++) {
			   reList.add(rankItemsNN[j]);
		   }// of for j
	   }// of for i
	return reList;
    }// of function recommendSomeone()
   
  
   
}
