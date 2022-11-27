package rest

import (
	"net/http"
	"task-api/domain"
	"task-api/usecase"

	"github.com/gin-gonic/gin"
)

func RouteTask(engine *gin.Engine) {
	v1 := engine.Group("/v1")
	v1.GET("/tasks", getTasks)
	v1.POST("/tasks", createTask)
}

func getTasks(c *gin.Context) {
	tasks := usecase.GetTasks()
	tasksJson := CreateJson(tasks)
	c.JSON(http.StatusOK, gin.H{
		"tasks": tasksJson,
	})
}

func createTask(c *gin.Context) {
	var rt RequestTaskJson
	c.Bind(&rt)
	task := TaskJson{1, rt.Title}
	c.JSON(200, task)
}

type TaskJson struct {
	Id    int    `json:"id"`
	Title string `json:"title"`
}

func CreateJson(ts []domain.Task) []TaskJson {
	var tsj []TaskJson

	for _, v := range ts {
		tsj = append(tsj, TaskJson{v.Id, v.Title})
	}
	return tsj
}

type RequestTaskJson struct {
	Title string `form:"title"`
}
