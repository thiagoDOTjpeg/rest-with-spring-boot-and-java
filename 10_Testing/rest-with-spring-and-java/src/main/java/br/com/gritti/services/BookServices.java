package br.com.gritti.services;

import br.com.gritti.controllers.BookController;
import br.com.gritti.data.vo.v1.BookVO;
import br.com.gritti.exceptions.RequiredObjectsIsNullException;
import br.com.gritti.exceptions.ResourceNotFoundException;
import br.com.gritti.mapper.DozerMapper;
import br.com.gritti.model.Book;
import br.com.gritti.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.logging.Logger;

@Service
public class BookServices {
  private Logger logger = Logger.getLogger(this.getClass().getName());

  private BookRepository repository;

  private final PagedResourcesAssembler<BookVO> assembler;

  @Autowired
  public BookServices(BookRepository bookRepository, PagedResourcesAssembler assembler) {
    this.repository = bookRepository;
    this.assembler = assembler;
  }

  public PagedModel<EntityModel<BookVO>> findAll(Pageable pageable)  {
    logger.info("Finding all books");

    var bookPage = repository.findAll(pageable);

    var bookVoPage = bookPage.map((b -> DozerMapper.parseObject(b, BookVO.class)));
    bookVoPage.map(p -> {
      try {
        return p.add(linkTo(methodOn(BookController.class).getBookById(p.getKey())).withSelfRel());
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });

    Link link = linkTo(methodOn(BookController.class).getAllBooks(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
    return assembler.toModel(bookVoPage, link);
  }

  public BookVO findById(Long id) {
    logger.info("Finding a book by id: " + id);
    var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found"));

    BookVO vo = DozerMapper.parseObject(entity, BookVO.class);
    vo.add(linkTo(methodOn(BookController.class).getBookById(id)).withSelfRel());
    return vo;
  }

  public BookVO create(BookVO book) {
    if(book == null) {
      throw new RequiredObjectsIsNullException("It is not allowed to persist a null object!");
    }
    logger.info("Saving a book...");
    var entity = DozerMapper.parseObject(book, Book.class);
    var savedEntity = repository.save(entity);
    var vo = DozerMapper.parseObject(savedEntity, BookVO.class);

    vo.add(linkTo(methodOn(BookController.class).getBookById(vo.getKey())).withSelfRel());

    return vo;
  }

  public BookVO update(BookVO book) {
    if(book == null) {
      throw new RequiredObjectsIsNullException("It is not allowed to persist a null object!");
    }
    logger.info("Updating a book...");
    var entity = repository.findById(book.getKey()).orElseThrow(() -> new ResourceNotFoundException("Book not found"));

    entity.setTitle(book.getTitle());
    entity.setAuthor(book.getAuthor());
    entity.setLaunch_date(book.getLaunch_date());
    entity.setPrice(book.getPrice());

    var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
    vo.add(linkTo(methodOn(BookController.class).getBookById(vo.getKey())).withSelfRel());
    return vo;
  }

  public void delete(Long id) {
    logger.info("Deleting a book...");
    var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found"));

    repository.delete(entity);
  }
}
