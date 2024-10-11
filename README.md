# 📚 SPRING PLUS SCHEDULER
더욱더 향상된 스케쥴러 서비스.

# 🚀 STACK

Environment


![인텔리제이](   https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![](https://img.shields.io/badge/Gradle-02303a?style=for-the-badge&logo=gradle&logoColor=white)
![](https://img.shields.io/badge/Postman-ff6c37?style=for-the-badge&logo=postman&logoColor=white)
![깃허브](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)
![깃이그노어](https://img.shields.io/badge/gitignore.io-204ECF?style=for-the-badge&logo=gitignore.io&logoColor=white)
![깃](https://img.shields.io/badge/GIT-E44C30?style=for-the-badge&logo=git&logoColor=white)

Development

![스프링부트](https://img.shields.io/badge/SpringBoot-6db33f?style=for-the-badge&logo=springboot&logoColor=white)
![자바](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)

Communication

![슬랙](  https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white)
![노션](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white)

# 🏗️트러블슈팅

## Hibernate "Transient Entity" 예외

```java
@Getter
@Entity
@NoArgsConstructor
@Table(name = "todos")
public class Todo extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String contents;
    private String weather;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "todo", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "todo", cascade = CascadeType.PERSIST)
    private List<Manager> managers = new ArrayList<>();

    public Todo(String title, String contents, String weather, User user) {
        this.title = title;
        this.contents = contents;
        this.weather = weather;
        this.user = user;
        this.managers.add(new Manager(user, this));
    }
}
```
### 문제상황

- **"Not-null property references a transient value"** 예외가 발생됨

- Todo 엔티티가 User와 Manager 엔티티와 관계를 맺고 있을 때, 아직 데이터베이스에 저장되지 않은(transient) 엔티티를 참조하려고 할 때 발생한 오류

### 원인 분석

- Todo 엔티티는 User와 Manager와의 관계에서 **@ManyToOne** 및 **@OneToMany** 관계를 설정하고 있지만, Hibernate는 관계에 있는 User와 Manager가 아직 저장되지 않은 상태에서 이를 저장하려고 시도할 때 오류가 발생한다

- Todo 엔티티를 저장할 때 User와 Manager 엔티티가 먼저 저장되지 않으면 발생하는 문제

### 해결방법

- CascadeType.PERSIST 사용: Todo 엔티티와 User, Manager 간의 관계에 cascade = CascadeType.PERSIST를 추가함으로써, Todo가 저장될 때 User와 Manager가 자동으로 데이터베이스에 저장되도록 설정

```java
@Getter
@Entity
@NoArgsConstructor
@Table(name = "todos")
public class Todo extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String contents;
    private String weather;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "todo", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "todo", cascade = CascadeType.PERSIST)
    private List<Manager> managers = new ArrayList<>();

    public Todo(String title, String contents, String weather, User user) {
        this.title = title;
        this.contents = contents;
        this.weather = weather;
        this.user = user;
        this.managers.add(new Manager(user, this));
    }
}
```

# 👹프로젝트 회고

트러블 슈팅에 작성한 예외가 Cascade를 적용하는 상황에서 발생하는 에러인데
테스트를 하지 않고 그 후 개발을 진행해서 에러를 찾는데 시간을 많이 사용하였음
코드를 수정하는 매 과정마다 기능 테스트를 소홀히 하지 말것을 다짐...