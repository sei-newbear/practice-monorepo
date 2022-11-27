package main

import (
	"database/sql"
	"os"
	"task-api/driver"
	"task-api/gateway"
	"task-api/rest"
	"task-api/usecase"

	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"

	_ "github.com/lib/pq"
)

func main() {
	engine := gin.Default()
	engine.Use(cors.New(cors.Config{
		AllowOrigins: []string{
			"*",
		},
	}))
	rest.RouteHealth(engine)
	rest.RouteTask(engine, InitTaskHandler())

	engine.Run(":8089")
}

func InitDb() *sql.DB {
	dsn := os.Getenv("TASK_DB_DSN")
	if dsn == "" {
		dsn = "host=localhost port=5432 user=task_user password=task_pw dbname=task_db sslmode=disable"
	}
	db, _ := sql.Open("postgres", dsn)
	return db
}

func InitTaskHandler() rest.TaskHandler {
	taskDriver := driver.TaskDriver{DB: InitDb()}
	taskPort := gateway.NewTaskPort(taskDriver)
	taskUseCase := usecase.TaskUseCase{TaskPort: taskPort}
	return rest.TaskHandler{TaskUseCase: taskUseCase}
}
