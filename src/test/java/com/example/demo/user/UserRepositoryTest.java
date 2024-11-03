package com.example.demo.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.sql.Timestamp;

@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
	@Autowired
	UserRepository userRepository;

	@Autowired
	private EntityManager entityManager;

	@AfterEach
	public void end() {
		userRepository.deleteAll();
	}

	@Test
	public void testSaveUser() {
		User user = User.builder()
			.username("adminUser")
			.password("securePassword")
			.type(User.UserType.ADMIN)
			.build();

		User savedUser = userRepository.save(user);
		assertThat(savedUser).isNotNull();
		assertThat(savedUser.getUsername()).isEqualTo(user.getUsername());
		assertThat(savedUser.getPassword()).isEqualTo(user.getPassword());
		assertThat(savedUser.getType()).isEqualTo(user.getType());
	}

	@Test
	public void testDeleteUser() {
		User user = User.builder()
			.username("User")
			.password("securePassword")
			.type(User.UserType.GENERAL)
			.build();

		User savedUser = userRepository.save(user);
		Long userId = savedUser.getId();

		userRepository.deleteById(userId);
		assertThat(userRepository.existsById(userId)).isFalse();
	}

	@Test
	public void testUniqueUsername() {
		User user1 = User.builder()
			.username("duplicateUser")
			.password("password123")
			.type(User.UserType.GENERAL)
			.build();

		User user2 = User.builder()
			.username("duplicateUser") // same username as user1
			.password("anotherPassword")
			.type(User.UserType.ADMIN)
			.build();

		userRepository.save(user1);
		assertThrows(Exception.class, () -> {
			try {
				userRepository.save(user2);
			} catch (Exception e) {
				entityManager.clear(); // 예외 후 세션 초기화
				throw e; // 예외를 다시 던져 테스트 실패를 유지
			}
		});
	}

	@Test
	public void testIdGenerator() {
		User user1 = User.builder()
			.username("user1")
			.password("password123")
			.type(User.UserType.GENERAL)
			.build();

		User user2 = User.builder()
			.username("user2")
			.password("anotherPassword")
			.type(User.UserType.ADMIN)
			.build();

		User savedUser1 = userRepository.save(user1);
		User savedUser2 = userRepository.save(user2);

		assertThat(savedUser1.getId()).isEqualTo(savedUser2.getId()-1);
	}

	@Test
	public void testCreationTimestamp() {
		User user = User.builder()
			.username("user")
			.password("password123")
			.type(User.UserType.GENERAL)
			.build();

		Timestamp startTime = new Timestamp(System.currentTimeMillis());
		User savedUser = userRepository.save(user);
		Timestamp endTime = new Timestamp(System.currentTimeMillis());

		assertThat(savedUser.getCreateDate()).isAfter(startTime).isBefore(endTime);
	}
}
