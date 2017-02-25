/**
 * This class implements seven different comparison sorts 
 * <ul>
 * <li>selection sort</li>
 * <li>insertion sort</li>
 * <li>merge sort</li>
 * <li>quick sort</li>
 * <li>heap sort</li>
 * <li>selection2 sort</li>
 * <li>(extra credit) insertion2 sort</li>
 * </ul>
 * It also has a method that runs all the sorts on the same input array and
 * prints out statistics.
 */

public class ComparisonSort {
	static int dataCompares;
	static int dataSwaps;
	static long time;


	/**
	 * Sorts the given array using the selection sort algorithm. You may use
	 * either the algorithm discussed in the on-line reading or the algorithm
	 * discussed in lecture (which does fewer data moves than the one from the
	 * on-line reading). Note: after this method finishes the array is in sorted
	 * order.
	 * 
	 * @param <E>  the type of values to be sorted
	 * @param A    the array to sort
	 */
	public static <E extends Comparable<E>> void selectionSort(E[] A) {

		// TODO: implement this sorting algorithm
		dataSwaps = 0;
		int passes = A.length-1;
		time = System.currentTimeMillis();

		//reset 
		SortObject.resetCompares();

		for(int i = 0; i<passes; i++){
			//fields
			int minIndex = i;
			boolean changed = false;

			for(int j = i+1; j<A.length; j++){

				if( ((SortObject)A[j]).compareTo(((SortObject)A[minIndex]))  < 0){
					minIndex = j;
					changed = true;
				}
			}

			if(changed){
				E temp = A[minIndex];
				A[minIndex] = A[i];
				A[i] = temp;
				dataSwaps += 3;
			} 
		}
		time = System.currentTimeMillis() - time;
		dataCompares = SortObject.getCompares();

	}

	/**
	 * Sorts the given array using the insertion sort algorithm. Note: after
	 * this method finishes the array is in sorted order.
	 * 
	 * @param <E>  the type of values to be sorted
	 * @param A    the array to sort
	 */
	public static <E extends Comparable<E>> void insertionSort(E[] A) {
		dataSwaps = 0;
		time = System.currentTimeMillis();
		//reset 
		SortObject.resetCompares();

		for(int i = 1; i<A.length; i++){
			E temp = A[i];
			dataSwaps ++;
			int j;
			for(j = i-1; j>= 0 && ((SortObject)A[j]).compareTo((SortObject)temp) > 0; j--){
				A[j+1] = A[j];
				dataSwaps ++;
			}

			A[j+1] = temp;
			dataSwaps++;
		}

		time = System.currentTimeMillis() - time;
		dataCompares = SortObject.getCompares();

	}

	/**
	 * Sorts the given array using the merge sort algorithm. Note: after this
	 * method finishes the array is in sorted order.
	 * 
	 * @param <E>  the type of values to be sorted
	 * @param A    the array to sort
	 */
	public static <E extends Comparable<E>> void mergeSort(E[] A) {
		// TODO: implement this sorting algorithm
		dataSwaps = 0;
		time = System.currentTimeMillis();
		//reset 
		SortObject.resetCompares();

		mergeAux(A, 0, A.length - 1); // call the aux. function to do all the work

		time = System.currentTimeMillis() - time;
		dataCompares = SortObject.getCompares();
	}


	private static <E extends Comparable<E>> void mergeAux(E[] A, int low, int high) {
		// base case
		if (low == high) return;

		// recursive case

		// Step 1: Find the middle of the array (conceptually, divide it in half)
		int mid = (low + high) / 2;

		// Steps 2 and 3: Sort the 2 halves of A
		mergeAux(A, low, mid);
		mergeAux(A, mid+1, high);

		// Step 4: Merge sorted halves into an auxiliary array
		E[] tmp = (E[])(new Comparable[high-low+1]);
		int left = low;    // index into left half
		int right = mid+1; // index into right half
		int pos = 0;       // index into tmp

		while ((left <= mid) && (right <= high)) {
			// choose the smaller of the two values "pointed to" by left, right
			// copy that value into tmp[pos]
			// increment either left or right as appropriate
			// increment pos
			if (((SortObject)A[left]).compareTo(((SortObject)A[right])) <= 0) {
				tmp[pos] = A[left];
				dataSwaps++;
				left++;
			}
			else {
				tmp[pos] = A[right];
				right++;
				dataSwaps++;
			}
			pos++;
		}


		// when one of the two sorted halves has "run out" of values, but
		// there are still some in the other half, copy all the remaining 
		// values to tmp
		// Note: only 1 of the next 2 loops will actually execute
		while (left <= mid) {
			tmp[pos] = A[left];  
			dataSwaps++;
			left++;
			pos++;
		}
		while (right <= high) { 
			tmp[pos] = A[right]; 
			dataSwaps++;
			right++;
			pos++;
		}

		System.arraycopy(tmp, 0, A, low, tmp.length);
		dataSwaps += tmp.length;
	}

