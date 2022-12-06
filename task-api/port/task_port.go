package port

import (
	"context"
	"task-api/domain"
)

type TaskPort interface {
	FindAll(ctx context.Context) ([]domain.Task, error)
	Save(ctx context.Context, task domain.Task) (domain.Task, error)
}
