package middleware

import (
	"fmt"

	amqp "github.com/rabbitmq/amqp091-go"
)

type RMQ struct {
	Conn *amqp.Connection
}

func NewRmq() *RMQ {
	return &RMQ{}
}

func (r *RMQ) Connect() error {
	Conn, err := amqp.Dial("amqp://myuser:mypassword@localhost:5672/")

	if err != nil {
		return fmt.Errorf("couldn't connect RMQ %w", err)
	}

	r.Conn = Conn

	return nil
}
