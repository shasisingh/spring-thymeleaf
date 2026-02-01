(function () {
    // Accessible confirmation modal hooking
    let pendingForm = null;
    const overlay = document.getElementById('confirmModal');
    const msgEl = document.getElementById('confirmMessage');
    const btnYes = document.getElementById('confirmYes');
    const btnNo = document.getElementById('confirmNo');
    const btnClose = document.getElementById('confirmClose');

    function openConfirm(message) {
        msgEl.textContent = message || 'Are you sure you want to delete this item?';
        overlay.style.display = 'flex';
        overlay.classList.add('show');
        btnYes.focus();
        overlay.addEventListener('keydown', trapKeys);
        document.body.setAttribute('data-modal-open', 'true');
    }

    function closeConfirm() {
        overlay.classList.remove('show');
        overlay.style.display = 'none';
        overlay.removeEventListener('keydown', trapKeys);
        document.body.removeAttribute('data-modal-open');
        pendingForm = null;
    }

    function trapKeys(e) {
        if (e.key === 'Escape') {
            closeConfirm();
        }
        if (e.key === 'Tab') {
            const focusables = overlay.querySelectorAll('button, [href], input, select, textarea, [tabindex]:not([tabindex="-1"])');
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

    btnYes.addEventListener('click', function () {
        if (pendingForm) {
            const f = pendingForm; // keep ref
            closeConfirm();
            // submit after closing to avoid focus issues
            f.submit();
        } else {
            closeConfirm();
        }
    });

    function cancel() {
        closeConfirm();
    }

    btnNo.addEventListener('click', cancel);
    btnClose.addEventListener('click', cancel);
    overlay.addEventListener('click', function (e) {
        if (e.target === overlay) cancel();
    });

    function bindDeleteConfirmsAccessible() {
        document.querySelectorAll('form.confirm-delete').forEach(f => {
            f.addEventListener('submit', function (e) {
                // prevent default and show modal
                e.preventDefault();
                const message = this.getAttribute('data-confirm') || 'Are you sure you want to delete this item?';
                pendingForm = this;
                openConfirm(message);
            });
        });
    }

    document.addEventListener('DOMContentLoaded', bindDeleteConfirmsAccessible);
})();
