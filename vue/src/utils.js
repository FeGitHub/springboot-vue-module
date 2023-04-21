export function download (data, fileName) {
  let blob
  if (fileName.indexOf('.pdf') > -1) {
    blob = new Blob([data], { type: 'application/pdf' })
  } else if (fileName.indexOf('.xlsx') === -1) {
    blob = new Blob([data], { type: 'application/vnd.ms-excel' })
  } else {
    throw new Error('please check the file suffix name')
  }
  let url = window.URL.createObjectURL(blob)
  const link = document.createElement('a') // 创建a标签
  link.href = url
  link.download = fileName // 重命名文件
  link.click()
  URL.revokeObjectURL(url)
}

/***
 * 通過身份證號碼去獲取生日
 */
export function getBirthday (idcard) {
  let re = /^[0-9a-zA-Z]*$/g
  if (!re.test(idcard)) {
    return '';
  }
  let birthday
  if (idcard.length == 18) {
    let str = idcard.substr(6, 8)
    birthday =
      str.substr(0, 4) + '-' + str.substr(4, 2) + '-' + str.substr(6, 2)
  } else if (idcard.length == 15) {
    let str = idcard.substr(6, 6)
    let num = idcard.substr(6, 2)
    if (num > '20') {
      birthday = '19';
    } else {
      birthday = '20';
    }
    birthday +=
      str.substr(0, 2) + '-' + str.substr(2, 2) + '-' + str.substr(4, 2)
  }
  return birthday
}

/***
 * 身份證校驗
 */
export function isCardId (card) {
  let reg = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/
  return reg.test(card)
}
export default {
  getBirthday,
  isCardId,
  download
}
