package br.com.gritti.services;

import br.com.gritti.controllers.BookController;
import br.com.gritti.data.vo.v1.BookVO;
import br.com.gritti.exceptions.RequiredObjectsIsNullException;
import br.com.gritti.exceptions.ResourceNotFoundException;
import br.com.gritti.mapper.DozerMapper;
import br.com.gritti.model.Book;
import br.com.gritti.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.logging.Logger;

@Service
public class BookServices {
  private Logger logger = Logger.getLogger(this.getClass().getName());

  private BookRepository repository;

  @Autowired
  public BookServices(BookRepository bookRepository) {
    this.repository = bookRepository;
  }

  public List<BookVO> findAll()  {
    logger.info("Finding all books");

    var books = DozerMapper.parseListObjects(repository.findAll(), BookVO.class);
    books.forEach(p -> {
        try{
          p.add(linkTo(methodOn(BookController.class).getBookById(p.getKey())).withSelfRel());
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    );
            return books;
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
