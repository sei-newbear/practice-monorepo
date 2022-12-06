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
	Save(ctx context.Context, task TaskTable) (TaskTable, error)
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

func (td *TaskDriverImpl) Save(ctx context.Context, task TaskTable) (TaskTable, error) {
	in, err := td.DB.PrepareContext(ctx, "insert into task (title) values ($1) RETURNING id")
	if err != nil {
		return TaskTable{}, err
	}

	lastInsertId := 0
	err = in.QueryRowContext(ctx, task.Title).Scan(&lastInsertId)
	if err != nil {
		return TaskTable{}, err
	}

	return TaskTable{lastInsertId, task.Title}, nil
}

type TaskTable struct {
	Id    int
	Title string
}

type TrashScanner struct{}

func (TrashScanner) Scan(interface{}) error {
	return nil
}
