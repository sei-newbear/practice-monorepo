package main

import (
	"task-api/rest"

	"github.com/gin-gonic/gin"
)

func main() {
	router := gin.Default()
	rest.RouteHealth(router)
	rest.RouteTask(router)

	router.Run(":8089")
}
