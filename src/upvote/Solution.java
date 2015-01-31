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
        
        int[] allOutPut = new int[size_all-size_of_window + 1];
        int first_int = sc.nextInt();
        for ( int j= 1; j < size_all; j++){
            int second_int = sc.nextInt();
            State result_t = State.NON_SENSE;
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
            first_line_result[j-1] = result_t;
            first_int = second_int;
        }
        
        // The first line is calculated.
        sc.close();
        
        if (size_of_window == 1) {
        	for (int i =0 ; i < first_line_result.length ; i++){
        		allOutPut[i]=getValueToSum(first_line_result[i]);
        	}
		} else {
			// first step, first window, we calculate from the bottom
			// 1  1 -1  0
			//  1  X -1
			//   X  X
			//    X
			State[] currentWindow = Arrays.copyOfRange(first_line_result, 0,  0 + size_of_window - 1);
			State[] leftColumnOutPut = new State[size_of_window - 1];
			State[] rightColumnOutput = new State[size_of_window - 1];
			allOutPut[0] = calculateSumFromFirstLineForFirstWindow(currentWindow, leftColumnOutPut, rightColumnOutput);
			int sum = allOutPut[0];
			// for all other steps, we use the right column of value, and the new one to do the calcul
			for(int outIndex=1; outIndex < (size_all - size_of_window);outIndex++) {
				State[] currentRightColumnOutput = new State[size_of_window -1];
				currentRightColumnOutput[0] = first_line_result[outIndex + size_of_window];
				sum += getValueToSum(currentRightColumnOutput[0]);
				sum -= getValueToSum(leftColumnOutPut[0]);
				for (int line = 1 ; line < size_of_window -1; line ++) {
					currentRightColumnOutput[line] = State.ope(rightColumnOutput[line-1],currentRightColumnOutput[line-1]);
					sum += getValueToSum(currentRightColumnOutput[line]);
					sum -= getValueToSum(leftColumnOutPut[line]);
				}
				sum -= getValueToSum(leftColumnOutPut[size_of_window - 2]);
				allOutPut[outIndex] = sum;
			}
		}
        
            
        StringBuilder stringBuilder = new StringBuilder();
        for (int output = 0; output <= size_all-size_of_window; output++){
            stringBuilder.append(allOutPut[output]);
            stringBuilder.append("\n");
        }
        
        return stringBuilder.toString();
	}


	private static int calculateSumFromFirstLineForFirstWindow(State[] resultStates, State[] leftColumnOutput, State[] rightColumnOutput) {
		int sum = 0;
		State[] currentLine = resultStates;
		State[] nextLine = new State[currentLine.length - 1];
		int line = 0;
//		rightColumnOutput[line] = currentLine[currentLine.length-1];
//		leftColumnOutput[line++] = currentLine[0]; 
		while (currentLine.length > 0) {
			// CURRENT LINE 1 -1  0
			// NEXT LINE     X  -1
			
			// NEXT LINE       X
			for (int i = 0 ; i < currentLine.length-1 ; i++){
				sum += getValueToSum(currentLine[i]);
				nextLine[i] = State.ope(currentLine[i],currentLine[i+1] ) ;
			}
			rightColumnOutput[line] = currentLine[currentLine.length-1];
			leftColumnOutput[line++] = currentLine[0];
			sum +=getValueToSum(currentLine[currentLine.length-1]);
			currentLine = nextLine;
			if (currentLine.length > 0) {
				nextLine = new State[currentLine.length - 1];
			}
		}
		// last two lines
//		sum+=getValueToSum(currentLine[0]);
//		sum+=getValueToSum(currentLine[1]);
//		State lastValueAtTop = State.ope(currentLine[0],currentLine[1] );
//		sum+=getValueToSum(lastValueAtTop);
//		rightColumnOutput[line] = currentLine[1];
//		leftColumnOutput[line++] = currentLine[0];
//		leftColumnOutput[line] = lastValueAtTop;
		//sum+=getValueToSum(currentLine[0]);
		//leftColumnOutput[line] = currentLine[0];
		return sum;
	}

	private static int getValueToSum(State state) {
		return state ==State.STOP ? 0 :state.getValue();
	}
    
    
}