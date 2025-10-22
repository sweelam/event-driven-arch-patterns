package app

import (
	"context"
	"eda-order-fullfilement/middleware"
	"encoding/json"
	"time"

	"eda-order-fullfilement/model"

	amqp "github.com/rabbitmq/amqp091-go"
)

type OrderProducer struct {
	RMQ *middleware.RMQ
	Ch  *amqp.Channel
}

func (o *OrderProducer) Send(message model.Message, queueName string) error {
	conn := o.RMQ.Conn

	if o.Ch == nil {
		ch, err := conn.Channel()

		if err != nil {
			middleware.FailOnError(err, "Failed to open a channel")
			return err
		}

		o.Ch = ch
	}

	body, _ := json.Marshal(message)
	o.PublishMessage(queueName, body)

	return nil
}

func (o *OrderProducer) PublishMessage(queueName string, body []byte) error {
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	err := o.Ch.PublishWithContext(ctx,
		"training-exchange", // exchange
		queueName,           // routing key
		false,               // mandatory
		false,               // immediate
		amqp.Publishing{
			ContentType:  "json",
			DeliveryMode: 2,
			Body:         body,
		})

	if err != nil {
		middleware.FailOnError(err, "can't publilsh")
		return err
	}

	return nil
}
