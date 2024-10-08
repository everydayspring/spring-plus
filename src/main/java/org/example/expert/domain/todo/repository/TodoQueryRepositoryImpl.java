package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.example.expert.domain.todo.entity.QTodo.todo;

@Repository
@RequiredArgsConstructor
public class TodoQueryRepositoryImpl implements TodoQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        QTodo todo = QTodo.todo;
        QUser user = QUser.user;

        Todo foundTodo = queryFactory
                .selectFrom(todo)
                .leftJoin(todo.user, user).fetchJoin()
                .where(
                        todoIdEq(todoId)
                ).fetchOne();

        return Optional.ofNullable(foundTodo);
    }

    private BooleanExpression todoIdEq(Long todoId) {
        return todoId != null ? todo.id.eq(todoId) : null;
    }
}
