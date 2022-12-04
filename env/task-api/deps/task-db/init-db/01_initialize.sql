-- テーブル作成
CREATE TABLE  task (
  id INT,
  title TEXT,
  created_at TIMESTAMP NOT NULL default current_timestamp,
  PRIMARY KEY (id)
);

-- サンプルレコード作成
INSERT INTO task VALUES(1, 'hoge');
INSERT INTO task VALUES(2, 'fuga');
