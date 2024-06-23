# BoardCRUD

## 1. 스프링 부트에서 JPA를 이용하여 MySQL를 연동하기 위한 환경설정

- build.gradle

```groovy
dependencies {

  // Spring Data JPA를 사용하기 위한 의존성
  implementation 'org.springframework.boot:spring-boot-starter-data-jpa'

  //Spring MVC와 RESTful 웹 서비스를 만들기 위한 의존성 
	implementation 'org.springframework.boot:spring-boot-starter-web'

  //Lombok 애노테이션을 처리하기 위한 의존성 
	annotationProcessor 'org.projectlombok:lombok'

  //컴파일 시에만 Lombok을 사용하기 위한 의존성
	compileOnly 'org.projectlombok:lombok'

  //애플리케이션 실행 시 MySQL 데이터베이스 연결하기 위한 MySQL JDBC 드라이버 의존성
	runtimeOnly 'com.mysql:mysql-connector-j'

  //Spring Boot의 테스트 지원을 위한 의존성
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	
}
```

- application.yml 

```yaml
spring:
  datasource:
    # MySQL

    //데이터베이스 드라이버 클래스를 지정한다. 여기서는 MySQL 데이터베이스를 사용하기 위해 아래의 클래스를 사용한다.
    driver-class-name: com.mysql.cj.jdbc.Driver

    //데이터베이스에 연결하기 위한 URL
    //localhost에서 실행 중인 포트번호가 3306인 MySQL 데이터베이스의 new_schema 스키마에 접속한다.
    //serverTimezone=Asia/Seoul : 서버의 시간을 서울로 설정한다. 
    url: "jdbc:mysql://localhost:3306/new_schema?&serverTimezone=Asia/Seoul"

    //데이터베이스에 접속할 때 사용할 사용자 이름
    username: "root"

    //데이터베이스에 접속할 때 사용할 비밀번호 
    password: "1234"

```

```yaml
jpa:
    # JPA

    //Hibernate : DB와 자바 객체 간의 매핑을 자동으로 처리하여, 개발자가 SQL 쿼리를 직접 작성하지 않고도 DB 작업 수행을 도운다.
    //즉, 자바 기반의 객체 관계 매핑(Object-Relational Mapping, ORM) 프레임워크
    hibernate:
      //update는 기존 스키마를 유지하면서 필요한 경우 새로운 테이블이나 Column을 추가한다.
      ddl-auto: update

    properties:
      hibernate:
        //Hibernate가 생성하는 SQL 쿼리를 포기 좋게 포맷팅한다. 
        format_sql: true

    //JPA가 실행하는 SQL 쿼리를 큰솔에 출력한다. true로 설정하면 SQL 쿼리가 큰솔에 출력된다.
    show-sql: true

    //Spring의 "Open Session in View" 패턴을 비활성화한다.
    //false : DB 세션이 서비스 계층에서 종료되고, 뷰 렌더링 중에는 DB 접근이 불가능하다. 
    open-in-view: false

```

## 2. Post 클래스 Entity 설정

```java
@Entity //Post 클래스는 JPA가 관리하는 엔티티 (엔티티는 DB 테이블에 매핑된다)
@Data //Lombok을 통해 getter, setter, toString,equals, hashCode 메서드를 자동으로 생성해줌
@NoArgsConstructor //파라미터가 없는 기본 생성자를 자동으로 생성한다
public class Post {

    @Id //id 필드가 엔티티의 기본 키 
    @GeneratedValue(strategy = GenerationType.IDENTITY) //기본 키 값이 DB에 의해 자동으로 생성된다. 
    private Long id;

    private String title;
    private String content;

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
```

## 3. DTO 클래스 생성

> DTO란 Data Transfer Object의 약자로, 데이터를 주고받기 위한 객체이다.
>> 프레젠테이션 계층(뷰, 컨트롤러)과 서비스 계층, 서비스 계층과 데이터 접근 계층 간에 데이터를 주고 받기 위해 사용
>>> 도메인 모델(엔티티)는 데이터 접근 계층에서 데이터가 저장되는 DB와 직접적으로 관련되어 있는 객채라서 최대한 수정되지 않는 
  것이 좋다.
