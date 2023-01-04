import { UserBookInfo, AddWordsRsp, UserBookWord } from "../bin/baicizhan_types";
import { createClient, getPathVariables } from "../util/utils";
import { Client } from "../bin/UserBookService";
import Int64 from "node-int64";

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

    public addWord(req: Request): Promise<AddWordsRsp> {
        const path: string = 'https://booklist.baicizhan.com/rpc/user_book/match_words';
        const accessToken: string = req.headers['access_token'];        
        const client: Client = createClient(path, Client, accessToken);
        const pathVariables: object = getPathVariables(req, '/book/{bookId}/word/{word}');
        const bookId: Int64 = new Int64(pathVariables['bookId']);
        const word: string = pathVariables['word'];

        return new Promise((resolve, reject) => {
            client.match_words(word, (err, data) => {
                if (err) return reject(err);

                const userBooks: UserBookWord[] = data;

                if (!userBooks || !userBooks.length) {
                    return resolve(null);
                }

                client.add_words_to_book(bookId, [userBooks[0]], (error, addWordResp) => {
                    if (error) return reject(error);
                    
                    resolve(addWordResp);
                })
            });
        });
    }

    public removeWord(req: Request): Promise<AddWordsRsp> {
        const path: string = 'https://booklist.baicizhan.com/rpc/user_book/match_words';
        const accessToken: string = req.headers['access_token'];        
        const client: Client = createClient(path, Client, accessToken);
        const pathVariables: object = getPathVariables(req, '/book/{bookId}/word/{topicId}');
        const bookId: number = parseInt(pathVariables['bookId']);
        const topicId: number = pathVariables['topicId'];        

        return new Promise((resolve, reject) => {
            const bookWord: UserBookWord = new UserBookWord();
            bookWord.book_id = bookId;
            bookWord.topic_id = topicId;

            client.add_words_to_book(new Int64(bookId), [bookWord], (err, data) => {
                return err ? reject(err) : resolve(data);
            });
        });
    }
}