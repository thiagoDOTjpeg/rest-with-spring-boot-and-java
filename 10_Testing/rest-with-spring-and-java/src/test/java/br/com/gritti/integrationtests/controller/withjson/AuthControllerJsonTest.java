package br.com.gritti.integrationtests.controller.withjson;

import br.com.gritti.configs.TestConfigs;
import br.com.gritti.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.gritti.integrationtests.vo.AccountCredentialsVO;
import br.com.gritti.integrationtests.vo.TokenVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerJsonTest extends AbstractIntegrationTest {

  private static TokenVO tokenVO;

  @Test
  @Order(1)
  public void testSignin() throws JsonMappingException, JsonProcessingException {
    AccountCredentialsVO user = new AccountCredentialsVO("thiago", "root123");

    tokenVO =
            given()
                    .basePath("/auth/signin")
                    .port(TestConfigs.SERVER_PORT)
                    .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .body(user)
                    .when()
                    .post()
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .as(TokenVO.class);

    assertNotNull(tokenVO.getAccessToken());
    assertNotNull(tokenVO.getRefreshToken());
  }

  @Test
  @Order(2)
  public void testRefresh() throws JsonMappingException, JsonProcessingException {
    AccountCredentialsVO user = new AccountCredentialsVO("thiago", "root123");

    var newTokenVO =
            given()
                    .basePath("/auth/refresh")
                    .port(TestConfigs.SERVER_PORT)
                    .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .pathParam("username", tokenVO.getUserName())
                    .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
                    .when()
                    .put("{username}")
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .as(TokenVO.class);

    assertNotNull(newTokenVO.getAccessToken());
    assertNotNull(newTokenVO.getRefreshToken());
  }
}
