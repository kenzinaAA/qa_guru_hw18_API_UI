package homework.models;

import lombok.Data;

import java.util.List;

@Data
public class ListOfBooksResponseModel {
    String userId, username;
    List<Book> books;
}
