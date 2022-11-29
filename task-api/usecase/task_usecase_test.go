package usecase

import (
	"context"
	"task-api/domain"
	"testing"

	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

type mockTaskPort struct {
	mock.Mock
}

func (_m *mockTaskPort) FindAll(ctx context.Context) ([]domain.Task, error) {
	ret := _m.Called(ctx)
	return ret.Get(0).([]domain.Task), ret.Error(1)
}

func TestTaskUseCase_GetTasks(t *testing.T) {
	mockTaskPort := new(mockTaskPort)
	mockTaskPort.On("FindAll", context.Background()).Return([]domain.Task{}, nil)
	target := TaskUseCase{TaskPort: mockTaskPort}
	actual, err := target.GetTasks(context.Background())

	assert.Equal(t, actual, []domain.Task{})
	assert.NoError(t, err)
	mockTaskPort.AssertExpectations(t)

}
