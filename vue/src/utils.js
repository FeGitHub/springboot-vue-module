
export function getBirthday (idcard) {
  let re = /^[0-9a-zA-Z]*$/g;
  if (!re.test(idcard)) {
    return '';
  }
  let birthday;
  if (idcard.length == 18) {
    let str = idcard.substr(6, 8);
    birthday = str.substr(0, 4) + '-' + str.substr(4, 2) + '-' + str.substr(6, 2);
  } else if (idcard.length == 15) {
    let str = idcard.substr(6, 6);
    let num = idcard.substr(6, 2);
    if (num > '20') {
      birthday = '19';
    } else {
      birthday = '20';
    }
    birthday += str.substr(0, 2) + '-' + str.substr(2, 2) + '-' + str.substr(4, 2);
  }
  return birthday;
}



export function isCardId (card) {
  let reg = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;
  return reg.test(card);
}
export default {
  getBirthday,
  isCardId
};
