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
