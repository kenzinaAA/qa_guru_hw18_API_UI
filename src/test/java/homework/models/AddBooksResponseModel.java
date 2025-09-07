package homework.models;

import lombok.Data;

import java.util.List;

@Data
public class AddBooksResponseModel {
    List<Book> books;
}