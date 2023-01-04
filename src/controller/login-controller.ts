import { createClient, getPathVariables } from '../util/utils';
import { Client } from '../bin/UnifiedUserService';
import { PhoneFreeVerifyRequest, PhoneLoginRequest, PhoneVerifyCodeRequest, UserBindInfo, UserLoginResult } from '../bin/baicizhan_types';

export default class LoginController {
    public sendSmsVerifyCode(req: Request): Promise<boolean> {
        const path: string = 'https://passport.baicizhan.com/rpc/unified_user_service/send_sms_verify_code';
        const client: Client = createClient(path, Client);
        const phoneNum: string = getPathVariables(req, '/login/sendSmsVerifyCode/{phoneNum}')['phoneNum'];
        const verifyType: number = 5;

        return new Promise((resolve, reject) => {
            client.send_sms_verify_code(phoneNum, verifyType, (err, data) => {
                return err ? reject(err) : resolve(true);
            });
        });
    }

    public loginWithPhoneNum(req: Request): Promise<UserLoginResult> {
        const path: string = 'https://passport.baicizhan.com/rpc/unified_user_service/login_with_phone';
        const client: Client = createClient(path, Client);
        const pathVariables: object = getPathVariables(req, '/login/{phoneNum}/{verifyCode}');
        const phoneNum: string = pathVariables['phoneNum'];
        const verifyCode: string = pathVariables['verifyCode'];

        return new Promise((resolve, reject) => {
            const loginRequest: PhoneLoginRequest = new PhoneLoginRequest({            
                'verify_code_request': new PhoneVerifyCodeRequest({phone: phoneNum, verify_code: verifyCode}),
                'free_verify_request': new PhoneFreeVerifyRequest({ clientToken: '', opToken: '', operatorName: ''}),
                'device': req.headers['device_id'] || ''
            });

            client.login_with_phone(loginRequest, (err, data) => {
                return err ? reject(err) : resolve(data);
            });
        });
    }

    public getUserInfo(req: Request): Promise<UserBindInfo[]> {
        const path: string = 'https://passport.baicizhan.com/rpc/unified_user_service/check_access_token';
        const accessToken: string = req.headers['access_token'];        
        const client: Client = createClient(path, Client, accessToken);
        const deviceId: string = req.headers['device_id'] || '';
        
        return new Promise((resolve, reject) => {
            client.check_access_token(deviceId, (err, _) => {
                if (err) return reject(err);

                client.get_bind_info((error, data) => {
                    return error ? reject(error) : resolve(data);
                });
            });
        });
    }
}