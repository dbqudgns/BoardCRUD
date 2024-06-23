package Board_example.Board_crud.Service;

import Board_example.Board_crud.DTO.PostRegisterDTO;
import Board_example.Board_crud.DTO.PostResponseDTO;
import Board_example.Board_crud.Entity.Post;
import Board_example.Board_crud.Repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;


    public List<Post> getAllPost() {
        return postRepository.findAll();
    }


    public PostResponseDTO getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("잘못된 Post ID 입니다."));
        return new PostResponseDTO(post);
    }


    public Post createPost(PostRegisterDTO post) {
        return postRepository.save(post.toEntity());
    }


    public Post updatePost(Long id, PostRegisterDTO updatedPost) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("잘못된 Post ID 입니다."));
        post.update(updatedPost.getTitle(), updatedPost.getContent());

        return postRepository.save(post);
    }


    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

}
