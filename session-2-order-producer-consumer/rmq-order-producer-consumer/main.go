package main

import (
	"eda-order-fullfilement/app"
	m "eda-order-fullfilement/middleware"
	"eda-order-fullfilement/model"
	"fmt"
	"strconv"
	"time"
)

const queueName = "order-fullfillment-q"

func main() {
	rmq := m.NewRmq()

	if err := rmq.Connect(); err != nil {
		m.PanicOnError(err, "Error connecting to RMQ:")
	}

	defer func() {
		if err := rmq.Conn.Close(); err != nil {
			fmt.Printf("Error closing connection: %v\n", err)
		}
	}()

	// Start consumer first
	orderConsumer := app.NewOrderConsumer(rmq)
	go orderConsumer.Consume(queueName)

	// Wait a moment for consumer to be ready
	time.Sleep(2 * time.Second)

	// Then send the message
	orderProducer := app.OrderProducer{RMQ: rmq}

	for i := range 10 {
		message := model.Message{
			FirstName:   "John",
			LastName:    "Doe",
			Email:       "johnDoe@gmail.com",
			Phonenumber: "1234567890" + strconv.Itoa(i),
		}

		if err := orderProducer.Send(message, queueName); err != nil {
			fmt.Println("Error sending message:", err)
		}
	}

	// Keep the program running to allow consumer to process
	select {}
}
