# /v1/tasksのテスト
* "v1/tasks"へGETリクエストする
* ステータスコードが"200"であること
* "$.tasks"が存在すること
* "$.tasks"の要素数が"2"であること

## タスク一覧APIのレスポンスにidがある
* "$.tasks[0].id"の値が整数の"1"であること
* "$.tasks[1].id"の値が整数の"2"であること

## タスク一覧APIのレスポンスにタイトルがある
* "$.tasks[0].title"の値が文字列の"Kotlinについて調べる"であること
* "$.tasks[1].title"の値が文字列の"Gaugeについて勉強する"であること

## DB test -- test
//TODO DBテストの動作検証中。残りは変更を検知する仕組みを追加する
tags: watch-change-data
* DBが"expect/test1"通りになっていること
* "task"テーブルを条件"id = 1"で取得した"id"が"1"であること
* "task"テーブルを条件"id = 1"で取得した"title"が"Kotlinについて調べる"であること
* "task"テーブルを条件"id = 2"で取得した"id"が"2"であること
* "task"テーブルを条件"id = 2"で取得した"title"が"Gaugeについて勉強する"であること
* "task"テーブルを条件"id = 1"で取得した"created_at"が現在日時のプラスマイナス"60"秒以内で一致していること
* DBに"setup/test"をInsertする
* "task"テーブルに作成されたレコード数が"1"であること
