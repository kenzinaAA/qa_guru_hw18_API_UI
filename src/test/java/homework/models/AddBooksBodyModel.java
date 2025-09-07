package homework.models;

import lombok.Data;

import java.util.List;

@Data
public class AddBooksBodyModel {
    String userId;
    List<CollectionOfIsbnsModel> collectionOfIsbns;
}