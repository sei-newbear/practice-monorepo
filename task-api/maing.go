package main

import (
	"task-api/driver"
	"task-api/rest"

	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"

	_ "github.com/lib/pq"
)

func main() {
	driver.FindTasks()
	engine := gin.Default()
	engine.Use(cors.New(cors.Config{
		AllowOrigins: []string{
			"*",
		},
	}))
	rest.RouteHealth(engine)
	rest.RouteTask(engine)

	engine.Run(":8089")
}
