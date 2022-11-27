package usecase

import "task-api/domain"

type TaskUseCase struct{}

func (tu TaskUseCase) GetTasks() []domain.Task {
	return []domain.Task{{Id: 1, Title: "title1"}, {Id: 3, Title: "title3"}}
}
