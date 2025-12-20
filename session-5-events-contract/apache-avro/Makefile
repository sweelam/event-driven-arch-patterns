.PHONY: help install run clean venv

# Variables
PYTHON := python3
VENV := venv
VENV_BIN := $(VENV)/bin
PYTHON_VENV := $(VENV_BIN)/python
PIP_VENV := $(VENV_BIN)/pip
SCRIPT := app/src/avro_loader.py
REQUIREMENTS := requirements.txt

# Default target
help:
	@echo "Available targets:"
	@echo "  make install    - Create virtual environment and install dependencies"
	@echo "  make run        - Run the Avro loader script"
	@echo "  make clean      - Remove virtual environment and generated files"
	@echo "  make venv       - Create virtual environment only"
	@echo "  make help       - Show this help message"

# Create virtual environment
venv:
	@if [ ! -d "$(VENV)" ]; then \
		echo "Creating virtual environment..."; \
		$(PYTHON) -m venv $(VENV); \
	else \
		echo "Virtual environment already exists"; \
	fi

# Install dependencies
install: venv
	@echo "Installing dependencies..."
	$(PIP_VENV) install --upgrade pip
	$(PIP_VENV) install -r $(REQUIREMENTS)
	@echo "Installation complete!"

# Run the script
run: install
	@echo "Running Avro loader..."
	$(PYTHON_VENV) $(SCRIPT)

# Clean up virtual environment and generated files
clean:
	@echo "Cleaning up..."
	@if [ -d "$(VENV)" ]; then \
		rm -rf $(VENV); \
		echo "Removed virtual environment"; \
	fi
	@if [ -f "app/resources/email.avro" ]; then \
		rm -f app/resources/email.avro; \
		echo "Removed generated Avro file"; \
	fi
	@echo "Cleanup complete!"

