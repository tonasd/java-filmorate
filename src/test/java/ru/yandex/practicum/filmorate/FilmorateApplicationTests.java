package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmorateApplicationTests {
	private final UserService userService;
	private final FilmService filmService;

	static User user;
	static User friend;
	static Film film1;
	static Film film2;

	@BeforeAll
	static void setUp() {
		user = new User("email@email.ru",
				"login",
				"name",
				LocalDate.of(1990, 1, 1)
		);
		friend = new User("friend@email.ru",
				"friend",
				"friend",
				LocalDate.of(1990, 1, 2)
		);
		film1 = new Film("film1",
				"film1 description",
				LocalDate.of(2000, 2, 2),
				120,
				null
		);

		film2 = new Film("film2",
				"film2 description",
				LocalDate.of(2000, 2, 2),
				120,
				null
		);
	}

	@Test
	void contextLoads() {
	}



	@Test
	@Order(1)
	void putUser() {
		User userCreated = userService.create(user);
		assertNotNull(userCreated, "putUser does not return correct object");
		assertEquals(1, userCreated.getId(), "first created user must have id=1");
		assertEquals(user.getLogin(), userCreated.getLogin(), "put add object with incorrect fields");
		user = userCreated;
	}

	@Test
	@Order(2)
	void getUser() {
		User actual = userService.get(user.getId());
		assertNotNull(actual, "user exist but not returned");
		assertEquals(user.getId(), actual.getId(), "returned user id is incorrect");
	}

	@Test
	@Order(3)
	void getAllUsers() {
		List<User> users = userService.getAll();
		assertEquals(1, users.size(), "getAllUsers must return 1 user");
		friend = userService.create(friend);
		users = userService.getAll();
		assertEquals(2, users.size(), "getAllUsers must return 2 users");

	}

	@Test
	@Order(4)
	void updateUser() {
		LocalDate updatedBirthday = LocalDate.of(1992, 1, 1);
		user.setBirthday(updatedBirthday);
		userService.update(user);
		User updatedUser = userService.get(user.getId());
		assertEquals(user, updatedUser, "User sent for update is not equal to result of update");
	}

	@Test
	@Order(5)
	void addFriendAndGetFriendsIds() {
		userService.addFriend(user.getId(), friend.getId());
		assertEquals(1, userService.getFriends(user.getId()).size(), "user must have 1 friend");
		assertEquals(0, userService.getFriends(friend.getId()).size(), "user must have 0 friends");
	}

	@Test
	@Order(6)
	void removeFriend() {
		userService.removeFriend(user.getId(), friend.getId());
		assertEquals(0, userService.getFriends(friend.getId()).size(), "user must have 0 friends");
	}

	@Test
	@Order(7)
	void putFilm() {
		Film createdFilm = filmService.create(film1);
		assertNotNull(createdFilm, "putFilm does not return correct object");
		assertEquals(1, createdFilm.getId(), "first created film must have id=1");
		assertEquals(film1.getName(), createdFilm.getName(), "putFilm add object with incorrect fields");
		film1 = createdFilm;
	}

	@Test
	@Order(8)
	void getFilm() {
		Film actual = filmService.get(film1.getId());
		assertNotNull(actual, "film exist but not returned");
		assertEquals(film1.getId(), actual.getId(), "returned film id is incorrect");
	}

	@Test
	@Order(9)
	void getAllFilms() {
		List<Film> films = filmService.getAll();
		assertEquals(1, films.size(), "getAllFilms must return 1 film");
		film2 = filmService.create(film2);
		films = filmService.getAll();
		assertEquals(2, films.size(), "getAllFilms must return 2 users");
	}

	@Test
	@Order(10)
	void updateFilm() {
		film1.setDuration(60);
		filmService.update(film1);
		Film filmUpdated = filmService.get(film1.getId());
		assertEquals(film1, filmUpdated, "Film sent for update is not equal to result of update");
	}

	@Test
	@Order(11)
	void addLike() {
		filmService.addLike(film2.getId(), user.getId());
		assertEquals(filmService.getPopular(1).get(0), film2);
	}

	@Test
	@Order(12)
	void getPopular() {
		filmService.addLike(film1.getId(), user.getId());
		filmService.addLike(film2.getId(), friend.getId());
		List<Film> films = filmService.getPopular(10);
		assertEquals(List.of(film2, film1), films);
	}

	@Test
	@Order(13)
	void removeLike() {
		filmService.removeLike(film2.getId(), user.getId());
		assertEquals(filmService.getPopular(1).get(0), film1);
	}






}
