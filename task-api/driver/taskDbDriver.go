package driver

import (
	"database/sql"
	"fmt"
)

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
