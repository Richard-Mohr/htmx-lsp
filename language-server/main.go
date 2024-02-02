package main

import (
	"bufio"
	"log"
	"os"
)

func main() {
	StartLspServer()
}

func StartLspServer() {
	scanner := bufio.NewScanner(os.Stdin)
	writer := bufio.NewWriter(os.Stdout)

	for scanner.Scan() {
		handleInput(scanner.Text(), writer)
	}
}

func handleInput(input string, writer *bufio.Writer) {
	f, err := os.OpenFile("test-log", os.O_RDWR|os.O_CREATE|os.O_APPEND, 0666)
	if err != nil {
		log.Fatalf("error opening file: %v", err)
	}
	defer func(f *os.File) {
		err := f.Close()
		if err != nil {
			log.Fatalf("error opening file: %v", err)
		}
	}(f)

	writer.WriteString("Testing")
}
