package Board_example.Board_crud.DTO;

import Board_example.Board_crud.Entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRegisterDTO {

    private String title;
    private String content;

    public Post toEntity() {
        return new Post(title, content);
    }
}
