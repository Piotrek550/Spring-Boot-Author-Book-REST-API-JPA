package com.devtiro.database.repositories;

import com.devtiro.database.TestDataUtil;
import com.devtiro.database.domain.entities.AuthorEntity;
import com.devtiro.database.domain.entities.BookEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookEntityRepositoryIntegrationTests {

    private BookRepository underTest;

    @Autowired
    public BookEntityRepositoryIntegrationTests(BookRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatBookCanBeCreatedAndRecalled() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorEntityA();
        BookEntity bookEntity = TestDataUtil.createTestBookEntityA(authorEntity);
        underTest.save(bookEntity);
        Optional<BookEntity> result = underTest.findById(bookEntity.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(bookEntity);
    }

    @Test
    public void testThatMultipleBooksCanBeCreatedAndRecalled() {
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorEntityA();
        AuthorEntity authorEntityB = TestDataUtil.createTestAuthorEntityB();
        AuthorEntity authorEntityC = TestDataUtil.createTestAuthorEntityC();
        BookEntity bookEntityA = TestDataUtil.createTestBookEntityA(authorEntityA);
        BookEntity bookEntityB = TestDataUtil.createTestBookEntityB(authorEntityB);
        BookEntity bookEntityC = TestDataUtil.createTestBookEntityC(authorEntityC);
        underTest.save(bookEntityA);
        underTest.save(bookEntityB);
        underTest.save(bookEntityC);

        Iterable<BookEntity> result = underTest.findAll();

        assertThat(result).hasSize(3).containsExactly(bookEntityA, bookEntityB, bookEntityC);
    }

    @Test
    public void testThatBookCanBeUpdated() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorEntityA();

        BookEntity bookEntity = TestDataUtil.createTestBookEntityA(authorEntity);
        underTest.save(bookEntity);
        bookEntity.setTitle("TITLE UPDATED");
        underTest.save(bookEntity);
        Optional<BookEntity> result = underTest.findById(bookEntity.getIsbn());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(bookEntity);
    }

    @Test
    public void testThatBookCanBeDeleted() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorEntityA();

        BookEntity bookEntity = TestDataUtil.createTestBookEntityA(authorEntity);
        underTest.save(bookEntity);
        underTest.deleteById(bookEntity.getIsbn());
        Optional<BookEntity> result = underTest.findById(bookEntity.getIsbn());
        assertThat(result).isEmpty();
    }
}
