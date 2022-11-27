package port

import (
	"context"
	"task-api/domain"
)

type TaskPort interface {
	FindAll(ctx context.Context) ([]domain.Task, error)
}
