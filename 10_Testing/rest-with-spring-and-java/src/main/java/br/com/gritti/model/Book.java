package br.com.gritti.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "books")
public class Book implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column
  private String author;

  @Column
  private Date launch_date;

  @Column
  private double price;

  @Column
  private String title;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public Date getLaunch_date() {
    return launch_date;
  }

  public void setLaunch_date(Date launch_date) {
    this.launch_date = launch_date;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Book book = (Book) o;
    return Double.compare(price, book.price) == 0 && Objects.equals(id, book.id) && Objects.equals(author, book.author) && Objects.equals(launch_date, book.launch_date) && Objects.equals(title, book.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, author, launch_date, price, title);
  }
}
