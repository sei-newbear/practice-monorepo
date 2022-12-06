# syntax=docker/dockerfile:1

FROM golang:1.19-buster AS build

WORKDIR /app

COPY go.mod ./
COPY go.sum ./
RUN go mod download

COPY . ./

RUN go test ./...

RUN go build -o /task-api


FROM gcr.io/distroless/base-debian10

WORKDIR /

COPY --from=build /task-api /task-api

EXPOSE 8089

USER nonroot:nonroot

ENTRYPOINT ["/task-api"]