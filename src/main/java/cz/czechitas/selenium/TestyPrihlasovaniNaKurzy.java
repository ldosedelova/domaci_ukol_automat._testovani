package cz.czechitas.selenium;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestyPrihlasovaniNaKurzy {

    WebDriver prohlizec;

    @BeforeEach
    public void setUp() {
//      System.setProperty("webdriver.gecko.driver", System.getProperty("user.home") + "/Java-Training/Selenium/geckodriver");
        System.setProperty("webdriver.gecko.driver", "C:\\Java-Training\\Selenium\\geckodriver.exe");
        prohlizec = new FirefoxDriver();
        prohlizec.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    }

    @Test
    public void zaregistrovanyUzivatelSeMusiPrihlasit() {
        prohlizec.navigate().to("https://cz-test-jedna.herokuapp.com/");
        WebElement tlacitkoPrihlaseni = prohlizec.findElement(By.xpath("//a[@href = 'https://cz-test-jedna.herokuapp.com/prihlaseni']" ));
        tlacitkoPrihlaseni.click();

        assertujSpravnePrihlaseniUzivatele("Josef Novak","PepaN@acc.com","12345PepaN");
    }

    private void assertujSpravnePrihlaseniUzivatele(String jmenoUzivatele, String emailUzivatele, String hesloUzivatele) {
        WebElement poleEmail = prohlizec.findElement(By.id("email"));
        poleEmail.sendKeys(emailUzivatele);
        WebElement poleHeslo = prohlizec.findElement(By.id("password"));
        poleHeslo.sendKeys(hesloUzivatele);
        WebElement tlacitkoPrihlasit = prohlizec.findElement(By.xpath("//button[@type = 'submit']"));
        tlacitkoPrihlasit.click();

        WebElement prihlasen = prohlizec.findElement(By.xpath("//span[text()='Přihlášen']"));
        String poPrihlaseni = prihlasen.getText();
        Assertions.assertEquals("Přihlášen", poPrihlaseni);

        WebElement prihlasenSpravnyUzivatel = prohlizec.findElement(By.xpath("//a[@class='dropdown-toggle']"));
        String spravnyUzivatel = prihlasenSpravnyUzivatel.getText();
        Assertions.assertEquals(jmenoUzivatele, spravnyUzivatel);
    }

    @Test
    public void zaregistrovanyUzivatelSeMusiPrihlasitAzPoVyberuKurzu () {
        prohlizec.navigate().to("https://cz-test-jedna.herokuapp.com/");

        vyberKurzu(2,0);

        assertujSpravnePrihlaseniUzivatele("Josef Novak","PepaN@acc.com","12345PepaN");

        assertujVytvoreniPrihlasky("Ferda", "Novak", "12.3.2010");


    }

    private void vyberKurzu(int poradiTlacitkaViceInformaci, int poradiTlacitkaVytvoritPrihlasku) {
        List<WebElement> seznamTlacitekViceInformaciVsechKurzu = prohlizec.findElements(By.xpath("//div[@class = 'card']//a[text() = 'Více informací']"));
        WebElement tlacitkoViceInformaci = seznamTlacitekViceInformaciVsechKurzu.get(poradiTlacitkaViceInformaci);
        tlacitkoViceInformaci.click();

        List<WebElement> seznamTlacitekVytvoritPrihlasku = prohlizec.findElements(By.xpath("//div[@class = 'card']//a[text() = 'Vytvořit přihlášku']"));
        WebElement tlacitkoVytvoritPrihlasku = seznamTlacitekVytvoritPrihlasku.get(poradiTlacitkaVytvoritPrihlasku);
        tlacitkoVytvoritPrihlasku.click();
    }

    private void assertujVytvoreniPrihlasky(String jmenoDitete, String prijmeniDitete, String datumNarozeniDitete) {
        WebElement termin = prohlizec.findElement(By.xpath("//button[@data-id='term_id']"));
        termin.click();
        WebElement vybratTermin = prohlizec.findElement(By.id("bs-select-1-0"));
        vybratTermin.click();


        WebElement jmenoZaka = prohlizec.findElement(By.id("forename"));
        jmenoZaka.sendKeys(jmenoDitete);
        WebElement prijmeniZaka = prohlizec.findElement(By.id("surname"));
        prijmeniZaka.sendKeys(prijmeniDitete);
        WebElement datumNarozeniZaky = prohlizec.findElement(By.id("birthday"));
        datumNarozeniZaky.sendKeys(datumNarozeniDitete);


        WebElement oznaceniPlatby = prohlizec.findElement(By.xpath("//label[@for= 'payment_transfer']"));
        oznaceniPlatby.click();


        WebElement oznaceniPodminek = prohlizec.findElement(By.xpath("//label[@for= 'terms_conditions']"));
        oznaceniPodminek.click();



        WebElement tlacitkoVytvoritPrihlasku = prohlizec.findElement(By.xpath("//input[@type='submit']"));
        tlacitkoVytvoritPrihlasku.click();


        WebElement vytvorenaPrihlaska = prohlizec.findElement(By.xpath("//span[@class='breadcrumb-item active']"));
        String VytvorenaPrihlaskaJmeno =  vytvorenaPrihlaska.getText();
        Assertions.assertEquals(jmenoDitete + " " + prijmeniDitete, VytvorenaPrihlaskaJmeno);

    }


    @Test
    public void zaregistrovanyUzivatelSeMusiPrihlasitAPoteVybratKurz (){
        prohlizec.navigate().to("https://cz-test-jedna.herokuapp.com/");
        WebElement tlacitkoPrihlaseni = prohlizec.findElement(By.xpath("//a[@href = 'https://cz-test-jedna.herokuapp.com/prihlaseni']" ));
        tlacitkoPrihlaseni.click();

        assertujSpravnePrihlaseniUzivatele("Josef Novak","PepaN@acc.com","12345PepaN");

        WebElement tlacitkologoDomu= prohlizec.findElement(By.xpath("//a/img[@alt ='Domů']" ));
        tlacitkologoDomu.click();

        vyberKurzu(0, 1);

        assertujVytvoreniPrihlasky("Maruška", "Novakova", "27.4.2012");
    }

    @Test
    public void prihlasenyUzivatelMusiVytvoritPrihlaskuDiteteNaStrancePrehleduPrihlasek(){
        prohlizec.navigate().to("https://cz-test-jedna.herokuapp.com/");
        WebElement tlacitkoPrihlaseni = prohlizec.findElement(By.xpath("//a[@href = 'https://cz-test-jedna.herokuapp.com/prihlaseni']" ));
        tlacitkoPrihlaseni.click();
        assertujSpravnePrihlaseniUzivatele("Josef Novak","PepaN@acc.com","12345PepaN");


        WebElement tlacitkoVytvoritNovouPrihlasku = prohlizec.findElement(By.xpath("//a[@class ='btn btn-sm btn-info']"));
        tlacitkoVytvoritNovouPrihlasku.click();

        vyberKurzu(1, 0);

        assertujVytvoreniPrihlasky("Antonín", "Novak", "4.8.2011");

    }

    @AfterEach
    public void tearDown() {
        prohlizec.close();
    }
}
