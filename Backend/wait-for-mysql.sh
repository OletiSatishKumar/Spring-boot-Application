#!/bin/sh

echo "⏳ Waiting for MySQL to be ready..."

# Loop until the MySQL port is open
while ! nc -z db 3306; do
  sleep 1
done

echo "✅ MySQL is up - starting Spring Boot app..."
exec java -jar app.jar
