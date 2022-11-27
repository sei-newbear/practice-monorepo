package rest

import (
	"net/http"
	"task-api/domain"
	"task-api/usecase"

	"github.com/gin-gonic/gin"
)

type TaskHandler struct {
	TaskUseCase usecase.TaskUseCase
}

func RouteTask(e *gin.Engine, th TaskHandler) {
	v1 := e.Group("/v1")
	v1.GET("/tasks", th.getTasks)
	v1.POST("/tasks", th.createTask)
}

func (th *TaskHandler) getTasks(c *gin.Context) {
	ctx := c.Request.Context()
	tasks, err := th.TaskUseCase.GetTasks(ctx)
	if err != nil {
		c.JSON(500, err)
		return
	}

	tasksJson := createTasksJson(tasks)
	c.JSON(http.StatusOK, gin.H{
		"tasks": tasksJson,
	})
}

func (th *TaskHandler) createTask(c *gin.Context) {
	var rt RequestTaskJson
	c.Bind(&rt)
	task := TaskJson{1, rt.Title}
	c.JSON(200, task)
}

func createTaskJson(t domain.Task) TaskJson {
	return TaskJson{t.Id, t.Title}
}

func createTasksJson(ts []domain.Task) []TaskJson {
	var tsj []TaskJson

	for _, v := range ts {
		tsj = append(tsj, createTaskJson(v))
	}
	return tsj
}

type TaskJson struct {
	Id    int    `json:"id"`
	Title string `json:"title"`
}

type RequestTaskJson struct {
	Title string `form:"title"`
}
