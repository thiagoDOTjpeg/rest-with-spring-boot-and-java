package br.com.gritti.controllers;

import br.com.gritti.data.vo.v1.BookVO;
import br.com.gritti.services.BookServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
@Tag(name = "Book", description = "Endpoints for Managing Books")
public class BookController {
  private final BookServices bookServices;

  @Autowired
  public BookController(BookServices bookServices) {
    this.bookServices = bookServices;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Finds all Books", description = "Finds all Books", tags = {"Book"},
  responses = {
          @ApiResponse(description = "Success", responseCode = "200", content = {
                  @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BookVO.class))),
          }),
          @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
          @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
          @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
          @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
  })
  public List<BookVO> getAllBooks() {
    return bookServices.findAll();
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Find a Book", description = "Find a Book", tags = {"Book"},
          responses = {
                  @ApiResponse(description = "Success", responseCode = "200", content = {
                          @Content(schema = @Schema(implementation = BookVO.class)),
                  }),
                  @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                  @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                  @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                  @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
          })
  public BookVO getBookById(@PathVariable(value = "id") Long id) {
    return bookServices.findById(id);
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Save a Book", description = "Save a Book", tags = {"Book"},
          responses = {
                  @ApiResponse(description = "Success", responseCode = "200", content = {
                          @Content(schema = @Schema(implementation = BookVO.class)),
                  }),
                  @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                  @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                  @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
          })
  public BookVO createBook(BookVO book) {
    return bookServices.create(book);
  }

  @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Delete a Book", description = "Delete a Book", tags = {"Book"},
          responses = {
                  @ApiResponse(description = "No Content", responseCode = "204", content = {
                          @Content(schema = @Schema(implementation = BookVO.class)),
                  }),
                  @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                  @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                  @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                  @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
          })
  public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
    bookServices.delete(id);
    return ResponseEntity.noContent().build();
  }
}
