package usecase

import "task-api/domain"

func GetTasks() []domain.Task {
	return []domain.Task{{Id: 1, Title: "title1"}, {Id: 3, Title: "title3"}}
}
