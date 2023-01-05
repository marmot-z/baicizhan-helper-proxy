import Router from "./router";

type Methods = 'GET' | 'POST' | 'PUT' | 'DELETE';

function get(handler: (req: Request) => Promise<any>): Router {
    return new Router('GET', handler);
}

function post(handler: (req: Request) => Promise<any>): Router {
    return new Router('POST', handler);
}

function put(handler: (req: Request) => Promise<any>): Router {
    return new Router('PUT', handler);
}

function _delete(handler: (req: Request) => Promise<any>): Router {
    return new Router('DELETE', handler);
}

export {Methods, get, post, put, _delete};