package upvote;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.Assert;
import org.junit.Test;

public class SolutionTest {

	@Test
	public void testCalculateSolutionTestCase00() throws FileNotFoundException {
		testTemplate("00");
	}
	
	@Test
	public void testCalculateSolutionTestCase01() throws FileNotFoundException {
		testTemplate("01");
	}
	@Test
	public void testCalculateSolutionTestCase02() throws FileNotFoundException {
		testTemplate("02");
	}
	@Test
	public void testCalculateSolutionTestCase03() throws FileNotFoundException {
		testTemplate("03");
	}
	@Test
	public void testCalculateSolutionTestCase04() throws FileNotFoundException {
		testTemplate("04");
	}
	@Test
	public void testCalculateSolutionTestCase05() throws FileNotFoundException {
		testTemplate("05");
	}
	@Test
	public void testCalculateSolutionTestCase06() throws FileNotFoundException {
		testTemplate("06");
	}
	@Test
	public void testCalculateSolutionTestCase07() throws FileNotFoundException {
		testTemplate("07");
	}
	@Test
	public void testCalculateSolutionTestCase08() throws FileNotFoundException {
		testTemplate("08");
	}
	@Test
	public void testCalculateSolutionTestCase09() throws FileNotFoundException {
		testTemplate("09");
	}
	@Test
	public void testCalculateSolutionTestCase10() throws FileNotFoundException {
		testTemplate("10");
	}
	
	@Test
	public void testCalculateSolutionTestCase11() throws FileNotFoundException {
		testTemplate("11");
	}

	@Test
	public void testCalculateSolutionTestCase17() throws FileNotFoundException {
		testTemplate("17");
	}
	@Test
	public void testCalculateSolutionTestCase18() throws FileNotFoundException {
		testTemplate("18");
	}
	@Test
	public void testCalculateSolutionTestCase20() throws FileNotFoundException {
		testTemplate("20");
	}
	
	
	private void testTemplate(String index) throws FileNotFoundException {
		File testCase11_res = new File("output" + index + ".txt");
		Scanner sc_res = new Scanner(testCase11_res); 
		
		StringBuilder stringBuilder = new StringBuilder();
		while(sc_res.hasNext()){
			stringBuilder.append(sc_res.nextLine());
			stringBuilder.append("\n");
		}
		sc_res.close();
		
		File testCase11 = new File("input" + index + ".txt");
		Scanner sc = new Scanner(testCase11);
		
		Assert.assertEquals(stringBuilder.toString(), Solution.calculateAndPrintResults(sc));
	}
}
