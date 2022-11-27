package rest

import "github.com/gin-gonic/gin"

func RouteHealth(e *gin.Engine) {
	e.GET("/ping", func(c *gin.Context) {
		c.JSON(200, gin.H{
			"message": "pong",
		})
	})
}
