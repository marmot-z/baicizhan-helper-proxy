import { createClient, getPathVariables } from '../util/utils';
import { SearchWordResultV2, TopicResourceV2, TopicKey, GetTopicResourceChannel } from '../gen/baicizhan_types';
import { Client } from '../gen/ResourceService';

export default class ResourceController {
    public searchWord(req: Request): Promise<SearchWordResultV2[]> {
        const path: string = `https://resource.baicizhan.com/rpc/resource_api/search_word_v2/`;        
        const word: string = getPathVariables(req, '/search/word/{word}')['word'];
        const accessToken: string = req.headers['access_token'];
        const client: Client = createClient(path, Client, accessToken);

        return new Promise((resolve, reject) => {
            client.search_word_v2(word, (err, data) => {
                return err ? reject(err) : resolve(data)
            });
        });
    }

    public getWordDetail(req: Request): Promise<TopicResourceV2> {
        const path: string = `https://resource.baicizhan.com/rpc/resource_api/get_topic_resource_v2/`; 
        const topicId: number = parseInt(getPathVariables(req, '/word/{topicId}')['topicId']);
        const accessToken: string = req.headers['access_token'];
        const client: Client = createClient(path, Client, accessToken);

        const wordLevelId: number = 0;
        const tagId: number = 0;
        const withZpk: boolean = false;
        const withDict: boolean = true;
        const withDictWiki: boolean = false;
        const withMedia: boolean = false;
        const withSimilalWords: boolean = false;

        return new Promise((resolve, reject) => {
            client.get_topic_resource_v2(
                new TopicKey({topic_id: topicId, word_level_id: wordLevelId, tag_id: tagId}),
                GetTopicResourceChannel.LOOK_UP,
                withZpk, withDict, withDictWiki, withMedia, withSimilalWords,
                (err, data) => {
                    return err ? reject(err) : resolve(data)
                }
            );
        });        
    }
}