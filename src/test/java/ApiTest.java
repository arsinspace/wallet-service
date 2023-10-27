import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

/**
 * Servlets tests
 */
@Testcontainers
public class ApiTest {

    MountableFile warFile = MountableFile.forHostPath(Paths.get("target/WalletService-1.0-SNAPSHOT.war")
            .toAbsolutePath(), 0777);

    @Container
    GenericContainer payaraContainer = new GenericContainer("payara/micro:6.2023.10-jdk17")
            .withExposedPorts(8080)
            .withCopyFileToContainer(warFile, "/opt/payara/deployments/WalletService-1.0-SNAPSHOT.war")
            .waitingFor(Wait.forLogMessage(".* Payara Micro .* ready in .*\\s", 1))
            .withCommand("--deploy /opt/payara/deployments/WalletService-1.0-SNAPSHOT.war --contextRoot /");


    @Test
    public void checkContainerIsRunning() {
        Assertions.assertThat(payaraContainer.isRunning()).isTrue();
    }

    @Test
    public void testSignupServlet() {
        String testJson = "{\"name\":\"Adam\",\"lastName\":\"Adam\",\"age\":\"29\",\"credentials\":{\"login\":\"adam\",\"password\":\"123\"}}";
        given()
                .port(8080) // port number
                .contentType("application/json")
                .body(testJson)
                .when()
                .post("/signup")
                .then()
                .assertThat().toString().contains("Success signup");
    }

    @Test
    public void testLoginServlet(){
        String testJson = "{\"login\":\"adam\",\"password\":\"123\"}";
        given()
                .port(8080) // port number
                .contentType("application/json")
                .body(testJson)
                .when()
                .post("/login")
                .then()
                .assertThat().toString().contains("Success login");
    }

    @Test
    public void testTransactionServlet(){
        String testJson = "{\"transactionalId\":\"yourId777\",\"purpose\":\"example\",\"amount\":123}";
        given()
                .port(8080) // port number
                .contentType("application/json")
                .body(testJson)
                .when()
                .post("/transaction/credit")
                .then()
                .assertThat().toString().contains("Transaction created");
    }
    @Test
    public void testWalletServlet(){
        RestAssured.get("/wallet").then().assertThat().statusCode(302);
    }
    public String getUrl(String url) {
        return String.format("http://%s:%d/%s", payaraContainer.getHost(),
                payaraContainer.getMappedPort(8080), url);
    }
}