>>>> 또한, 도메인 모델은 View에서는 노출되면 안되는 민감한 정보들을 지니고 있다. 

- PostRegisterDTO

> 일련 번호를(id)를 제외한 title, content 필드만 이용하여 Post 객체 생성해주는 DTO 클래스 

```java
@Data //Lombok을 통해 getter, setter, toString,equals, hashCode 메서드를 자동으로 생성해줌
@NoArgsConstructor //파라미터가 없는 기본 생성자를 자동으로 생성해준다.
@AllArgsConstructor //모든 필드를 파라미터로 받는 생성자를 자동으로 생성해준다.
@Builder // 객체 생성시 빌더 패턴을 통해 가독성을 높여주는 객체 생성 코드를 보여준다. 
public class PostRegisterDTO {

    private String title;
    private String content;

    //PostRegisterDTO 객체를 통해 Post 엔티티 객체를 생성하 메소드 
    public Post toEntity() {
        return new Post(title, content);
    }
}
```

- PostResponseDTO

> Post 엔티티로부터 데이터를 받아 클라이언트에게 필요한 형태로 변환하여 전달하는 역할을 한다. 

```java
@Data //Lombok을 통해 getter, setter, toString,equals, hashCode 메서드를 자동으로 생성해줌
@NoArgsConstructor //파라미터가 없는 기본 생성자를 자동으로 생성해준다.
@AllArgsConstructor //모든 필드를 파라미터로 받는 생성자를 자동으로 생성해준다.
@Builder // 객체 생성시 빌더 패턴을 통해 가독성을 높여주는 객체 생성 코드를 보여준다. 
public class PostResponseDTO {

    private String title;
    private String content;

    //해당 post 객체를 통해 PostResponseDTO 객체의 title, content 필드에 값을 지정하는 메소드
    public PostResponseDTO(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
     }
}
```

## 4. PostRepository 인터페이스 

> DB 접근은 JpaRepository를 사용하여 편리하게 접근이 가능하다.
>> JPA가 SQL을 자동으로 생성해주어, 객체로 데이터에 접근한다. 

> PostRepository 객체는 다양한 기본 메소드를 포함한다.
>> findAll(), findById(id), save(Post), saveAll(PostList), delete(Post), deleteAll(PostList), count(), exists(id), flush()

```java
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
```

## 5. PostService 클래스

> 컨트롤러와 DB 접근을 흐름을 담당하는 비즈니스 로직

```java
@Service // 비즈니스 로직을 처리하는 서비스 계층을 명시해준다
@Transactional
 /*
    DB에서 비정상 처리되어 데이터의 부정합이 발생한다면 원래 상태로 원상 복귀하고,
    정상적으로 프로그램이 진행되었다면 데이터에 최종적으로 적용하게 되어 데이터의 부정합을 방지하게 되는 것이다.
 */
@RequiredArgsConstructor //final 필드나 @NonNull 붙은 필드를 매개변수로 받는 생성자를 자동으로 생성한다.
public class PostService {

    private final PostRepository postRepository;

    //R : 모든 게시글을 조회하여 반환한다.
    public List<Post> getAllPost() {
        return postRepository.findAll();
    }

    //R : 주어진 ID를 가진 게시글을 조회하여 PostResponseDTO 객체로 반환한다.
    //해당 ID가 없다면 예외를 발생시킨다. 
    public PostResponseDTO getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("잘못된 Post ID 입니다."));
        return new PostResponseDTO(post);
    }

    //C : 새로운 게시글을 생성하여 데이터베이스에 저장한다.
    //PostRegisterDTO 객체를 인수로 받아서 Post 엔티티로 변환한 후 저장한다. 
    public Post createPost(PostRegisterDTO post) {
        return postRepository.save(post.toEntity());
    }

    //U : 주어진 ID를 가진 게시글을 수정하고, 수정된 게시글을 데이터베이스에 저장한다.
    //해당 ID가 없다면 예외를 발생시킨다. 
    public Post updatePost(Long id, PostRegisterDTO updatedPost) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("잘못된 Post ID 입니다."));
        post.update(updatedPost.getTitle(), post.getContent());

        return postRepository.save(post);
    }

    //주어진 ID를 가진 게시글을 삭제한다. 
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

}
```

