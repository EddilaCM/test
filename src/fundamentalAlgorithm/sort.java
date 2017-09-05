package fundamentalAlgorithm;

import sun.awt.image.OffScreenImage;


public class sort {
	/**
	 * @冒泡排序
	 * 结果：由大到小
	 * 比较相邻的元素。如果第一个比第二个大，就交换他们两个。
	 * 对每一对相邻元素作同样的工作，从开始第一对到结尾的最后一对。在这一点，最后的元素应该会是最大的数。
	 * 针对所有的元素重复以上的步骤，除了最后一个。
	 * 持续每次对越来越少的元素重复上面的步骤，直到没有任何一对数字需要比较。
	 */
	public int[] bubbleSort(int[] arr) {
		int temp = 0;
		int len = arr.length;
		for (int i = 0; i < len-1; i++) {
			for (int j = 0; j < len-1-i; j++) {
				if (arr[j] < arr[j+1]) {
					temp = arr[j];
					arr[j] = arr[j+1];
					arr[j+1] = temp;
				}
			}
		}
		
		return arr;
	}
	
	
	/**
	 * @快速排序
	 * 结果：由大到小
	 * 通过一趟排序将待排序记录分割成独立的两部分，其中一部分记录的关键字均比另一部分关键字小，则分别对这两部分继续进行排序，直到整个序列有序。
	 * 把整个序列看做一个数组，把第零个位置看做中轴，和最后一个比，如果比它小交换，比它大不做任何处理；交换了以后再和小的那端比，比它小不交换，比他大交换。这样循环往复，一趟排序完成，左边就是比中轴小的，右边就是比中轴大的，然后再用分治法，分别对这两个独立的数组进行排序。
	 * 
	 */
	public int[] quickSort(int[] Arr,int low, int high) {
		 int start = low;
		 int end = high;
		 int key = Arr[low];
		 while (end > start) {
			while (end>start && Arr[end]>=key) {//如果没有比关键值小的，比较下一个，直到有比关键值小的交换位置，然后又从前往后比较
				end--;
				if (Arr[end] <= key) {
					int temp = Arr[end];
					Arr[end] = Arr[start];
					Arr[start] = temp;
				}// of if
				
			}// of while
			while (end>start && Arr[start]<=key) {//如果没有比关键值大的，比较下一个，直到有比关键值大的交换位置
				start++;
				if (Arr[start]>=key) {
					int temp = Arr[start];
					Arr[start] = Arr[end];
					Arr[end] = temp;
				} // of if
				//此时第一次循环比较结束，关键值的位置已经确定了。左边的值都比关键值小，右边的值都比关键值大，但是两边的顺序还有可能是不一样的，进行下面的递归调用
			}// of while	
			
			//递归
			if (start>low) {
				quickSort(Arr, low, start-1);//左边序列。第一个索引位置到关键值索引-1
			}// of if 
			if (end<high) {
				quickSort(Arr, end+1, high);//右边序列。从关键值索引+1到最后一个
			}// of if
			
		 }		 
		
		return Arr;
	}
	
	
	/**
	 * @选择排序
	 * 在未排序序列中找到最小元素，存放到排序序列的起始位置 
	 * 再从剩余未排序元素中继续寻找最小元素，然后放到排序序列末尾。
	 *  以此类推，直到所有元素均排序完毕。
	 */
	public int[] choseResort(int [] arr) {
		int len = arr.length;//数组的长度
		int temp = 0; //中间变量
		for (int i = 0; i < len; i++) {
			int k = i;//待确定的位子
			//选择出应该在第i个位子的数
			for (int j = len-1; j < i; j--) {
				if (arr[j]<arr[k]) {
					k = j;
				}// of if
			}// of for j
			temp = arr[i];
			arr[i] = arr[k];
			arr[k] = temp;			
		}// of for i		
		return arr;
	}
	
	
	/**
	 * @插入排序
	 * 每步将一个待排序的记录，按其顺序码大小插入到前面已经排序的字序列的合适位置（从后向前找到合适位置后），直到全部插入排序完为止。
	 * 从第一个元素开始，该元素可以认为已经被排序
     * 取出下一个元素，在已经排序的元素序列中从后向前扫描 
     * 如果该元素（已排序）大于新元素，将该元素移到下一位置  
     * 重复步骤3，直到找到已排序的元素小于或者等于新元素的位置  
     * 将新元素插入到该位置中  
     * 重复步骤2  
     * 时间复杂度：O（n^2）
	 */
	public int[] insertSort(int[] arr) {
		int len = arr.length;
		int temp = 0;
		int j = 0;
		for (int i = 0; i < len; i++) {
			temp= arr[i];
			for (j = i; j < 0 && temp<arr[j-1]; j--) {
				arr[j] = arr[j-1];
			}// of for j
			arr[j] = temp;
		}// of for i
		return arr;
	}
	
	/**
	 * @希尔排序
	 * 先将整个待排序的记录序列分割成为若干子序列分别进行直接插入排序，待整个序列中的记录“基本有序”时，再对全体记录进行依次直接插入排序。
	 * 希尔排序的原理:根据需求，如果你想要结果从大到小排列，它会首先将数组进行分组，然后将较大值移到前面，较小值
     * 移到后面，最后将整个数组进行插入排序，这样比起一开始就用插入排序减少了数据交换和移动的次数，可以说希尔排序是加强
     * 版的插入排序
     * 
     * 时间复杂度：O（n^2）.
	 */
	public int[] shellSort(int[] arr) {
		int j = 0;
		int temp = 0;
		//将每次的步长缩短为原来的半
		for (int increment = arr.length/2;increment > 0; increment /= 2) {
			for (int i = increment; i < arr.length; i++) {
				temp = arr[i];
				for (j = i; j>=increment; j -= increment) {
					if (temp>arr[j-increment]) {
						arr[j] = arr[j-increment];
					}else{
						break;
					}// of if....else....
				}// of for j 
				arr[j] = temp;
			}// of for i
		}// of for increment
		return arr;
	}
	
	

	/**
	 * @param main
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		test bubbleSort
		int[] test = {12,52,9,7,16,6,7,11,-5,22};
		sort aSort = new sort();
		int[] resultBubble = aSort.bubbleSort(test);
		System.out.println("BubbleSort results:");
		for (int i = 0; i < resultBubble.length; i++) {
			System.out.print(resultBubble[i]+" ");
		}// of for bubble
		System.out.println('\n');
		
		int[] reusultQuick = aSort.quickSort(test, 0, test.length-1);
		System.out.println("quickSort results:");
		for (int i = 0; i < reusultQuick.length; i++) {
			System.out.print(reusultQuick[i]+" ");
		}// of for quick
		
		System.out.println("\n\nchoseSort results:");		
		int[] resultChose = aSort.choseResort(test);
		for (int i = 0; i < resultChose.length; i++) {
			System.out.print(resultChose[i]+" ");
		}// of for chose
		
		System.out.println("\n\nInsertSort results:");		
		int[] resultInsert = aSort.insertSort(test);
		for (int i = 0; i < resultInsert.length; i++) {
			System.out.print(resultInsert[i]+" ");
		}// of for chose
		
		System.out.println("\n\nshellSort results:");		
		int[] resultShell = aSort.shellSort(test);
		for (int i = 0; i < resultShell.length; i++) {
			System.out.print(resultShell[i]+" ");
		}// of for chose
		
		

	}// of main()

}// of class sort
