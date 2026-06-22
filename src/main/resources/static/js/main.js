/**
 * main.js — general UI utilities for AI Interview Platform
 */

// ── Interview Timer ───────────────────────────────────────────────────────────
window.startTimer = function (durationMinutes) {
    const badge = document.getElementById('timerBadge');
    if (!badge) return;

    let remaining = durationMinutes * 60;

    function update() {
        const m = Math.floor(remaining / 60);
        const s = remaining % 60;
        badge.textContent = String(m).padStart(2, '0') + ':' + String(s).padStart(2, '0');

        badge.classList.remove('warning', 'danger');
        if (remaining <= 120) badge.classList.add('danger');
        else if (remaining <= 300) badge.classList.add('warning');

        if (remaining <= 0) {
            clearInterval(timer);
            badge.textContent = '00:00';
            document.getElementById('interviewForm')?.submit();
        }
        remaining--;
    }

    update();
    const timer = setInterval(update, 1000);
};

// ── Auto-dismiss flash alerts ─────────────────────────────────────────────────
document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.alert.auto-dismiss').forEach(el => {
        setTimeout(() => {
            el.style.opacity = '0';
            el.style.transform = 'translateY(-6px)';
            el.style.transition = 'all 0.4s ease';
            setTimeout(() => el.remove(), 400);
        }, 4000);
    });

    // ── Animate-in elements ───────────────────────────────────────────────────
    document.querySelectorAll('.animate-in').forEach((el, i) => {
        el.style.opacity = '0';
        el.style.transform = 'translateY(18px)';
        el.style.transition = `opacity 0.4s ease ${i * 0.06}s, transform 0.4s ease ${i * 0.06}s`;
        requestAnimationFrame(() => {
            requestAnimationFrame(() => {
                el.style.opacity = '1';
                el.style.transform = 'translateY(0)';
            });
        });
    });

    // ── Active nav link highlight ─────────────────────────────────────────────
    const path = window.location.pathname;
    document.querySelectorAll('.nav-link').forEach(link => {
        const href = link.getAttribute('href') || '';
        if (href && path.startsWith(href) && href !== '/') {
            link.classList.add('active');
        } else if (href === path) {
            link.classList.add('active');
        }
    });

    // ── Textarea character count ──────────────────────────────────────────────
    document.querySelectorAll('textarea[data-max]').forEach(ta => {
        const max = parseInt(ta.dataset.max);
        const counter = document.createElement('div');
        counter.className = 'form-text mt-1';
        counter.style.color = 'var(--text-dim)';
        counter.style.fontSize = '0.75rem';
        ta.parentNode.appendChild(counter);
        function update() {
            const len = ta.value.length;
            counter.textContent = len + ' / ' + max + ' characters';
            counter.style.color = len > max * 0.9 ? 'var(--warning)' : 'var(--text-dim)';
        }
        ta.addEventListener('input', update);
        update();
    });

    // ── Confirm deletes ───────────────────────────────────────────────────────
    document.querySelectorAll('[data-confirm]').forEach(el => {
        el.addEventListener('click', e => {
            if (!confirm(el.dataset.confirm || 'Are you sure?')) e.preventDefault();
        });
    });
});