## 6. PostController 클래스 

> 

```java
@RestController //모든 메서드는 JSON 형식의 응답을 반환한다. 
@RequestMapping("/post") //위 클래스는 모든 요청 매핑이 /post 경로를 기본으로 한다
@RequiredArgsConstructor //final 필드나 @NonNull 붙은 필드를 매개변수로 받는 생성자를 자동으로 생성한다.
public class PostController {

    private final PostService postService;

    //R : 전체 조회
    //모든 게시글을 조회하여 반환한다. 
    @GetMapping
    public List<Post> getAllPost() {
        return postService.getAllPost();
    }

    //R : 아이디로 조회
    //주어진 ID를 가진 게시글을 조회하여 PostResponseDTO 객체로 반환한다.
    //경로 변수로 ID를 받는다.
    @GetMapping("/{id}")
    public PostResponseDTO getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    //C : 게시글 생성
    //새로운 게시글을 생성하여 DB에 저장한다.
    //JSON에 게시글 필드로 받아 PostRegisterDTO 객체를 생성한다. 
    @PostMapping
    public Post createPost(@RequestBody PostRegisterDTO post) {
        return postService.createPost(post);
    }

    //U : 게시글 수정
    //주어진 ID를 가진 게시글을 수정하고, 수정된 게시글을 DB에 저장한다.
    //경로 변수로 ID를 받으며, JSON에 게시글 필드로 받아 PostRegisterDTO 객체를 생성한다. 
    @PutMapping("/{id}")
    public Post updatePost(@PathVariable Long id, @RequestBody PostRegisterDTO updatedPost) {
        return postService.updatePost(id, updatedPost);
    }

    //D : 게시글 삭제
    //주어진 ID를 가진 게시글을 삭제한다.
    //경로 변수로 ID를 받는다. 
    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }


}
```

## 실습 적용 

- 레코드 생성
![image](https://github.com/dbqudgns/BoardCRUD/assets/68501204/58d5bc83-ed0c-4f68-999e-9ad5e1d91c49)

- MySQL 확인 : 정상적으로 레코드가 생성됨을 볼 수 있다.
![image](https://github.com/dbqudgns/BoardCRUD/assets/68501204/fb3b2642-9cf3-4c45-ae13-dc87b0106530)

- 게시글 전체 조회 : 미리 레코드를 3개 만들고 조회하였다. 
![image](https://github.com/dbqudgns/BoardCRUD/assets/68501204/02e42fac-8122-4c16-ab06-f3ce313acdf8)
![image](https://github.com/dbqudgns/BoardCRUD/assets/68501204/d6f188c2-0b47-4d67-a697-5dc4f530de6e)

- 게시글 ID로 조회 
![image](https://github.com/dbqudgns/BoardCRUD/assets/68501204/3fc05d24-8b4a-4b03-9cff-a226c713b291)

- 게시글 수정 : 4번 레코드 content 성공-> 성공성공
![image](https://github.com/dbqudgns/BoardCRUD/assets/68501204/58aba49b-539d-4d3e-8f43-efd224e5ef7c)

- MySQL 확인 : 
![image](https://github.com/dbqudgns/BoardCRUD/assets/68501204/991d8b58-29d1-4530-89a2-24f30a3131da)

###참고 : https://uknowblog.tistory.com/311

- 게시글 삭제 : 5번 레코드 삭제 
![image](https://github.com/dbqudgns/BoardCRUD/assets/68501204/4614b063-b712-4eee-a080-b305d54e8ca0)

- MySQL 확인 : 
![image](https://github.com/dbqudgns/BoardCRUD/assets/68501204/f1570ef2-b258-4143-a9b5-fb64ddb87128)
