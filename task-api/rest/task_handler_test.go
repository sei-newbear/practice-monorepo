package rest

import (
	"task-api/domain"
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestCreateJson(t *testing.T) {
	tasks := []domain.Task{{Id: 11, Title: "test title11"}, {Id: 22, Title: "test title22"}}

	actual := createTasksJson(tasks)
	expected := []TaskJson{{11, "test title11"}, {22, "test title22"}}

	assert.Equal(t, actual, expected)
}
