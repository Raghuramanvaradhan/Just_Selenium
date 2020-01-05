package test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class Assignment3 {
	private String browser;
	WebDriver driver;
	protected final  String taxid = "FR121212";
	protected final String _username = "kumar.testleaf@gmail.com";
	protected final String _password = "leaf@12";
	protected final String _searchText = "IT Support";

	public WebDriver _browserSetup(String browser) {
		if(browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver","./drivers/chromedriver.exe");
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--start-maximized");
			options.addArguments("--disable-web-security");
			options.addArguments("--no-proxy-server");

			Map<String, Object> prefs = new HashMap<String, Object>();
			prefs.put("credentials_enable_service", false);
			prefs.put("profile.password_manager_enabled", false);
			driver = new ChromeDriver(options);
			return driver;
		}else {
			System.setProperty("webdriver.geckodriver.driver","./drivers/geckodriver.exe");
			FirefoxProfile fp = new FirefoxProfile();
			fp.setPreference("browser.startup.homepage", "about:blank");
			fp.setPreference("startup.homepage_welcome_url", "about:blank");
			driver = new FirefoxDriver();
			return driver;
		}


	}
	@Parameters({"browser"})
	@Test
	public void _assignment3(String browser) throws Throwable {
		this.browser=browser;
		_browserSetup(browser);
		try {
			try {
				driver.get("https://acme-test.uipath.com/account/login");
				driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				WebElement userName = driver.findElement(By.xpath("//input[@id='email']"));
				userName.sendKeys(_username);
				userName.sendKeys(Keys.TAB);
				driver.findElement(By.xpath("//input[@id='password']")).sendKeys(_password); 
				driver.findElement(By.xpath("//button[@id='buttonLogin']")).click();
				String welcome = driver.findElement(By.xpath("//div[@class='main-container']//h1[1]")).getText();

			}catch(Exception e) {
				throw new Exception("Unable to login Check with credentials");
			}
			Actions act = new Actions(driver);
			WebElement invoice = driver.findElement(By.xpath("//div[6]//button[1]"));
			act.moveToElement(invoice).click().perform();
			WebElement SearchForInvoices = driver.findElement(By.linkText("Search for Invoice"));
			act.click(SearchForInvoices).perform();
			driver.findElement(By.xpath("//input[@id='vendorTaxID']")).sendKeys(taxid);
			driver.findElement(By.xpath("//button[@id='buttonSearch']")).click();
			List<WebElement> lstinvceitem = driver.findElements(By.xpath("//tr/td[3]"));
			for(WebElement w:lstinvceitem)
			{
				if(w.getText().contains(_searchText)) {
					String taxID = driver.findElement(By.xpath("//tr/td[2]")).getText();
					System.out.println("The Tax ids are : "+ taxID+ ""+w.getText());
				}

			}
		}finally {
			driver.close();
		}

	}

}
