function getHtmlDocName() {
  var str = window.location.href
  str = str.substring(str.lastIndexOf('/') + 1)
  str = str.substring(0, str.lastIndexOf('.'))
  return str
}

function page(pageNo) {
  window.location.href = getHtmlDocName() + '.do?pageNo=' + pageNo
}
function delStaff(id) {
  if (confirm('确定删除此职工信息?')) {
    window.location.href = 'personnel.do?operate=del&id=' + id
  }
}
function delInventory(id) {
  if (confirm('确定删除此存储信息?')) {
    window.location.href = 'inventory.do?operate=del&id=' + id
  }
}
function editStaff(id) {
  window.location.href = 'personnel.do?operate=select&id=' + id
}
function editInventory(id){
  window.location.href = 'inventory.do?operate=select&id=' + id

}