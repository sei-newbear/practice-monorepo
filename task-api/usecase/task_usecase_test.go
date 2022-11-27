package usecase

import (
	"context"
	"task-api/domain"
	"testing"

	"github.com/stretchr/testify/assert"
)

type mockTaskPort struct {
	calls int
	args  []context.Context
}

func (m *mockTaskPort) FindAll(ctx context.Context) ([]domain.Task, error) {
	m.calls++
	m.args = append(m.args, ctx)

	res := []domain.Task{{}}
	return res, nil
}

func TestTaskUseCase_GetTasks(t *testing.T) {
	mockTaskPort := &mockTaskPort{0, []context.Context{}}
	target := TaskUseCase{TaskPort: mockTaskPort}
	actual, err := target.GetTasks(context.Background())

	assert.Equal(t, actual, []domain.Task{{}})
	assert.Nil(t, err)
}
