package main

import (
	"task-api/driver"
	"task-api/rest"

	"github.com/gin-gonic/gin"

	_ "github.com/lib/pq"
)

func main() {
	driver.FindTasks()
	router := gin.Default()
	rest.RouteHealth(router)
	rest.RouteTask(router)

	router.Run(":8089")
}
