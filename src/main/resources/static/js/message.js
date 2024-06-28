document.addEventListener('DOMContentLoaded', function() {
    const messageBox = document.getElementById('messageBox');
    if (messageBox) {
        setTimeout(function() {
            messageBox.style.display = 'none';
        }, 3000);
    }
});
