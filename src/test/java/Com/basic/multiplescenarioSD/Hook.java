package Com.basic.multiplescenarioSD;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import Base.BaseUtil;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Hook extends BaseUtil{

	private BaseUtil base;
	
	public Hook(BaseUtil base) {
		this.base = base;
	}
	
	public Properties prop ;
	@Before
	public void InitializeTest() throws IOException{
		/***********************************************
		 * WebDriverManager.chromedriver().setup(); 
		 * base.Driver = new ChromeDriver();
		 * base.Driver.manage().window().maximize();
		 ***********************************************/
		//Reading data_properties file from Data Folder
		prop =new Properties();         
		FileInputStream fis = new FileInputStream("../CucumberAuto/Configurations/Config.properties");
		prop.load(fis);
		String browserName = prop.getProperty("browser"); //CHROME

		if(browserName.equalsIgnoreCase("CHROME")) {
			WebDriverManager.chromedriver().setup();
			base.driver = new ChromeDriver();

		}else if(browserName.equalsIgnoreCase("FIREFOX")) {
			WebDriverManager.firefoxdriver().setup();
			base.driver = new FirefoxDriver();

		}else if(browserName.equalsIgnoreCase("IE")) {
			WebDriverManager.iedriver().setup();
			base.driver = new InternetExplorerDriver();
			
		}else if(browserName.equalsIgnoreCase("EDGE")) {
			WebDriverManager.edgedriver().setup();
			base.driver = new EdgeDriver();
		}
		base.driver.manage().window().maximize();
		base.driver.manage().timeouts().implicitlyWait(9, TimeUnit.SECONDS);
		base.driver.get("https://www.facebook.com/signup");
	}
	
	@After
	public void teardown(Scenario scenario) throws IOException {
		if (scenario.isFailed()) {
			File src = ((TakesScreenshot) base.driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(src, new File("../CucumberAuto/Screenshots/" + "Failed_" + scenario.getName() + ".png"));		
		}
		base.driver.quit();
	}
}