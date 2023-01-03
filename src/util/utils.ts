import {
    TClientConstructor,
    TFramedTransport,
    TCompactProtocol,
    createHttpConnection,
    createHttpClient,
    HttpConnection,
} from 'thrift';

const port: number = 80;
const cookies: object = {
    'device_name': 'iPhone10.3',
    'device_version': '13.4.1',
    'device_id': '450E525F-B0BE-4194-A5BD-DFDF9DC11662',
    'app_name': '7020300',
    'serial': '450E525F-B07165133',
    'os_version': '13.4.1',
    'app_version': '7020300',
    'channel': 'appstore',
};

export function createClient<TClient>(url: string, client: TClientConstructor<TClient>, accessToken?: string): TClient {
    let pattern: RegExp = /https?:\/\/([^/]*)(\/.*)/;
    let matches: RegExpMatchArray = pattern.exec(url);
    let host = matches[1];
    let path = `${matches[2]}/${new Date().getTime()}`;
    let cookie: object = Object.assign(cookies, {'access_token': accessToken || ''});
    let cookieStr: string = Object.entries(cookie).map(([k, v]) => `${k}=${v}`).join(';');
    const options: object = {
        path,
        https: false,
        transport: TFramedTransport,
        protocol: TCompactProtocol,
        headers: {
            'User-Agent': 'Cocoa/THTTPClient bcz_app_iphone/7020300',
            'Accept-Language': 'zh-cn',
            'Cookie': cookieStr,
            'Host': 'resource.baicizhan.com',
        }
    };

    const conn: HttpConnection = createHttpConnection(host, port, options);

    return createHttpClient(client, conn);
}

const pathPattern: RegExp = /\{(\w+)\}/g;

export function getPathVariables(req: Request, path: string): object {
    let url: string = req.url;
    let names: string[] = [];
    let values: string[] = [];
    let match: RegExpExecArray;

    while ((match = pathPattern.exec(path)) != null) {
        names.push(match[1]);
    }

    let pattern: RegExp = new RegExp(path.replace(pathPattern, '([^/]+)'), 'g');

    while ((match = pattern.exec(url)) != null) {
        values.push(match[1]);
    }

    let result: object = {};

    names.forEach((name, i) => result[name] = values[i] || null);

    return result;
}