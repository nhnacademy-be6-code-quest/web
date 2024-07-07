if (!window.opener) {
    alert('잘못된 접근입니다.');
    history.go(-1);
}