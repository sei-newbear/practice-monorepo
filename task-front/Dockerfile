FROM node:18-bullseye-slim

# RUN su node -c "umask 0002 && npm install -g http-server"
# RUN su node -c "mkdir /app"
WORKDIR /app
COPY ./app .

EXPOSE 3000
RUN ["npm", "install"]
ENTRYPOINT [ "npm", "run", "public" ]