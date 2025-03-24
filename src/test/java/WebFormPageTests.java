import jdk.jfr.Description;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


public class WebFormPageTests {
    WebDriver driver;
    Actions actions;
    private static final String HOME_PAGE_URL = "https://bonigarcia.dev";
    private static final String WEB_FORM_PAGE_URL = "https://bonigarcia.dev/selenium-webdriver-java/web-form.html";
    private static final String SUBMITTED_PAGE = "https://bonigarcia.dev/selenium-webdriver-java/submitted-form.html";
    WebElement submitButton;
    String expectedFormSubmittedText = "Form submitted";
    String actualText;
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));


    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        actions = new Actions(driver);
        driver.get(WEB_FORM_PAGE_URL);
        submitButton = driver.findElement(By.xpath("//button[@type = 'submit']"));
        //  driver.manage().window().maximize();
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    void pressEnterKey() {
        actions.sendKeys(Keys.ENTER).perform();
    }

    void clickSubmitButton() {
        submitButton.click();
    }

    void formSubmittedAssertEquals() {
        assertEquals(expectedFormSubmittedText, actualText, "Uoops!");
    }

    void checkSubmittedPage() throws MalformedURLException {
        //Проверка, что осуществлен переход на новую "submitted" страницу
        // Проверить текст "Form submitted"
        actualText = driver.findElement(By.cssSelector(".display-6")).getText();
        formSubmittedAssertEquals();
        //Проверить ссылку текущей страницы и что она совпадает с submitted
        String currentPage = driver.getCurrentUrl();
        URL url = new URL(currentPage);
        String path = url.getPath(); // /selenium-webdriver-java/submitted-form.html
        assertEquals(SUBMITTED_PAGE, HOME_PAGE_URL + path, "адрес не совпадает");
    }

    // Метод для чтения данных из CSV-файлач, получает названия DropDownSelect параметров
    static List<String> getDropDownSelectData() throws IOException {
        return Files.lines(Paths.get("src/test/resources/DropDownSelect.csv")).collect(Collectors.toList());
    }

    // Метод для чтения данных из CSV-файла, получает названия DataList параметров
    static List<String> getDataList() throws IOException {
        return Files.lines(Paths.get("src/test/resources/DropDownListOptions.csv")).skip(1).collect(Collectors.toList());
    }

    //Метод для чтения данных из CSV-файла, получает список имен файлов с расширением
    static List<String> getFileNames() throws IOException {
        return Files.lines(Paths.get("src/test/resources/file_list.csv")).collect(Collectors.toList());
    }

    @Test
    @Tag("textInputArea")
    void textInputTest() throws InterruptedException, MalformedURLException {
        WebElement textInputArea = driver.findElement(By.xpath("//input[@class='form-control' and @name = 'my-text']"));
        String inputText = "textInputArea";

        // 1 Проверяем ввод текста
        textInputArea.sendKeys(inputText);
        Thread.sleep(2000);
        //assert
        Assertions.assertEquals(inputText, textInputArea.getAttribute("value"), "Введенное и ожидаемое значения не совпадают");
        Thread.sleep(2000);

        //2 Проверяем отправку формы через enter
        pressEnterKey();
        Thread.sleep(2000);

        // 3 Проверяем submitted page
        checkSubmittedPage();

        // 4 Переход назад и очистка ввода
        driver.navigate().back();
        Thread.sleep(2000);
        //Очистить поле ввода
        textInputArea.clear();
        Thread.sleep(2000);

        //5 Проверяем отправку пустой формы по кнопке Submit
        clickSubmitButton();

        Thread.sleep(2000);
        formSubmittedAssertEquals();
    }

    @Test
    @Tag("passwordInputArea")
    void passwordInputTest() throws InterruptedException, MalformedURLException {
        WebElement passwordInputArea = driver.findElement(By.xpath("//input[@type = 'password']"));
        // 1 ввод пароля
        String inputText = "passssssssssswoooooord1235";
        passwordInputArea.sendKeys(inputText);
        Thread.sleep(2000);

        //assert
        Assertions.assertEquals(inputText, passwordInputArea.getAttribute("value"), "Введенное и ожидаемое значения не совпадают");
        Thread.sleep(2000);

        //2 отправка формы через Enter
        pressEnterKey();
        Thread.sleep(2000);

        // 3 Проверяем submitted page
        checkSubmittedPage();

        // 4 Переход назад и очистка ввода
        driver.navigate().back();
        Thread.sleep(2000);
        passwordInputArea.clear();
        Thread.sleep(2000);

        //5 Отправка пустой формы по кнопке Submit
        clickSubmitButton();
        Thread.sleep(2000);
        formSubmittedAssertEquals();
    }

    @Test
    @Tag("smoke")
    @Tag("TextArea")
    @Description("Проверяем отправку текста")
    void TextAreaTest() throws InterruptedException, MalformedURLException {
        WebElement textArea = driver.findElement(By.xpath("//textarea[@name = 'my-textarea']"));
        String text = textArea.getText();
        System.out.println(text);
        Thread.sleep(2000);
        //1 Вводим текст
        textArea.sendKeys("Text Area 123 ");
        Thread.sleep(2000);
        //2 Отправляем форму по кнопке Submit
        clickSubmitButton();

        Thread.sleep(2000);
        // 3 Проверяем submitted page
        checkSubmittedPage();

        // 4 Переход назад и очистка ввода
        driver.navigate().back();
        Thread.sleep(2000);
        textArea.clear();
        Thread.sleep(2000);

        //5 Отправка пустой формы по кнопке Submit
        clickSubmitButton();
        Thread.sleep(2000);
        formSubmittedAssertEquals();

    }

    @Test
    @Tag("TextArea")
    @Description("Передаем очень большой текст")
    void TextAreaTooBigTextTest() throws InterruptedException, IOException {
        String text = Files.readString(Paths.get("textTextAreaTest.txt"));
        WebElement textInputArea = driver.findElement(By.tagName("textarea"));

        //1 Отправка очень большого текста из файла
        textInputArea.sendKeys(text);
        Thread.sleep(2000);

        //2 Отправка формы по кнопке Submit
        clickSubmitButton();

        Thread.sleep(2000);

        //3 Переход на страницу Submitted, обработка исключения
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".display-6")));
            actualText = driver.findElement(By.cssSelector(".display-6")).getText();
        } catch (Exception e) {
            // Обрабатываем другие исключения
            Assertions.fail("Произошла ошибка при выполнении теста: " + e.getMessage());
        }
    }

    @Test
    @Tag("DisabledInput")
    @Description("Попытка ввода в заблокированное для ввода поле")
    void disabledAreaTest() {
        WebElement disabledArea = driver.findElement(By.xpath("//input[@name = 'my-disabled']"));
        String color = disabledArea.getCssValue("color");//rgba(33, 37, 41, 1)

//        //Код сделает область доступную для ввода текста
//        ((JavascriptExecutor) driver).executeScript("arguments[0].disabled = false;", disabledArea);
//        disabledArea.sendKeys("bjhbfvjhbjhdbvfh");
//        Thread.sleep(2000);

        assertTrue(disabledArea.isDisplayed());
        assertEquals("rgba(33, 37, 41, 1)", color, "Цвет не совпадает с цветом из документации");
        assertEquals("Disabled input", disabledArea.getAttribute("placeholder"));
        assertThrows(ElementNotInteractableException.class, () -> disabledArea.sendKeys("fnkngk"));
    }

    @Test
    @Tag("ReadonlyInput")
    @Description("Попытка ввода в заблокированное для ввода поле")
    void readonlyAreaTest() {
        WebElement readonlyArea = driver.findElement(By.xpath("//input[@name = 'my-readonly']"));
        String color = readonlyArea.getCssValue("color");//rgba(33, 37, 41, 1)
        System.out.println(color);

        assertTrue(Boolean.parseBoolean(readonlyArea.getAttribute("readonly")));
        assertEquals("rgba(33, 37, 41, 1)", color, "Цвет не совпадает с цветом из документации");
        assertEquals("Readonly input", readonlyArea.getAttribute("value"));

//        // Используем executeScript, чтобы снять атрибут readonly и пробросить введенный текст в URL
//        ((JavascriptExecutor) driver).executeScript("arguments[0].readOnly = false;", readonlyArea);
//        readonlyArea.sendKeys("fnkngk");
//       Thread.sleep(2000);
//       clickSubmitButton();
//      String url =  driver.getCurrentUrl();
//        System.out.println(url);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "DropDownSelect.csv", numLinesToSkip = 0)
    @Tag("DropDownSelect")
    @Description("")
    void dropDownSelect1Test() throws IOException {
        WebElement selectElement = driver.findElement(By.xpath("//select[@name = 'my-select']"));
        Select select = new Select(selectElement);
        List<WebElement> options = select.getOptions();
        //Проверяем список на соответствие количеству элементов
        assertEquals(4, options.size(), "Список состоит из 4 значений");

        // Загружаем данные из CSV файла в List
        List<String> expectedOptionNamesFromFile = getDropDownSelectData();

        List<WebElement> selectElements = driver.findElements(By.xpath("//select/option"));

        //Проверяем список на соответствие названию
        for (int i = 0; i < selectElements.size(); i++) {
            String actualOptionName = selectElements.get(i).getText().split("\n")[0].trim();
            String expectedOptionNameFromFile = expectedOptionNamesFromFile.get(i);
            assertEquals(expectedOptionNameFromFile, actualOptionName, "Название не совпадает");
        }
    }

    @Test
    @Tag("DropDownSelect")
    @Description("Проверка переключения параметров")
    void dropDownSelect2Test() throws InterruptedException {
        WebElement selectElement = driver.findElement(By.xpath("//select[@name = 'my-select']"));
        Select select = new Select(selectElement);

        //Выбираем значения из списка
        select.selectByIndex(0);
        Thread.sleep(2000);
        select.selectByIndex(1);
        Thread.sleep(2000);
        select.selectByIndex(2);
        Thread.sleep(2000);
        select.selectByIndex(3);
        Thread.sleep(2000);

        assertFalse(select.isMultiple(), "Это список не мультиселкт");
    }

    @ParameterizedTest
    @Tag("DataList")
    @Description("Проверка количества и названия параметров")
    @CsvFileSource(resources = "DropDownListOptions.csv", numLinesToSkip = 1)
    void dataList1Test(String nameOptions) throws IOException {
        List<WebElement> dataListOptions = driver.findElements(By.xpath("//datalist/option"));
        List<String> dataListNames = getDataList();

        //Проверяем список на количество параметров
        int actualCount = dataListOptions.size();
        assertEquals(dataListNames.size(), actualCount, "Список должен содержать 5 параметров");
        //Проверяем список на соответствие названию
        for (int i = 0; i < dataListOptions.size(); i++) {
            String actualDataListName = dataListOptions.get(i).getAttribute("value").trim();
            System.out.println("actualDataListName " + actualDataListName);
            String expectedDataListName = dataListNames.get(i);
            System.out.println("actual: " + actualDataListName + ", expected: " + expectedDataListName);
            assertEquals(expectedDataListName, actualDataListName, "Название параметра не совпадает");
        }
    }

    @Test
    @Tag("DataList")
    @Description("Проверка переключения и очистки параметров")
    void dataList2Test() throws InterruptedException, IOException {
        WebElement dataList = driver.findElement(By.name("my-datalist"));
        List<String> listOptionsFromFile = getDataList();
        for (int i = 0; i < listOptionsFromFile.size(); i++) {
            dataList.sendKeys(listOptionsFromFile.get(i).trim());
            Thread.sleep(2000);
            clickSubmitButton();
            checkSubmittedPage();
            Thread.sleep(2000);
            driver.navigate().back();
            Thread.sleep(2000);
            dataList.clear();
        }
        dataList.sendKeys("Свой вариант");
        Thread.sleep(2000);
    }

    @ParameterizedTest
    @Tag("FileUpload")
    @Description("Проверка загрузки файлов с различным расширением")
    @CsvFileSource(resources = "file_list.csv", numLinesToSkip = 1)
    void fileUpload1Test(String fileName) throws InterruptedException, URISyntaxException, IOException {
        WebElement fileElement = driver.findElement(By.xpath("//input[@type = 'file']"));
        //Получить абсолютный путь:
        String folderName = "fileTest\\";
        String relativePath = folderName + fileName;
        String filePath = Paths.get(getClass().getClassLoader().getResource(relativePath).toURI()).toString();
        fileElement.sendKeys(filePath);
        Thread.sleep(2000);
        String[] name = fileElement.getAttribute("value").split("\\\\");
        String actualName = name[name.length - 1];
        assertEquals(fileName, actualName, "Uooops");
        clickSubmitButton();
        checkSubmittedPage();
        Thread.sleep(2000);
        driver.navigate().back();
        fileElement.clear();
        Thread.sleep(2000);
    }

    @Test
    @Tag("CheckBox")
    @Description("Проверка названий чекбокса")
    void checkBoxNamesTest() {
        String checkedCheckBox = driver.findElement((By.xpath("//input[@id='my-check-1']/ancestor::label"))).getText();
        assertEquals("Checked checkbox", checkedCheckBox, "Название не совпадает");
        String defaultCheckBox = driver.findElement((By.xpath("//input[@id='my-check-2']/ancestor::label"))).getText();
        assertEquals("Default checkbox", defaultCheckBox, "Название не совпадает");
    }

    @Test
    @Tag("CheckBox")
    @Description("Проверка работы чекбокса")
    void сheckedCheckBoxTest() throws InterruptedException {
        WebElement checkedCheckBox = driver.findElement(By.xpath("//input[@id = 'my-check-1']"));

        assertTrue(checkedCheckBox.isSelected(), "Чек бокс должен быть выбран по умолчанию");
        Thread.sleep(2000);
        checkedCheckBox.click();
        Thread.sleep(2000);
        assertFalse(checkedCheckBox.isSelected(), "Чек бокс должен быть unChecked");
    }

    @Test
    @Tag("radioButton")
    @Description("Проверка радиобаттн")
    void radioButtonTest() {
        WebElement checkedRadioButton = driver.findElement(By.xpath("//input[@id='my-radio-1']"));
        WebElement defaultRadioButton = driver.findElement(By.xpath("//input[@id='my-radio-2']"));

        assertTrue(checkedRadioButton.isSelected(), "Должно быть по умолчанию выбрано");
        defaultRadioButton.click();
        assertTrue(defaultRadioButton.isSelected(), "Должно быть выбрано");
        assertFalse(checkedRadioButton.isSelected(), "Должен быть UnChecked");
    }

    @Test
    @Tag("colorPicker")
    @Description("Проверка выбора цвета")
    void colorPickerTest() throws InterruptedException {
        WebElement colorPicker = driver.findElement(By.xpath("//input[@name='my-colors']"));
        String expectedColor = driver.findElement(By.xpath("//input[@name='my-colors']")).getAttribute("value");

        Assertions.assertEquals(expectedColor, colorPicker.getDomAttribute("value"), "Цвет по умолчанию не совпадает");
        Thread.sleep(2000);
        Color red = new Color(255, 0, 0, 1);
        String script = String.format("arguments[0].setAttribute('value', '%s');", red.asHex());
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(script, colorPicker);
        Thread.sleep(2000);
        Assertions.assertEquals("#ff0000", colorPicker.getDomProperty("value"), "Неверный цвет выбран");
    }

    @Test
    @Tag("datePicker")
    @Description("Проверка календаря")
    void datePickerTest() throws InterruptedException {
        WebElement datePicker = driver.findElement(By.xpath("//input[@name='my-date']"));
        assertTrue(datePicker.getAttribute("value").isEmpty(), "По умолчанию поле должно быть пустым");


        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        System.out.println(date);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.querySelector('input[name=\"my-date\"]').value ='" + date + "';");
        Thread.sleep(2000);
        String selectedDate = datePicker.getAttribute("value");
        assertEquals(date, selectedDate, "Даты не совпадают");
    }

    @Test
    @Tag("exampleRange")
    @Description("Тестирование ползунка")
    void exampleRangeTest() {
        WebElement rangePicker = driver.findElement(By.xpath("//input[@name='my-range']"));
        String defaultRange = rangePicker.getAttribute("value").trim();
        System.out.println(defaultRange);
        assertEquals("5", defaultRange, "Значение по умолчанию не совпадает");

        rangePicker.sendKeys(Keys.ARROW_LEFT);
        String expectedValue = rangePicker.getAttribute("value").trim();
        assertEquals("4", expectedValue, "Значение должно быть = 4");
    }

}
