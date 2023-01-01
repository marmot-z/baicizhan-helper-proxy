import http from 'http';

http.createServer((req, resp) => {
    resp.writeHead(200, {'Content-type': 'text/plain'});
    resp.write(new Buffer('hello world', 'utf-8'));
    resp.end();
})
.listen(8080);

console.log('Server running at http://localhost:8080');