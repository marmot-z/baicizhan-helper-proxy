import { createHttpConnection, createHttpClient, TFramedTransport, TCompactProtocol, HttpConnection } from 'thrift';
import { Client } from '../bin/UnifiedUserService';

export default class LoginController {
    public sendSmsVerifyCode(req: Request): boolean {
        const path: string = `/rpc/resource_api/search_word_v2/${new Date().getTime()}`;
        const options: object = {
            path,
            https: false,
            transport: TFramedTransport,
            protocol: TCompactProtocol,
            headers: {
                'User-Agent': 'Cocoa/THTTPClient bcz_app_iphone/7020300',
                'Accept-Language': 'zh-cn',
                'Cookie': 'device_name=iPhone10.3; device_version=13.4.1; device_id=450E525F-B0BE-4194-A5BD-DFDF9DC11661; app_name=7020300; serial=450E525F-B07165133; access_token=/MVQkLcaoe7Fbhoh8r4ybHdFnQiNUSSCFuR5xkBSbcw%3D; os_version=13.4.1; app_version=7020300; channel=appstore; client_time=1659862293',
                'Host': 'resource.baicizhan.com',
            }
        };
        const conn: HttpConnection = createHttpConnection("resource.baicizhan.com", 80, options);
        const client: Client = createHttpClient(Client, conn);

        client.send_sms_verify_code('15870664270', 5, (err, data) => {
            if (err) throw err;

            console.log(data);
        });

        return false;
    }
}