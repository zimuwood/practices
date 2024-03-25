import {extend} from 'umi-request'
import * as process from "process";

const request = extend({
  credentials: 'include',
  prefix: process.env.NODE_ENV === 'production' ? "http://cloudyw.cn" : undefined,
})

export default request;
