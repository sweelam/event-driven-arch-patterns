package app

import (
	"eda-order-fullfilement/middleware"
	"eda-order-fullfilement/model"
	"encoding/json"
	"fmt"

	amqp "github.com/rabbitmq/amqp091-go"
)

type OrderConsumer struct {
	RMQ *middleware.RMQ
	Ch  *amqp.Channel
}

func (oc *OrderConsumer) Consume(queueName string) {
	conn := oc.RMQ.Conn

	if oc.Ch == nil {
		ch, err := conn.Channel()

		if err != nil {
			middleware.FailOnError(err, "Failed to open a channel")
		}

		oc.Ch = ch
	}

	mssgs, err := oc.Ch.Consume(queueName, "", true, false, false, false, nil)

	if err != nil {
		middleware.FailOnError(err, "couldn't consumer message")
	}

	go func() {
		var message model.Message
		for msg := range mssgs {
			json.Unmarshal(msg.Body, &message)
			fmt.Println("Message received ", message)
		}
	}()

	<-forever
}

var forever chan struct{}
