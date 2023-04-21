import https from '@/https';

/***
 * 导出
 */
export function testDownLoadTemplate (data) {
  return https.request({
    url: '/excel/testDownLoadTemplate',
    method: 'post',
    responseType: 'blob',
    data: data
  })
}

/***
 * 导出
 */
export function createPdfByTemplate (data) {
  return https.request({
    url: '/test/createPdfByTemplate',
    method: 'post',
    responseType: 'blob',
    data: data
  })
}
