package gateway

import (
	"context"
	"task-api/domain"
	"task-api/driver"
	"testing"

	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

type mockTaskDriver struct {
	mock.Mock
}

func (_m mockTaskDriver) FindAll(ctx context.Context) (res []driver.TaskTable, err error) {
	ret := _m.Called(ctx)
	return ret.Get(0).([]driver.TaskTable), ret.Error(1)
}

func (_m mockTaskDriver) Save(ctx context.Context, task driver.TaskTable) (driver.TaskTable, error) {
	ret := _m.Called(ctx, task)
	return ret.Get(0).(driver.TaskTable), ret.Error(1)
}

func Test_taskGateway_FindAll(t *testing.T) {
	mockTaskDriver := new(mockTaskDriver)
	ctx := context.Background()
	target := taskGateway{mockTaskDriver}

	mockTaskDriver.On("FindAll", ctx).Return([]driver.TaskTable{{Id: 1, Title: "title1"}, {Id: 2, Title: "title2"}}, nil)

	actual, err := target.FindAll(ctx)

	expected := []domain.Task{{Id: 1, Title: "title1"}, {Id: 2, Title: "title2"}}
	assert.Equal(t, expected, actual)
	assert.NoError(t, err)
	mockTaskDriver.AssertExpectations(t)
}

func Test_taskGateway_Save(t *testing.T) {
	mockTaskDriver := new(mockTaskDriver)
	ctx := context.Background()
	target := taskGateway{mockTaskDriver}

	mockTaskDriver.On("Save", ctx, driver.TaskTable{Title: "new title"}).Return(driver.TaskTable{Id: 3, Title: "new title"}, nil)

	actual, err := target.Save(ctx, domain.Task{Title: "new title"})

	assert.Equal(t, domain.Task{Id: 3, Title: "new title"}, actual)
	assert.NoError(t, err)
	mockTaskDriver.AssertExpectations(t)
}
