FROM node:20-alpine

WORKDIR /app

COPY --chown=node:node server.mjs ./

RUN mkdir -p /app && chown -R node:node /app

USER node

EXPOSE 8087

CMD ["node", "server.mjs"]
