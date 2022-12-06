-- テーブル作成
CREATE TABLE  task (
  id SERIAL,
  title TEXT,
  created_at TIMESTAMP NOT NULL default current_timestamp,
  PRIMARY KEY (id)
);

-- サンプルレコード作成
INSERT INTO task (title) VALUES
('hoge'),
('fuga');
