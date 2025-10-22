package middleware

import (
	amqp "github.com/rabbitmq/amqp091-go"
)

type BrokerManager struct {
	Channel *amqp.Channel
}

func NewBrokerManager(channel *amqp.Channel) *BrokerManager {
	return &BrokerManager{
		Channel: channel,
	}
}

func (r *BrokerManager) DeclareQueueAndBind(queueName string, exchangeName string, exchangeType string) {
	if err := r.declareQueue(queueName); err != nil {
		PanicOnError(err, "Failed to declare a queue")
	}

	if err := r.declareQueueExchange(exchangeName, exchangeType); err != nil {
		PanicOnError(err, "Failed to declare a queue")
	}

	r.Channel.QueueBind(queueName, queueName, exchangeName, true, nil)
}

func (r *BrokerManager) declareQueueExchange(exchangeName string, exchangeType string) error {
	return r.Channel.ExchangeDeclare(
		exchangeName,
		exchangeType,
		true,
		false,
		false,
		true,
		nil,
	)
}

func (r *BrokerManager) declareQueue(queueName string) error {
	_, err := r.Channel.QueueDeclare(
		queueName, // name
		true,      // durable
		false,     // delete when unused
		false,     // exclusive
		false,     // no-wait
		nil,       // arguments
	)

	return err
}