	/**
	 * Sorts the given array using the quick sort algorithm, using the median of
	 * the first, last, and middle values in each segment of the array as the
	 * pivot value. Note: after this method finishes the array is in sorted
	 * order.
	 * 
	 * @param <E>  the type of values to be sorted
	 * @param A   the array to sort
	 */
	public static <E extends Comparable<E>> void quickSort(E[] A) {

		dataSwaps = 0;
		time = System.currentTimeMillis();

		//reset compares
		SortObject.resetCompares();

		quickAux(A, 0, A.length-1);

		time = System.currentTimeMillis() - time;
		dataCompares = SortObject.getCompares();

	}


	private static <E extends Comparable<E>> void quickAux(E[] A, int low, int high) {
		if (high-low < 4) {

			for (int i = low; i < high + 1; i++) {
				E temp = A[i];
				dataSwaps++;
				int j;
				for (j = i - 1; j >= 0 && ((SortObject)A[j]).compareTo((SortObject)temp) > 0; j--) {
					A[j + 1] = A[j];
					dataSwaps++;
				}
				A[j + 1] = temp;
				dataSwaps++;
			}

		}
		else {
			int right = partition(A, low, high);
			quickAux(A, low, right);
			quickAux(A, right+2, high);
		}
	}

	private static <E extends Comparable<E>> int partition(E[] A, int low, int high) {
		// precondition: A.length > 3

		E pivot = medianOfThree(A, low, high); // this does step 1

		int left = low+1; 
		int right = high-2;
		while ( left <= right ) {
			while (A[left].compareTo(pivot) < 0) left++;
			while (A[right].compareTo(pivot) > 0) right--;
			if (left <= right) {
				E temp = A[left];
				A[left] = A[right];
				A[right] = temp;
				dataSwaps+= 3;
				left++;
				right--;
			}
		}

		E temp2 = A[right + 1];
		A[right + 1] = A[high - 1];
		A[high - 1] = temp2;
		dataSwaps+=3;

		return right;
	}

	private static <E extends Comparable<E>> E medianOfThree(E[] A, int low, int high){
		E left = A[low];
		E right = A[high];
		E center = A[(low + high) / 2];
		dataSwaps += 3;

		if ((((SortObject)center).compareTo((SortObject)left) <= 0 && ((SortObject)center).compareTo((SortObject)right) >= 0) ||
				(((SortObject)center).compareTo((SortObject)left) >= 0 && ((SortObject)center).compareTo((SortObject)right) <= 0)) {

			E temp = A[(low + high) / 2];
			A[(low + high) / 2] = A[high - 1];
			A[high - 1] = temp;
			dataSwaps += 3;

			if (((SortObject)left).compareTo((SortObject)right) >= 0) {
				E temp2 = A[low];
				A[low] = A[high];
				A[high] = temp2;
				dataSwaps += 3;
			}

			return center;
		}

		else if ((((SortObject)left).compareTo((SortObject)center) <= 0 && ((SortObject)left).compareTo((SortObject)right) >= 0) ||
				(((SortObject)left).compareTo((SortObject)center) >= 0 && ((SortObject)left).compareTo((SortObject)right) <= 0)) {


			E temp = A[low];
			A[low] = A[high - 1];
			A[high - 1] = temp;
			dataSwaps += 3;


			if (((SortObject)center).compareTo((SortObject)right) >= 0) {
				E temp2 = A[high];
				A[high] = A[(low + high) / 2];
				E temp3 = A[low];
				A[low] = temp2;
				A[(low + high) / 2] = temp3;
				dataSwaps += 5;
			}
			else {
				E temp2 = A[low];
				A[low] = A[(high + low) / 2];
				A[(high + low) / 2] = temp2;
				dataSwaps += 3;

			}


			return left;
		}
		else {

			E temp = A[high];
			A[high] = A[high - 1];
			A[high - 1] = temp;
			dataSwaps += 3;


			if (((SortObject)center).compareTo((SortObject)left) >= 0) {


				E temp2 = A[high];
				A[high] = A[(high + low) / 2];
				A[(high + low) / 2] = temp2;
				dataSwaps += 3;


			}

			else {
				E temp2 = A[high];
				A[high] = A[low];
				A[low] = temp2;
				E temp3 = A[(high + low) / 2];
				A[(high + low) / 2] = A[low];
				A[low] = temp3;
				dataSwaps += 6;

			}



			return right;
		}

	}


