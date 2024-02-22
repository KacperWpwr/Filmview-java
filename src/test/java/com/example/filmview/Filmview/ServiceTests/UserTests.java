package com.example.filmview.Filmview.ServiceTests;

import com.example.filmview.FilmviewApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = FilmviewApplication.class
)
public class UserTests {
}
