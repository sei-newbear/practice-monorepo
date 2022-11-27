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
