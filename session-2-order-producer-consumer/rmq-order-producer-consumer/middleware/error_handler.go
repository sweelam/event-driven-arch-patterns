package middleware

import "fmt"

func FailOnError(err error, msg string) error {
	if err != nil {
		return fmt.Errorf("%s: %w", msg, err)
	}
	return nil
}

func PanicOnError(err error, msg string) {
	if err != nil {
		panic(fmt.Sprintf("%s: %s", msg, err.Error()))
	}
}
