package br.com.gritti.integrationtests.controller.withjson;

import static io.restassured.RestAssured.given;

import br.com.gritti.configs.TestConfigs;
import br.com.gritti.integrationtests.testcontainers.AbstractIntegrationTest;
import static org.junit.jupiter.api.Assertions.*;

import br.com.gritti.integrationtests.vo.PersonVO;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationTest {

  private static RequestSpecification specification;
  private static ObjectMapper objectMapper;

  private static PersonVO person;

  @BeforeAll
  public static void setup(){
    objectMapper = new ObjectMapper();
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    person = new PersonVO();
  }

  @Test
  @Order(1)
  public void testCreate() throws Exception{
    mockPerson();

    specification = new RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, "https://gritti.com.br")
            .setBasePath("/api/person/v1")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(new RequestLoggingFilter(LogDetail.ALL))
            .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();

    var content =
            given().spec(specification)
                    .contentType(TestConfigs.CONTENT_TYPE_JSON)
                      .body(person)
                    .when()
                    .post()
                    .then()
                      .statusCode(200)
                    .extract()
                      .body()
                        .asString();
    PersonVO createdPerson = objectMapper.readValue(content, PersonVO.class);
    person = createdPerson;

    assertNotNull(createdPerson);
    assertNotNull(createdPerson.getId());
    assertNotNull(createdPerson.getFirstName());
    assertNotNull(createdPerson.getLastName());
    assertNotNull(createdPerson.getAddress());
    assertNotNull(createdPerson.getGender());

    assertTrue(createdPerson.getId() > 0);

    assertEquals("John", createdPerson.getFirstName());
    assertEquals("Doe", createdPerson.getLastName());
    assertEquals("New York City - New York",createdPerson.getAddress());
    assertEquals("Male", createdPerson.getGender());
  }

  private void mockPerson() {
    person.setFirstName("John");
    person.setLastName("Doe");
    person.setAddress("New York City - New York");
    person.setGender("Male");
  }
}
