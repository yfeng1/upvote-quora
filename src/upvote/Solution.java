package upvote;
import java.util.*;

public class Solution {
	static int size_all;
	static int size_of_window;
    
	public enum State{
		MOINS(-1),PLUS(1),EQUALS(0),STOP(88),NON_SENSE(-20) ;
		
		private int value;
		private State(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
		
		public static State ope(State x1, State x2) {
	        if (x1 == PLUS && x2 == PLUS) return PLUS;
	        if (x1 == PLUS && x2 == MOINS) return STOP; 
	        if (x1 == PLUS && x2 == EQUALS) return PLUS;
	        if (x1 == PLUS && x2 == STOP) return STOP;
	        if (x1 == MOINS && x2 == PLUS) return STOP; 
	        if (x1 == MOINS && x2 == MOINS) return MOINS;
	        if (x1 == MOINS && x2 == EQUALS) return MOINS;
	        if (x1 == MOINS && x2 == STOP) return STOP;
	        if (x1 == EQUALS && x2 == PLUS) return PLUS;
	        if (x1 == EQUALS && x2 == MOINS) return MOINS;
	        if (x1 == EQUALS && x2 == EQUALS) return EQUALS;
	        if (x1 == EQUALS && x2 == STOP) return STOP;
	        if (x1 == STOP && x2 == PLUS) return STOP;
	        if (x1 == STOP && x2 == MOINS) return STOP;
	        if (x1 == STOP && x2 == EQUALS) return STOP;
	        if (x1 == STOP && x2 == STOP) return STOP;
	        return NON_SENSE; // should never return this;
	    }
	}
	
    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner sc = new Scanner(System.in);
        
        // The line with all data
        //byte[] first_line_res = new byte[size_all_data-1];
        
        calculateAndPrintResults(sc);
    }

	public static String calculateAndPrintResults(Scanner sc) {
		
		size_all = sc.nextInt();
        size_of_window = sc.nextInt();
        
        sc.nextLine();
        State[] first_line_result = new State[size_all-1];
        //System.out.println("size_all_data-size_of_window: " + (size_all_data-size_of_window) + "size_all_data :" + size_all_data);
        
        int[] allOutPut = new int[size_all-size_of_window + 1];
        int first_int = sc.nextInt();
        int i=0;
        for ( int j= 1; j < size_all; j++){
            int second_int = sc.nextInt();
            State result_t = State.NON_SENSE;
            //System.out.println("first_int:" + first_int + "second_int :" + second_int);
            if (first_int > second_int) {
                result_t = State.MOINS;
            } else if (first_int == second_int) {
                result_t = State.EQUALS;
            } else {
                result_t = State.PLUS;
            }
            
            if (result_t == State.NON_SENSE) {
            	throw new IllegalArgumentException("NON SENSE OPERATION");
            }
            
            first_line_result[i] = result_t;
            
            //System.out.println("i:" + i + "res :" + allResult[0][i]);
            first_int = second_int;
            i++;
        }
        
        // The first line is calculated.
        
        sc.close();
        for(int outIndex=0; outIndex<=(size_all - size_of_window);outIndex++) {
        	allOutPut[outIndex] = calculateSumFromFirstLine(outIndex, Arrays.copyOfRange(first_line_result, outIndex,  outIndex + size_of_window - 1));
        }
            
        StringBuilder stringBuilder = new StringBuilder();
        for (int output = 0; output <=size_all-size_of_window; output++){
            stringBuilder.append(allOutPut[output]);
            stringBuilder.append("\n");
        }
        
        return stringBuilder.toString();
	}

	private static int calculateSumFromFirstLine(int outIndex, State[] resultStates) {
//		State[][] allResult = new State[size_of_window][size_of_window];
//		int sum = 0;
//		if (size_of_window > 1){
//		    for (int line=1;line<(size_of_window-1);line++){
//		        for (int index=outIndex;index<(size_of_window-line-1+outIndex);index++){
//		            allResult[line][index] = State.ope(allResult[line-1][index],allResult[line-1][index+1] ) ;   
//		            sum+=allResult[line][index]== State.STOP ? 0 :allResult[line][index].getValue();
//		            //System.out.println("j:" + j + "k" + k + " res " + allResult[j][k]);
//		        }
//		    }    
//		}
//		return sum;
		int sum = 0;
		if (size_of_window == 1) {
			return getValueToSum(resultStates[0]);
		} else {
			State[] currentLine = resultStates;
			State[] nextLine = new State[currentLine.length - 1];
			while (nextLine.length > 0) {
				for (int i = 0 ; i < nextLine.length ; i++){
					sum += getValueToSum(currentLine[i]);
					nextLine[i] = State.ope(currentLine[i],currentLine[i+1] ) ;
				}
				sum +=getValueToSum(currentLine[currentLine.length-1]);
				currentLine = nextLine;
				nextLine = new State[currentLine.length - 1];
			}
			return sum;
		}
	}

	private static int getValueToSum(State state) {
		return state ==State.STOP ? 0 :state.getValue();
	}
    
    
}