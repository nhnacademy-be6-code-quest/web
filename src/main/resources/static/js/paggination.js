function paginate(totalPages, currentPage) {
    const paginationContainer = document.querySelector('.pagination');
    const maxVisiblePages = 5; // 보여줄 최대 페이지 번호 수
    let pages = [];

    if (totalPages <= maxVisiblePages) {
        pages = Array.from({ length: totalPages }, (_, i) => i + 1);
    } else {
        const startPage = Math.max(1, currentPage - Math.floor(maxVisiblePages / 2));
        const endPage = Math.min(totalPages, currentPage + Math.floor(maxVisiblePages / 2));

        if (startPage > 1) pages.push('...');
        for (let i = startPage; i <= endPage; i++) pages.push(i);
        if (endPage < totalPages) pages.push('...');
    }

    paginationContainer.innerHTML = '';
    if (currentPage > 0) {
        const prevLink = document.createElement('a');
        prevLink.href = `?page=${currentPage - 1}`;
        prevLink.innerText = 'Previous';
        paginationContainer.appendChild(prevLink);
    }

    pages.forEach(page => {
        if (page === '...') {
            const dotSpan = document.createElement('span');
            dotSpan.innerText = '...';
            paginationContainer.appendChild(dotSpan);
        } else {
            const pageLink = document.createElement('a');
            if (page === currentPage + 1) {
                pageLink.classList.add('active');
            }
            pageLink.href = `?page=${page - 1}`;
            pageLink.innerText = page;
            paginationContainer.appendChild(pageLink);
        }
    });

    if (currentPage < totalPages - 1) {
        const nextLink = document.createElement('a');
        nextLink.href = `?page=${currentPage + 1}`;
        nextLink.innerText = 'Next';
        paginationContainer.appendChild(nextLink);
    }
}

document.addEventListener('DOMContentLoaded', function() {
    const totalPages = parseInt(document.querySelector('.pagination').dataset.totalPages);
    const currentPage = parseInt(document.querySelector('.pagination').dataset.currentPage);
    paginate(totalPages, currentPage);
});
