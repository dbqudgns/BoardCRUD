package Board_example.Board_crud.DTO;

import Board_example.Board_crud.Entity.Post;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseDTO {

    private String title;
    private String content;

    public PostResponseDTO(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
     }
}
