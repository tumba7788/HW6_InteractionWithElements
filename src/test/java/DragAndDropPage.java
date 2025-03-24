import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class DragAndDropPage {
    WebDriver driver;
    static final String DRAG_AND_DROP = "https://bonigarcia.dev/selenium-webdriver-java/drag-and-drop.html";
    Actions actions;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.get(DRAG_AND_DROP);
        actions = new Actions(driver);
    }

    @AfterEach
    void tearDown(){
        driver.quit();
    }

    @Test
    @Tag("DragAndDrop")
    void dragAndDropTest() throws InterruptedException {
        WebElement source = driver.findElement(By.xpath("//div[@id = 'draggable']"));
        WebElement target = driver.findElement(By.xpath("//div[@id = 'target']"));

        actions.dragAndDrop(source, target).perform();
        Thread.sleep(2000);
        Assertions.assertEquals(source.getLocation(), target.getLocation(), "Элемент не перемещен в окно");
    }

}
