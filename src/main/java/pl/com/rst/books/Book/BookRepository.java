package pl.com.rst.books.Book;


import java.util.Optional;

public class BookRepository {
    public Optional<Book> getBook(long id) {
        return Optional.of(new Book());
    }
}
