package upvote;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Solution {
	static int size_all;
	static int size_of_window;
    
	public enum State{
		MOINS(-1),PLUS(1),EQUALS(0),NON_SENSE(-10);
		
		private int value;
		private State(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}
	
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
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
        sc.close();
        
        if (size_of_window == 1){
        	// do nothing
        } else if (size_of_window == 2) {
        	for (int i =0 ; i < first_line_result.length ; i++){
        		allOutPut[i]=first_line_result[i].getValue();
        	}
        } else {
        	// The first line is calculated.
        	// Find sequence increasing, and sequence decreasing 
        	List<StateSequence> currentWindow = new ArrayList<StateSequence>();
        	
        	// The first window, we have to calculate all StateSequences
        	StateSequence currentSequence = new StateSequence(0, first_line_result[0]);
        	currentWindow.add(currentSequence);
        	if (first_line_result.length > 1) {
        		for (int winIndex = 1; winIndex < size_of_window-1 ; winIndex ++){
        			try {
        				currentSequence.putOneElementToRightSide(first_line_result[winIndex]);
        			} catch (NewSequenceAtRightException e) {
        				StateSequence nextSequence = new StateSequence(winIndex, first_line_result[winIndex]); 
        				currentWindow.add(nextSequence);
        				currentSequence = nextSequence;
        			}
        		}
        	}
        	
        	allOutPut[0] = getSumFromSequencesInWindow(currentWindow);
        	
        	// For further window, move the window and re-modify the sequence;
        	for (int outIndex=1; outIndex <=(size_all - size_of_window); outIndex ++){
        		try {
					currentWindow.get(0).removeOneElementFromLeftSide(first_line_result[outIndex]);
				} catch (LastElementAtLeftException e) {
					currentWindow.remove(0);
				} 
        		try {
					State rightValue = first_line_result[outIndex+size_of_window-2];
					if (!currentWindow.isEmpty()) {
						currentWindow.get(currentWindow.size()-1).putOneElementToRightSide(rightValue);
					} else {
						StateSequence lastSequence = new StateSequence(outIndex+size_of_window-2, first_line_result[outIndex+size_of_window-2]); 
						currentWindow.add(lastSequence);
					}
				} catch (NewSequenceAtRightException e) {
					StateSequence lastSequence = new StateSequence(outIndex+size_of_window-2, first_line_result[outIndex+size_of_window-2]); 
					currentWindow.add(lastSequence);
				}
        		allOutPut[outIndex] = getSumFromSequencesInWindow(currentWindow);
        	}
        	
        	
        	
        	
//	        for(int outIndex=0; outIndex<=(size_all - size_of_window);outIndex++) {
//	        	allOutPut[outIndex] = 0; 
//	        			int sum = 0;
//						if (size_of_window == 1) {
//							//return getValueToSum(windowStates[0]);
//						} else {
//							State[] currentLine = Arrays.copyOfRange(first_line_result, outIndex,  outIndex + size_of_window - 1);
//							State[] nextLine = new State[currentLine.length - 1];
//							while (nextLine.length > 0) {
//								for (int i = 0 ; i < nextLine.length ; i++){
//									//sum += getValueToSum(currentLine[i]);
//									//nextLine[i] = State.ope(currentLine[i],currentLine[i+1] ) ;
//								}
//								//sum +=getValueToSum(currentLine[currentLine.length-1]);
//								currentLine = nextLine;
//								nextLine = new State[currentLine.length - 1];
//							}
//							//return sum;
//						}
//	        }
        }
        StringBuilder stringBuilder = new StringBuilder();
        System.out.println("Result:");
        for (int output = 0; output <=size_all-size_of_window; output++){
            stringBuilder.append(allOutPut[output]);
            stringBuilder.append("\n");
        }
        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
	}
    
	private static int getSumFromSequencesInWindow(List<StateSequence> sequenceInWindow) {
		int sum = 0;
		for (StateSequence sequence : sequenceInWindow) {
			sum += sequence.getSum();
		}
		return sum;
	}

	public static class  LastElementAtLeftException extends Exception {
		private static final long serialVersionUID = 1L;
	}
	
	public  static class NewSequenceAtRightException extends Exception {
		private static final long serialVersionUID = 1L;
	}
	
	public static class StateSequence {
		private int fromPointer;
		private int toPointer;
		private State sequenceState;
		private State lastRightState;
		private List<Integer> zeroMembers = new ArrayList<Integer>();
		
		public StateSequence(int startIndex, State indexState) {
			this.fromPointer = startIndex;
			this.toPointer = startIndex;
			initializeSequenceState(indexState);
			this.lastRightState = indexState;
			if (indexState == State.EQUALS) {
				this.zeroMembers.add(1);
			}
		}

		private void initializeSequenceState(State indexState) {
			this.sequenceState =  indexState == State.EQUALS ? null :indexState;
		}
		
		public int getSum() {
			int numberOfMonotone = toPointer-fromPointer;
			int sum = numberOfMonotone == 0 ? 1 :((numberOfMonotone + 2) * (numberOfMonotone + 1) / 2 - getZeroSum());
			if (sequenceState == State.PLUS) {
				return sum; 
			} else if (sequenceState == State.MOINS) {
				return -sum; 
			} else {
				return 0;
			}
		}
		
		private int getZeroSum() {
			int sum = 0;
			for (Integer zeroNumber : zeroMembers){
				sum += zeroNumber * (zeroNumber + 1)/ 2;
			}
			return sum;
		}
		
		
		
		public void removeOneElementFromLeftSide(State indexState) throws LastElementAtLeftException {
			if (fromPointer == toPointer) {
				throw new LastElementAtLeftException();
			} else {
				fromPointer++;
				if (indexState == State.EQUALS) {
					Integer value = zeroMembers.get(0);
					if (value.intValue() == 1) {
						zeroMembers.remove(0);
					} else {
						zeroMembers.set(0, value-1);
					}
				}
			}
		}
		
		public void putOneElementToRightSide(State indexState) throws NewSequenceAtRightException {
			if (sequenceState == null) {
				initializeSequenceState(indexState);
			}
			if (indexState != sequenceState && indexState != State.EQUALS && sequenceState != null) {
				throw new NewSequenceAtRightException();
			} else {
				toPointer++;
				if (indexState == State.EQUALS ) {
					if (lastRightState == State.EQUALS) { // last & this are all EQUALS
						zeroMembers.set(zeroMembers.size()-1, zeroMembers.get(zeroMembers.size()-1) + 1);
					} else {
						zeroMembers.add(1);
					}
				}
			}
		}
		
		@Override
		public String toString() {
			return Integer.toString(getSum());
		}
		
	}
	
}