package app

import (
	"eda-order-fullfilement/middleware"
	"eda-order-fullfilement/model"
	"encoding/json"
	"fmt"
	"time"

	amqp "github.com/rabbitmq/amqp091-go"
)

type OrderConsumer struct {
	RMQ *middleware.RMQ
	Ch  *amqp.Channel
}

func NewOrderConsumer(rmq *middleware.RMQ) *OrderConsumer {
	return &OrderConsumer{
		RMQ: rmq,
	}
}

func (oc *OrderConsumer) Consume(queueName string) {
	conn := oc.RMQ.Conn

	channelConnect(oc, conn)

	// Declare the queue to ensure it exists
	bm := middleware.NewBrokerManager(oc.Ch)
	bm.DeclareQueueAndBind(queueName, "training-exchange", "direct")

	// Create a unique consumer tag
	consumerTag := fmt.Sprintf("consumer-%d", time.Now().UnixNano())
	mssgs, shouldReturn := consumeMessage(oc, queueName, consumerTag)
	if shouldReturn {
		return
	}

	displayMessage(mssgs)

	keepRunning()
}

func channelConnect(oc *OrderConsumer, conn *amqp.Connection) {
	if oc.Ch == nil {
		ch, err := conn.Channel()

		if err != nil {
			middleware.PanicOnError(err, "Failed to open a channel")
		}

		oc.Ch = ch
	}
}

func consumeMessage(oc *OrderConsumer, queueName string, consumerTag string) (<-chan amqp.Delivery, bool) {
	mssgs, err := oc.Ch.Consume(queueName,
		consumerTag,
		false,
		false,
		false,
		false,
		nil,
	)

	if err != nil {
		middleware.FailOnError(err, "couldn't consumer message")
		return nil, true
	}

	return mssgs, false
}

func displayMessage(mssgs <-chan amqp.Delivery) {
	go func() {
		var message model.Message
		for msg := range mssgs {
			if err := json.Unmarshal(msg.Body, &message); err != nil {
				fmt.Printf("Error unmarshaling message: %v\n", err)
				msg.Nack(false, false) // Don't requeue
				continue
			}

			fmt.Println("Message received ", message)
			msg.Ack(false) // Acknowledge after successful processing
		}
	}()
}

func keepRunning() {
	forever := make(chan struct{})
	<-forever
}
