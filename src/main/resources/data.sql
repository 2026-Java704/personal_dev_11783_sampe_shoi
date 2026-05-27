INSERT INTO users (name, password)
VALUES
('田中太郎', 'test123'), 
('鈴木一郎', 'test456');

INSERT INTO tasks (username, title, closing_date, progress, memo, time, date, priority, created_at, category_id)
VALUES
('田中太郎', '例１', '2026/05/20', '完了', '依頼された機能実装', 500, '2026/05/15', '中', '2026/05/14',2),
('田中太郎', '例２', '2026/06/20', '進行中', '趣味の描画', 100, '2026/06/15', '低', '2026/06/14',1);

INSERT INTO categories (category_id, category_name)
VALUES
(1, '仕事'),
(2, '日常');