package rest

import (
	"net/http"
	"task-api/domain"
	"task-api/usecase"

	"github.com/gin-gonic/gin"
)

type TaskHandler struct {
	taskUseCase usecase.TaskUseCase
}

func RouteTask(engine *gin.Engine) {
	v1 := engine.Group("/v1")

	taskUseCase := usecase.TaskUseCase{}
	taskHandler := TaskHandler{taskUseCase}

	v1.GET("/tasks", taskHandler.getTasks)
	v1.POST("/tasks", taskHandler.createTask)
}

func (taskHandler *TaskHandler) getTasks(c *gin.Context) {
	tasks := taskHandler.taskUseCase.GetTasks()
	tasksJson := createTasksJson(tasks)
	c.JSON(http.StatusOK, gin.H{
		"tasks": tasksJson,
	})
}

func (taskHandler *TaskHandler) createTask(c *gin.Context) {
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
