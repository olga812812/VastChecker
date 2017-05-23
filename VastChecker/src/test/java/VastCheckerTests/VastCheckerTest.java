package test.java.VastCheckerTests;

import main.java.VastChecker.VastChecker;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.Assert;

public class VastCheckerTest extends Assert {
	
	VastChecker vc = new VastChecker();
	
	@DataProvider
	public Object[][] Vasts()
	{
		String vast1= "<Tracking event=\"firstQuartile\"><![CDATA[8.339571e-317vent4%]]></Tracking>";
		String vast2 = "<Tracking event=\"firstQuartile\"><![CDATA[https://hel[ello-privet]]></Tracking>";
		String vast3 = "<Tracking event=\"firstQuartile\"><![CDATA[http://hello.ru]]></Tracking>";
		String vast4 = "<Tracking event=\"firstQuartile\"><![CDATA[http://при_all_in_english]]></Tracking>";
		String vast5 = "<Tracking event=\"firstQuartile\"><![CDATA[https://correct?h=56&j=fdrt&p=nggg]]></Tracking>";
		String vast6 = "<Tracking event=\"firstQuartile\"><![CDATA[https://corr ect?h=56&j=fdrt&p=nggg]]></Tracking>";
		String vast7 = "<Tracking event=\"firstQuartile\"><![CDATA[https://correct/path/to/new.html?h=56&j=%record_id%&p=nggg]]></Tracking>";
		
		return new Object[][]
				{
				{vast1, false},
				{vast2, false},
				{vast3, true},
				{vast4, false},
				{vast5, true},
				{vast6, false},
				{vast7, true}
				};
	}

	@Test (description="check different trackers with errors", dataProvider="Vasts")
	public void checkVast_check(String vast, boolean expectedResult)
	{
		assertEquals(vc.checkVast(vast), expectedResult);
	}
	
}
