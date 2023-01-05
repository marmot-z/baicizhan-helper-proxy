import { Methods } from "./method";

export default class Router {
    constructor(public method: Methods, public handler: Function) {
        this.method = method;
        this.handler = handler;
    }
}