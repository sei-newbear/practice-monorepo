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

func (_m *mockTaskPort) Save(ctx context.Context, task domain.Task) (domain.Task, error) {
	ret := _m.Called(ctx, task)
	return ret.Get(0).(domain.Task), ret.Error(1)
}

func TestTaskUseCase_GetTasks(t *testing.T) {
	mockTaskPort := new(mockTaskPort)
	expected := []domain.Task{}
	ctx := context.Background()
	mockTaskPort.On("FindAll", ctx).Return(expected, nil)
	target := TaskUseCase{TaskPort: mockTaskPort}
	actual, err := target.GetTasks(ctx)

	assert.Equal(t, expected, actual)
	assert.NoError(t, err)
	mockTaskPort.AssertExpectations(t)
}

func TestTaskUseCase_Create(t *testing.T) {
	mockTaskPort := new(mockTaskPort)
	ctx := context.Background()
	expected := domain.Task{}
	mockTaskPort.On("Save", ctx, domain.Task{}).Return(expected, nil)
	target := TaskUseCase{mockTaskPort}
	actual, err := target.Create(ctx, expected)
	assert.Equal(t, expected, actual)
	assert.NoError(t, err)
	mockTaskPort.AssertExpectations(t)
}
