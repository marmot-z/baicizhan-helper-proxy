import http, { ServerResponse } from 'http';
import Router from './entity/router';
import { get, post, put, _delete } from './entity/method';
import LoginController from './controller/login-controller';
import ResourceController from './controller/resource-controller';
import ResponseResult from './entity/response-result';
import BookController from './controller/book-controller';
import Int64 from 'node-int64';

const host: string = '127.0.0.1';
const port: number = 8080;
let globalRouters: Map<RegExp, Router>;

function registRouter(): void {
    globalRouters = new Map();
    let loginController: LoginController = new LoginController();
    let resourceController: ResourceController = new ResourceController();
    let bookController: BookController = new BookController();

    globalRouters.set(/^\/login\/sendSmsVerifyCode\/\d{11}/, post(loginController.sendSmsVerifyCode));
    globalRouters.set(/^\/login\/\w+\/\d{6}/, post(loginController.loginWithPhoneNum));
    globalRouters.set(/^\/userInfo/, get(loginController.getUserInfo));
    globalRouters.set(/^\/search\/word\/\w+/, get(resourceController.searchWord));
    globalRouters.set(/^\/word\/\w+/, get(resourceController.getWordDetail));
    globalRouters.set(/^\/books/, get(bookController.getBookList));
    globalRouters.set(/^\/book\/\d+\/word\/\d+/, _delete(bookController.removeWord));
    globalRouters.set(/^\/book\/\d+\/word\/\w+/, put(bookController.addWord));    
}

registRouter();

http.createServer((req, resp) => {     
    let url: string = req.url || '';
    let method: string | undefined = req.method;

    if (typeof method !== 'undefined') {
        // 根据路由进行分发处理
        for (let entry of globalRouters.entries()) {
            if (url.match(entry[0])) {
                if (entry[1].method === method) {
                    entry[1].handler.call(null, req)
                            .then(result => sendResponse(resp, result))
                            .catch(err => {
                                console.error(`处理 ${url} 请求时出现了错误：`, err);
                                resolveError(resp, err as Error);                      
                            });        
                } 
                // 不支持的方法返回响应
                else {
                    sendMethodNotAllowedResponse(resp);
                }

                return;
            }
        }
    }

    // 未识别的路由返回 404
    sendNotFoundResponse(resp);
})
.listen(port, host, () => 
    console.log(`Server running at http://${host}:${port}`)
);

function sendResponse(resp: ServerResponse, result: any): void {
    resp.writeHead(200, {'Content-type': 'application/json; charset=utf-8'});
    let json: string = JSON.stringify(ResponseResult.successful(result), (k, v) => {
        if (v instanceof Int64) {
            return (v as Int64).toNumber();
        }

        return v;
    });
    resp.write(json);
    resp.end();
}

function resolveError(resp: ServerResponse, error: Error): void {
    console.error('出现异常：', error);

    resp.writeHead(200, {'Content-type': 'application/json; charset=utf-8'}); 
    resp.write(JSON.stringify(ResponseResult.failure('系统异常，请稍后重试')));
    resp.end();
}

function sendNotFoundResponse(resp: ServerResponse): void {
    resp.statusCode = 404;
    resp.end();
}

function sendMethodNotAllowedResponse(resp: ServerResponse): void {
    resp.statusCode = 405;
    resp.end();
} 