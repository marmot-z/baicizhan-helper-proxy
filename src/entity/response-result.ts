export default class ResponseResult {
    constructor(public code: number, 
                public data?: any, public message?: string) {
        this.code = code;
    } 

    public static successful(data: any): ResponseResult {
        let resp: ResponseResult = new ResponseResult(200);

        resp.data = data;

        return resp;
    }

    public static failure(message: string): ResponseResult {
        let resp: ResponseResult = new ResponseResult(500);

        resp.message = message;

        return resp;
    }

    public static unauthorized(): ResponseResult {
        let resp: ResponseResult = new ResponseResult(401);

        resp.message = "请重新登录";

        return resp;
    }
}