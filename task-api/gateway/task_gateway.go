package gateway

import (
	"context"
	"task-api/domain"
	"task-api/driver"
	"task-api/port"
)

type taskGateway struct {
	taskDriver driver.TaskDriver
}

func (tg *taskGateway) Save(ctx context.Context, task domain.Task) (domain.Task, error) {

	taskTable := driver.TaskTable{Title: task.Title}
	createdTask, err := tg.taskDriver.Save(ctx, taskTable)

	if err != nil {
		return domain.Task{}, err
	}

	return domain.Task{Id: createdTask.Id, Title: createdTask.Title}, nil

}

func NewTaskPort(taskDriver driver.TaskDriver) port.TaskPort {
	return &taskGateway{
		taskDriver: taskDriver,
	}
}

func (tg *taskGateway) FindAll(cxt context.Context) ([]domain.Task, error) {
	trs, err := tg.taskDriver.FindAll(cxt)

	if err != nil {
		return nil, err
	}

	tasks := []domain.Task{}
	for _, v := range trs {
		tasks = append(tasks, domain.Task{Id: v.Id, Title: v.Title})
	}

	return tasks, nil
}
