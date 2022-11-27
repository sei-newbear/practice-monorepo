package driver

import (
	"context"
	"database/sql"
	"fmt"
)

type TaskDriver struct {
	DB *sql.DB
}

func (td *TaskDriver) FindAll(ctx context.Context) (res []TaskTable, err error) {
	query := "SELECT * FROM task"
	stmt, err := td.DB.PrepareContext(ctx, query)
	if err != nil {
		return []TaskTable{}, err
	}

	rows, err := stmt.QueryContext(ctx)
	if err != nil {
		return []TaskTable{}, err
	}

	taskRows := []TaskTable{}

	for rows.Next() {
		task := TaskTable{}
		rows.Scan(&task.Id, &task.Title)
		taskRows = append(taskRows, task)
	}

	return taskRows, nil
}

func FindTasks() []TaskTable {
	db, err := sql.Open("postgres", "host=localhost port=5432 user=task_user password=task_pw dbname=task_db sslmode=disable")
	defer db.Close()

	if err != nil {
		fmt.Println(err)
	}

	rows, err := db.Query("SELECT * FROM task")

	if err != nil {
		fmt.Println(err)
	}

	taskRows := []TaskTable{}

	for rows.Next() {
		task := TaskTable{}
		rows.Scan(&task.Id, &task.Title)
		taskRows = append(taskRows, task)
	}

	fmt.Println(taskRows)

	return taskRows
}

type TaskTable struct {
	Id    int
	Title string
}
