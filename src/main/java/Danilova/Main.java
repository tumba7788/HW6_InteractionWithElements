package Danilova;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {
    public static final String WEB_FORM = "https://bonigarcia.dev/selenium-webdriver-java/web-form.html";

    public static void main(String[] args){
        dropDownSelectOptions();
        dataListOptions();
        String folderPath = "src/test/resources/fileTest";  // адрес папки
        String csvPath = "src/test/resources/file_list.csv"; // Путь для сохранения CSV
        saveFileListToCSV(folderPath, csvPath);
    }

    //Метод генерирует список options в select DropDown select
    static void dropDownSelectOptions() {
        WebDriver driver = new ChromeDriver();
        driver.get(WEB_FORM);
        driver.manage().window().maximize();

        List<WebElement> options = driver.findElements(By.xpath("//select/option"));
        try (FileWriter writer = new FileWriter("dropDownSelectOptions.txt", false)) {
            writer.append("nameOfDropDownSelectOptions\n");  // Заголовки CSV
            for (WebElement element : options) {
                String optionName = element.getText().trim();
                writer.write(optionName + "\n");
            }
        } catch (Exception e) {
            System.err.println("Error during writing fields: " + e.getMessage());

        }
        driver.quit();
    }

    //Метод генерирует список dataList
    static void dataListOptions() {
        WebDriver driver = new ChromeDriver();
        driver.get(WEB_FORM);

        List<WebElement> dataListOptions = driver.findElements(By.xpath("//datalist/option"));
        try (FileWriter writer = new FileWriter("dataListOptions.txt", false)) {
            writer.append("nameOptions\n");  // Заголовки CSV
            for (WebElement option : dataListOptions) {
                String optionName = option.getAttribute("value").trim();
                writer.write(optionName + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        driver.quit();
    }


    static void saveFileListToCSV (String directoryPath, String csvFilePath){
            File folder = new File(directoryPath);

            if (!folder.exists() || !folder.isDirectory()) {
                System.out.println("folder doesn't exist");
                return;
            }

            File[] files = folder.listFiles();
            if (files == null || files.length == 0) {
                System.out.println("folder is empty.");
                return;
            }

            try (FileWriter writer = new FileWriter(csvFilePath)) {
                writer.append("Filename\n");  // Заголовки CSV

                for (File file : files) {
                    if (file.isFile()) {
                        String fileName = file.getName();
                        writer.append(fileName).append("\n");
                    }
                }

                System.out.println("CSV-file was created " + csvFilePath);
            } catch (IOException e) {
                System.out.println("Error " + e.getMessage());
            }
        }

}