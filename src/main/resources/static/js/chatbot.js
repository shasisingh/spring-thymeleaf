(function () {
    const fab = document.getElementById('chatbotToggle');
    const modal = document.getElementById('chatbotModal');
    const close = document.getElementById('chatbotClose');
    const form = document.getElementById('chatbotForm');
    const input = document.getElementById('chatbotInput');
    const body = document.getElementById('chatbotBody');

    function trapKeys(e) {
        if (e.key === 'Escape') {
            hide();
        }
        if (e.key === 'Tab') {
            const focusables = modal.querySelectorAll('button, [href], input, select, textarea, [tabindex]:not([tabindex="-1"])');
            const first = focusables[0];
            const last = focusables[focusables.length - 1];
            if (e.shiftKey && document.activeElement === first) {
                e.preventDefault();
                last.focus();
            } else if (!e.shiftKey && document.activeElement === last) {
                e.preventDefault();
                first.focus();
            }
        }
    }

    function show() {
        modal.style.display = 'block';
        modal.classList.add('show');
        input.focus();
        modal.addEventListener('keydown', trapKeys);
        document.body.setAttribute('data-modal-open', 'true');
    }

    function hide() {
        modal.classList.remove('show');
        modal.style.display = 'none';
        modal.removeEventListener('keydown', trapKeys);
        document.body.removeAttribute('data-modal-open');
    }

    if (fab) fab.addEventListener('click', show);
    if (close) close.addEventListener('click', hide);
    if (form) form.addEventListener('submit', function (e) {
        e.preventDefault();
        const msg = input.value.trim();
        if (!msg) return;
        const userMsg = document.createElement('div');
        userMsg.className = 'chatbot-message chatbot-message-user';
        userMsg.textContent = msg;
        body.appendChild(userMsg);
        input.value = '';
        // Simulate bot reply
        setTimeout(() => {
            const botMsg = document.createElement('div');
            botMsg.className = 'chatbot-message chatbot-message-bot';
            botMsg.textContent = 'Oops — on a coffee break. Beep me later with snacks. ☕🍪';
            body.appendChild(botMsg);
            body.scrollTop = body.scrollHeight;
        }, 600);
        body.scrollTop = body.scrollHeight;
    });
})();
