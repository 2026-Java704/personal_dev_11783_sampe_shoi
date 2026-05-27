INSERT INTO users (name, password)
VALUES
('田中太郎', 'test123'), 
('鈴木一郎', 'test456');

INSERT INTO tasks (username, title, closing_date, progress, memo, time, date, priority, created_at, category_id)
VALUES
('田中太郎', '開発', '2026/03/26', '完了', '依頼された機能実装', 500, '2026/04/02', '中', '2026/03/26',2),
('田中太郎', '絵', '2026/06/24', '未着手', '趣味の描画', 300, '2026/07/06', '中', '2026/06/22',1);

INSERT INTO categories (category_id, category_name)
VALUES
(1, '仕事'),
(2, '日常');