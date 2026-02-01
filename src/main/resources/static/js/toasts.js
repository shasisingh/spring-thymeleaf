(function () {
    const toasts = document.querySelectorAll('.toast-container .toast');
    toasts.forEach((t, idx) => {
        // show with small stagger
        setTimeout(() => {
            t.classList.add('show');
            // auto hide after 3.5s
            const hideId = setTimeout(() => {
                t.classList.remove('show');
                t.remove();
            }, 3500);
            // dismiss on click
            const closeBtn = t.querySelector('.toast-close');
            if (closeBtn) {
                closeBtn.addEventListener('click', () => {
                    clearTimeout(hideId);
                    t.classList.remove('show');
                    t.remove();
                });
            }
        }, idx * 150);
    });
})();
