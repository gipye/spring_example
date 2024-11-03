package com.example.demo.user;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="user")
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User implements Cloneable {
	@AllArgsConstructor
	public static enum UserType {
		ADMIN((byte)0),
		GENERAL((byte)1);

		private final byte value;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private long id;

	@Column(name="username", nullable=false, length=30, unique=true)
	private String username;

	@Column(name="password", nullable=false, length=100, unique=false)
	private String password;

	@Enumerated(EnumType.ORDINAL)
	@Column(name="type", nullable=false)
	private UserType type;

	@CreationTimestamp
	@Column(name="createDate", nullable=false)
	private Timestamp createDate;

	@Override
	public User clone() {
		try {
			return (User)super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}
}
