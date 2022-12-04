package driver

import (
	"context"
	"database/sql"
)

type TaskDriverImpl struct {
	DB *sql.DB
}

type TaskDriver interface {
	FindAll(ctx context.Context) (res []TaskTable, err error)
}

func (td *TaskDriverImpl) FindAll(ctx context.Context) (res []TaskTable, err error) {
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
		rows.Scan(&task.Id, &task.Title, TrashScanner{})
		taskRows = append(taskRows, task)
	}

	return taskRows, nil
}

type TaskTable struct {
	Id    int
	Title string
}

type TrashScanner struct{}

func (TrashScanner) Scan(interface{}) error {
	return nil
}
