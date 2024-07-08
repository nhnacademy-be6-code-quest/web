function openCategorySearch(){
    var url = "/categories/all";

    var newWindowWidth = 600;  // 새 창의 너비
    var newWindowHeight = 750; // 새 창의 높이

    var availWidth = screen.availWidth;
    var availHeight = screen.availHeight;
    var left = Math.max(0, (availWidth - newWindowWidth) / 2);
    var top = Math.max(0, (availHeight - newWindowHeight) / 2);
    // 새 창 열기
    var newWindow = window.open(url, "_blank", "width=" + newWindowWidth + ",height=" + newWindowHeight + ",top=" + top + ",left=" + left);

    newWindow.focus();
}