	/**
	 * Sorts the given array using the heap sort algorithm outlined below. Note:
	 * after this method finishes the array is in sorted order.
	 * <p>
	 * The heap sort algorithm is:
	 * </p>
	 * 
	 * <pre>
	 * for each i from 1 to the end of the array
	 *     insert A[i] into the heap (contained in A[0]...A[i-1])
	 *     
	 * for each i from the end of the array up to 1
	 *     remove the max element from the heap and put it in A[i]
	 * </pre>
	 * 
	 * @param <E>  the type of values to be sorted
	 * @param A    the array to sort
	 */
	public static <E extends Comparable<E>> void heapSort(E[] A) {

		dataSwaps = 0;
		time = System.currentTimeMillis();

		//reset compares
		SortObject.resetCompares();

		//put smallest item in position 0
		int minPos = 0;
		SortObject min = (SortObject) A[0];

		for(int i = 0; i<A.length-1; i++ ){
			if(((SortObject)A[i]).compareTo(min) <0){
				min = (SortObject) A[i];
				minPos = i;
			}
		}

		E temp = A[minPos];
		A[minPos] = A[0];
		A[0] = temp;
		dataSwaps += 3;


		for(int i =1; i<A.length; i++){
			int index = i;

			//Keep swapping items until heap ordering constraint satisfied
			while( (index !=1 ) && ((SortObject)A[index/2]).compareTo(((SortObject)A[index])) < 0){
				E temp1 = A[index];
				A[index] = A[index/2];
				A[index/2] = temp1;
				index = index/2;
				dataSwaps += 3;
			}
		}

		for(int i = A.length-1; i > 0; i--){

			//data fields
			E temp1 = A[1];
			A[1] = A[i];
			A[i] = temp1;
			dataSwaps += 3;

			int curr = 1;

			//re-heapify if necessary
			while( (curr*2  <= i -1) && ( ((SortObject)A[curr]).compareTo(((SortObject)A[curr*2])) < 0 || 
					((SortObject)A[curr]).compareTo(((SortObject)A[curr*2+1])) <0 )){

				if( (curr*2 +1 > i -1) && (((SortObject)A[curr]).compareTo(((SortObject)A[curr*2])) > 0)){
					curr = 1000000000;

				}else{

					int maxChild = -1;
					if(curr*2 +1  < i){

						if( ((SortObject)A[(curr*2)+1]).compareTo(((SortObject)A[curr*2]))  <= 0 )
							maxChild= curr*2;
						else{
							maxChild = (curr*2) + 1;
						}
					}else{
						maxChild = (curr*2);
					}


					E temp2 = A[curr];
					A[curr] = A[maxChild];
					A[maxChild] = temp2;
					curr = maxChild;
					dataSwaps += 3;
				}
			}
		}
		time = System.currentTimeMillis() - time;
		dataCompares = SortObject.getCompares();
	}
	public static <E extends Comparable<E>> void selection2Sort(E[] A) {

		dataSwaps = 0;
		SortObject.resetCompares();
		time = System.currentTimeMillis();

		int begin = 0;
		int end = A.length - 1;


		while (begin < end) {
			int minIndex = begin;
			int maxIndex = begin;
			int offSet = 0;

			for (int i = begin; i < end; i += (2 + offSet)) {


				if (end - i == 2) {
					offSet = -1;
				}

				if (((SortObject)A[i]).compareTo((SortObject)A[i+1]) < 0) {
					if (((SortObject)A[minIndex]).compareTo((SortObject)A[i]) > 0) {
						minIndex = i;
					}
					if (((SortObject)A[maxIndex]).compareTo((SortObject)A[i+1]) < 0) {
						maxIndex = i+1;
					}
				}
				else {
					if (((SortObject)A[minIndex]).compareTo((SortObject)A[i+1]) > 0) {
						minIndex = i+1;
					}
					if (((SortObject)A[maxIndex]).compareTo((SortObject)A[i]) < 0) {
						maxIndex = i;
					}
				}


			}


			if (begin != maxIndex) {
				E temp = A[begin];
				A[begin] = A[minIndex];
				A[minIndex] = temp;

				E temp2 = A[end];
				A[end] = A[maxIndex];
				A[maxIndex] = temp2;
				dataSwaps += 6;


			}

			else {

				E temp2 = A[end];
				A[end] = A[maxIndex];
				A[maxIndex] = temp2;
				dataSwaps += 3;


				if (minIndex != end + 1) {

					E temp = A[begin];
					A[begin] = A[minIndex];
					A[minIndex] = temp;
					dataSwaps += 3;

				}
			}
			end--;
			begin++;

		}

		time = System.currentTimeMillis() - time;
		dataCompares = SortObject.getCompares();

	}
	/**
	 * <b>Extra Credit:</b> Sorts the given array using the insertion2 sort 
	 * algorithm outlined below.  Note: after this method finishes the array 
	 * is in sorted order.
	 * <p>
	 * The insertion2 sort is a bi-directional insertion sort that sorts the 
	 * array from the center out towards the ends.  The insertion2 sort 
	 * algorithm is:
	 * </p>
	 * <pre>
	 * precondition: A has an even length
	 * left = element immediately to the left of the center of A
	 * right = element immediately to the right of the center of A
	 * if A[left] > A[right]
	 *     swap A[left] and A[right]
	 * left--, right++ 
	 *  
	 * // At the beginning of every iteration of this loop, we know that the elements
	 * // in A from A[left+1] to A[right-1] are in relative sorted order.
	 * do
	 *     if (A[left] > A[right])
	 *         swap A[left] and A[right]
	 *  
	 *     starting with with A[right] and moving to the left, use insertion sort 
	 *     algorithm to insert the element at A[right] into the correct location 
	 *     between A[left+1] and A[right-1]
	 *     
	 *     starting with A[left] and moving to the right, use the insertion sort 
	 *     algorithm to insert the element at A[left] into the correct location 
	 *     between A[left+1] and A[right-1]
	 *  
	 *     left--, right++
	 * until left has gone off the left edge of A and right has gone off the right 
	 *       edge of A
	 * </pre>
	 * <p>
	 * This sorting algorithm described above only works on arrays of even 
	 * length.  If the array passed in as a parameter is not even, the method 
	 * throws an IllegalArgumentException
	 * </p>
	 *
	 * @param  A the array to sort
	 * @throws IllegalArgumentException if the length or A is not even
	 */    
	public static <E extends Comparable<E>> void insertion2Sort(E[] A) throws IllegalArgumentException  { 
		dataSwaps = 0;
		time = System.currentTimeMillis();

		//reset compares
		SortObject.resetCompares();

		if(A.length%2 != 0){
			throw new IllegalArgumentException();
		}

		int left = (A.length/2) -1;
		int right = (A.length/2);
	
		//master for loop
		for(int i = 0; i < (A.length/2); i++){

			//compare the two elements we are inserting to determine which one is max and min
			//if left is bigger, swap
			if (((SortObject)A[left]).compareTo((SortObject)A[right]) > 0) {
				E temp = A[left];
				A[left] = A[right];
				A[right] = temp;
				dataSwaps+=3;
			}

			//fields
			int insertMax = right-1;
			boolean minFound = false; 
			boolean maxFound = false;
			E leftTemp = A[left];
			E rightTemp = A[right];
			dataSwaps+=2;

			for(int j = left+1; j< right && (!minFound || !maxFound ) ; j++){

				//MIN
				if(!minFound){
					if( ((SortObject)A[left]).compareTo((SortObject)A[j]) < 0){

						minFound = true;
						
						//make room for inserting min
						for(int e = left; e< j-1; e++){
							A[e] = A[e+1];
							dataSwaps++;
						}

						//put left into place
						A[j-1] = leftTemp;	
						dataSwaps++;		
					}
				}

				//MAX
				if(!maxFound){
					//found spot
					if(((SortObject)A[right]).compareTo((SortObject)A[insertMax]) > 0){

						maxFound = true;
						
						//make room for inserting min
						for(int e = right; e> insertMax+1; e--){
							A[e] = A[e-1];
							dataSwaps++;
						}
						
						//put lef into place
						A[insertMax+1] = rightTemp;	
						dataSwaps++;
					}
				}
				insertMax--;
			}
			left--;
			right++;
		}	
		time = System.currentTimeMillis() - time;
		dataCompares = SortObject.getCompares();
		
	}


