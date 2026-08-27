import http from 'node:http';

const HOSTNAME = '0.0.0.0';
const PORT = 8087;

const RESPONSE_BODY = {
  versionResponse: 'alpha',
  reservationId: '8afa90f9-5fec-428f-bd17-aec9cc657284'
};

const server = http.createServer((req, res) => {
  if (req.method === 'POST' && req.url === '/v1/parties/digitalbank/accounting-transaction/reservation/webhook') {
    res.statusCode = 200;
    res.setHeader('Content-Type', 'application/json');
    res.end(JSON.stringify(RESPONSE_BODY));
    return;
  }

  res.statusCode = 404;
  res.setHeader('Content-Type', 'application/json');
  res.end(JSON.stringify({ error: 'Not found' }));
});

server.listen(PORT, HOSTNAME, () => {
  console.log(`Server running at http://${HOSTNAME}:${PORT}`);
});

// Optional, if you want to import `server` in another file:
// export { server };