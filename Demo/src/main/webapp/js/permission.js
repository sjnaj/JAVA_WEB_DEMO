$(function () {
  //页面显示之前执行权限管理
  var title = getCookie('title')
  var html = getHtmlDocName()

  eval(html + '("' + title + '")')
  console.log(title)
})
function index(title) {
  //只有manager能进行人事管理
  if (title != 'manager') document.getElementById('super').hidden = true

  //普通员工不能查看仓库
  if (title == 'employee') document.getElementById('medium').hidden = true

}
function record(title) {
  //只有manager能查看不同部门信息
  if (title != 'manager') document.getElementById('branch').hidden = true
}
function inventory(title) {
  record(title)
}
function getHtmlDocName() {
  var str = window.location.href
  str = str.substring(str.lastIndexOf('/') + 1)
  str = str.substring(0, str.lastIndexOf('.'))
  return str
}


function getCookie(name) {
  // 拆分 cookie 字符串
  var cookieArr = document.cookie.split(';')

  // 循环遍历数组元素
  for (var i = 0; i < cookieArr.length; i++) {
    var cookiePair = cookieArr[i].split('=')

    /* 删除 cookie 名称开头的空白并将其与给定字符串进行比较 */
    if (name == cookiePair[0].trim()) {
      // 解码cookie值并返回
      return decodeURIComponent(cookiePair[1])
    }
  }
  // 如果未找到，则返回null
  return null
}
