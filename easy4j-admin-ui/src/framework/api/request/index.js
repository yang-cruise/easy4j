import service from './service'
import qs from 'qs'

export function post (url, params) {
  // return new Promise((resolve, reject) => {
  //   service.post(url, qs.stringify(params)).then(res => {
  //     resolve(res)
  //   }).catch((err) => {
  //     reject(err)
  //   })
  // })

  return service({
    url,
    method: 'post',
    data: qs.stringify(params)
  })
}
