# Stage 1: Build React app
FROM node:18 AS build
WORKDIR /app

# Copy only package files first (for better Docker caching)
COPY package*.json ./
RUN npm install

# Then copy the rest of the project
COPY . .
RUN npm run build

# Stage 2: Serve using NGINX
FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html

# Optional: Expose port and minimal logging
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
