// function check() {
//   var s = document.getElementById('username')
//   var p = document.getElementById('password')
//   if (s.value.length == 0 || p.value.length == 0) {
//     alert('用户名和密码不能为空')
//   } else {
//     window.location.href =
//       'check.do?username=' +
//       encodeURI(s.value) +
//       '&password=' +
//       encodeURI(p.value)
//   }
//   while(sessionStorage)

//     alert('用户名或密码错误！')
//     location.href = '登录界面.html?' + 'username=' + encodeURI(s.value)
//   }
function createXMLHttpRequest() {
  var XMLHttpRequest1
  if (window.XMLHttpRequest) {
    XMLHttpRequest_test = new XMLHttpRequest()
  } else if (window.ActiveXObject) {
    try {
      XMLHttpRequest_test = new ActiveXObject('Msxml2.XMLHTTP')
    } catch (e) {
      XMLHttpRequest_test = new ActiveXObject('Microsoft.XMLHTTP')
    }
  }
  return XMLHttpRequest_test
}

function ajax() {
  //param1与param2就是用户在输入框的两个参数
  var username = document.getElementById('username').value
  var password = document.getElementById('password').value
  if (username == '' || password == '') {
    alert('用户名或密码不能为空')
    return
  }
  var XMLHttpRequest_test = createXMLHttpRequest()
  //指明相应页面
  var url = 'check'
  XMLHttpRequest_test.open('POST', url, true)
  //请求头，保证不乱码
  XMLHttpRequest_test.setRequestHeader(
    'Content-Type',
    'application/x-www-form-urlencoded',
  )
  console.log('username=' + username + '&password=' + password)
  XMLHttpRequest_test.send('username=' + username + '&password=' + password)
  XMLHttpRequest_test.onreadystatechange = function () {
    //这个4代表已经发送完毕之后
    if (XMLHttpRequest_test.readyState == 4) {
      //200代表正确收到了返回结果
      if (XMLHttpRequest_test.status == 200) {
        //弹出返回结果
        var s = XMLHttpRequest_test.responseText
        if (s == '登录成功！') {
          window.location.href = 'index.html'
        } else alert(s)
      } else {
        //如果不能正常接受结果，你肯定是断网，或者我的服务器关掉了。
        alert('用户名或密码错误！')
      }
    }
  }
}

function checknull(url, oper) {
  var inputs = document.getElementsByTagName('input')
  var href = url + '.do?operate=' + oper
  for (var input of inputs) {
    if (input.value==false) {
      alert('不能有空信息！')
      return
    }
    href += '&' + input.name + '=' + input.value
  }
  console.log(href)
  window.location.href = href
}
