package org.example;

import io.javalin.Javalin;
import org.example.controller.ClientController;
import org.example.view.pages.CreateClientPage;
import org.example.view.pages.ListPage;
import org.example.view.util.DriverFactory;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClientViewTest {

    private Javalin app;
    private WebDriver driver;
    private ListPage listPage;
    private ClientController clientController;

    private static final int PORT = 7000;
    private static final String BASE_URL = "http://localhost:" + PORT + "/clients";

    @BeforeAll
    public void setupAll() {
        app = Javalin.create(config -> {}).start(PORT);
        clientController = new ClientController(app);

        driver = DriverFactory.createDriver();
        driver.manage().window().maximize();
    }

    @AfterAll
    public void teardownAll() {
        if (driver != null) driver.quit();
        if (app != null) app.stop();
    }

    @BeforeEach
    public void beforeEachTest() {
        driver.get(BASE_URL);
        listPage = new ListPage(driver);
    }

    @Test
    public void listPageNavigationTest() {
        listPage.goToCreateClientPage();
        String expectedUrl = BASE_URL + "/new";
        String actualUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    public void createClientTest() {
        listPage.goToCreateClientPage();

        CreateClientPage createClientPage = new CreateClientPage(driver);
        createClientPage.enterName("Augusto");
        createClientPage.enterEmail("augustocedro@example.com");
        createClientPage.submitCreateClientButton();

        String expectedUrl = BASE_URL;
        String actualUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl, actualUrl);
    }
    @Test
    public void createInvalidClientTest() {
        listPage.goToCreateClientPage();

        CreateClientPage page = new CreateClientPage(driver);

        page.enterName("");
        page.enterEmail("emailErrado");
        page.submitCreateClientButton();


        String errors = page.getErrors();

        Assertions.assertTrue(errors.contains("Nome não pode ser vazio"));
        Assertions.assertTrue(errors.contains("Email inválido"));
    }


}