	/**
	 * Internal helper for printing rows of the output table.
	 * 
	 * @param sort          name of the sorting algorithm
	 * @param compares      number of comparisons performed during sort
	 * @param moves         number of data moves performed during sort
	 * @param milliseconds  time taken to sort, in milliseconds
	 */
	private static void printStatistics(String sort, int compares, int moves,
			long milliseconds) {
		System.out.format("%-23s%,15d%,15d%,15d\n", sort, compares, moves, 
				milliseconds);
	}

	/**
	 * Sorts the given array using the six (seven with the extra credit)
	 * different sorting algorithms and prints out statistics. The sorts 
	 * performed are:
	 * <ul>
	 * <li>selection sort</li>
	 * <li>insertion sort</li>
	 * <li>merge sort</li>
	 * <li>quick sort</li>
	 * <li>heap sort</li>
	 * <li>selection2 sort</li>
	 * <li>(extra credit) insertion2 sort</li>
	 * </ul>
	 * <p>
	 * The statistics displayed for each sort are: number of comparisons, 
	 * number of data moves, and time (in milliseconds).
	 * </p>
	 * <p>
	 * Note: each sort is given the same array (i.e., in the original order) 
	 * and the input array A is not changed by this method.
	 * </p>
	 * 
	 * @param A  the array to sort
	 */
	static public void runAllSorts(SortObject[] A) {
		
		//make copies of arrays that are passed in to use to test each different sort
		SortObject[] insert = new  SortObject[A.length];
		System.arraycopy(A, 0, insert, 0, A.length);

		SortObject[] merge = new  SortObject[A.length];
		System.arraycopy(A, 0, merge, 0, A.length);

		SortObject[] quick = new  SortObject[A.length];
		System.arraycopy(A, 0, quick, 0, A.length);

		SortObject[] heap = new  SortObject[A.length];
		System.arraycopy(A, 0, heap, 0, A.length);

		SortObject[] selection2 = new  SortObject[A.length];
		System.arraycopy(A, 0, selection2, 0, A.length);

		SortObject[] insertion2 = new  SortObject[A.length];
		System.arraycopy(A, 0, insertion2, 0, A.length);

		//formating
		System.out.format("%-23s%15s%15s%15s\n", "algorithm", "data compares", 
				"data moves", "milliseconds");
		System.out.format("%-23s%15s%15s%15s\n", "---------", "-------------", 
				"----------", "------------");

		//run each sort and print statistics about what it did
		selectionSort(A);
		printStatistics("selection",dataCompares, dataSwaps, time);

		insertionSort(insert);
		printStatistics("insert",dataCompares, dataSwaps, time);

		mergeSort(merge);
		printStatistics("merge",dataCompares, dataSwaps, time);

		quickSort(quick);
		printStatistics("quick",dataCompares, dataSwaps, time);

		heapSort(heap);
		printStatistics("heap",dataCompares, dataSwaps, time);

		selection2Sort(selection2);
		printStatistics("selection2",dataCompares, dataSwaps, time);

		insertion2Sort(insertion2);
		printStatistics("insertion2",dataCompares, dataSwaps, time);
	}
}
