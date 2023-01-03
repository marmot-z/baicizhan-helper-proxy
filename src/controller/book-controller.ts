import { UserBookInfo } from "../bin/baicizhan_types";
import { createClient } from "../util/utils";
import { Client } from "../bin/UserBookService";

export default class BookController {
    public getBookList(req: Request): Promise<UserBookInfo> {
        const path: string = `https://booklist.baicizhan.com/rpc/user_book/get_user_books`;
        const accessToken: string = req.headers['access_token'];
        const client: Client = createClient(path, Client, accessToken);

        return new Promise((resolve, reject) => {
            client.get_user_books((err, data) => {
                return err ? reject(err) : resolve(data);
            });
        });        
    }
}