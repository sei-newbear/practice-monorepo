package usecase

import (
	"context"
	"task-api/domain"
	"task-api/port"
)

type TaskUseCase struct {
	TaskPort port.TaskPort
}

func (tu TaskUseCase) GetTasks(ctx context.Context) ([]domain.Task, error) {
	return tu.TaskPort.FindAll(ctx)
}

func (tu TaskUseCase) Create(ctx context.Context, task domain.Task) (domain.Task, error) {
	return tu.TaskPort.Save(ctx, task)
}
