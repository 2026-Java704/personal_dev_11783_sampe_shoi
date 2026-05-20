package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // IDをデータベース側で自動連番に設定
	private Long id; // ユーザーの一意なIDを保存する変数

	private String name; // ユーザー名（ログイン時に使用）を保存する変数
	private String password; // パスワード（ログイン時に使用）を保存する変数

	// --- 以下、各変数を安全に読み書きするための ゲッター（get）と セッター（set） ---

	public Long getId() {
		return id; // ユーザーIDを取得する
	}

	public void setId(Long id) {
		this.id = id; // ユーザーIDを設定する
	}

	public String getName() {
		return name; // ユーザー名を取得する
	}

	public void setName(String name) {
		this.name = name; // ユーザー名を設定する
	}

	public String getPassword() {
		return password; // パスワードを取得する
	}

	public void setPassword(String password) {
		this.password = password; // パスワードを設定する
	}
}