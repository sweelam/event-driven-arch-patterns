package main

import (
	"eda-order-fullfilement/app"
	"eda-order-fullfilement/middleware"
	"eda-order-fullfilement/model"
	"fmt"
)

func main() {
	rmq := middleware.RMQ{}
	err := rmq.Connect()

	if err != nil {
		fmt.Println("Error connecting to RMQ:", err)
	}

	orderProducer := app.OrderProducer{RMQ: &rmq}
	message := model.Message{
		FirstName:   "John",
		LastName:    "Doe",
		Email:       "md.sweelam@gmail.com",
		Phonenumber: "1234567890",
	}

	orderProducer.Send(message, "order-fullfillment-q")

	defer rmq.Conn.Close()

	orderConsumer := app.OrderConsumer{RMQ: &rmq}
	orderConsumer.Consume("order-fullfillment-q")
}
